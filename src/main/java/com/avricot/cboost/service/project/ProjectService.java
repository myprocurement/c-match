package com.avricot.cboost.service.project;

import com.avricot.cboost.domain.project.Field;
import com.avricot.cboost.domain.project.FieldType;
import com.avricot.cboost.domain.project.Project;
import com.avricot.cboost.repository.ProjectRepository;
import com.avricot.cboost.service.project.reader.FileReaderService;
import com.avricot.cboost.service.project.reader.ILineReader;
import com.avricot.cboost.utils.EnumJson;
import com.avricot.cboost.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class for managing projects.
 */
@Service
@Transactional
public class ProjectService {
    public final static List<EnumJson> FIELD_TYPES = new ArrayList<EnumJson>();
    private static final Logger log = LoggerFactory.getLogger(ProjectService.class);

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private FileReaderService fileReaderService;

    @Value("${tmp.file.location}")
    private String tmpFileLocation;

    @PostConstruct
    public void initFieldType() {
        FIELD_TYPES.clear();
        for (FieldType fieldType : FieldType.values()) {
            FIELD_TYPES.add(fieldType.getJson());
        }
    }


    /**
     * Init the demo project, copy the demo file to the file location.
     */
    public void initDemoProject() {
        Path path = Paths.get(tmpFileLocation);
        Path demoFile = path.resolve("cboost-1.csv");
        try {
            Files.deleteIfExists(demoFile);
            Files.copy(new ClassPathResource("demo/base-demo.csv").getInputStream(), demoFile);
            log.info("demo application loaded");
        } catch (IOException e) {
            throw new ProjectInitializationError("can't load demo project", e);
        }
    }


    public ProjectMappingJson getById(final Long projectId) {
        Project project = projectRepository.findOneFetchingMapping(projectId);
        String extension = StringUtils.getFilenameExtension(project.getFileName());
        File file = new File(tmpFileLocation + "/cboost-" + project.getId() + "." + extension);
        final ProjectMappingJson result = new ProjectMappingJson(project, FIELD_TYPES);

        //Read the 10 first lines of the file, for mapping preview and auto-mapping.
        fileReaderService.process(file, new ILineReader() {
            @Override
            public void processLine(final Integer i, final String[] split) {
                result.getFilePreview().add(split);
            }
        }, project.getCharset(), 15);
        autoMap(result);
        for(Field field : project.getMapping().values()){
            result.getMatchingFieldType().set(field.getPosition(), field.getType());
        }
        return result;
    }

    /**
     * Try to automatically map the file rows, if none is set.
     * TODO : not really clever for now.
     */
    private void autoMap(ProjectMappingJson project) {
        if(project.getProject().getMapping().isEmpty()){
            Map<FieldType, Integer> headerMatch = new HashMap<FieldType, Integer>();
            int i = 0;
            for(String row : project.getFilePreview().get(0)){
                FieldType fieldType = guessHeadFieldType(row);
                if(fieldType != null){
                    headerMatch.put(fieldType, i);
                }
                i++;
            }
            Map<Integer, Map<FieldType, Integer>> match = matchLineContent(project.getFilePreview());
            for(FieldType fieldType : FieldType.values()){
                //Header match, we take it
                if(headerMatch.containsKey(fieldType)){
                    Field field = new Field(fieldType, headerMatch.get(fieldType));
                    project.getProject().getMapping().put(fieldType, field);
                } //Otherwise we try by field content
                else{
                    Integer maxCount = 0;
                    for(Map.Entry<Integer, Map<FieldType, Integer>> e : match.entrySet()){
                        for(Map.Entry<FieldType, Integer> count : e.getValue().entrySet()){
                            if(count.getKey() == fieldType && count.getValue()>maxCount){
                                maxCount = count.getValue();
                                Field field = new Field(count.getKey(), e.getKey());
                                project.getProject().getMapping().put(count.getKey(), field);
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * Match each row content.
     */
    protected Map<Integer, Map<FieldType, Integer>> matchLineContent(final List<String[]> lines) {
        Map<Integer, Map<FieldType, Integer>> match = new HashMap<Integer, Map<FieldType, Integer>>();
        for (String[] line :lines) {
            int j = 0;
            for(String row : line){
                FieldType fieldType = guessFieldType(row);
                if(fieldType != null){
                    if(!match.containsKey(j)){
                        match.put(j, new HashMap<FieldType, Integer>());
                    }
                    Integer count = match.get(j).containsKey(fieldType) ? match.get(j).get(fieldType) : 0;
                    match.get(j).put(fieldType, count+1);
                }
                j++;
            }
        }
        return match;
    }

    private final static String[] VAT = new String[]{"AT", "BE", "BG", "CY", "CZ", "DE", "DK", "EE", "EL", "FS", "FI", "FR", "GB", "HR", "HU", "IE", "IT", "LT", "LU", "LV", "MT", "NL", "PL", "PT", "RO", "SE", "SI"};
    private final static Pattern ADDRESS_PATTERN = Pattern.compile(".*(cedex|rue|street|avenue|av|place|boulevard|bd).*", Pattern.CASE_INSENSITIVE); // you can also use (?i) at the beginning

    /**
     * Try to guess field type with row header
     */
    protected FieldType guessHeadFieldType(final String field) {
        if (!StringUtils.hasText(field)) {
            return null;
        }
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.getHeadPattern().matcher(field).matches() &&
                    (fieldType.getHeadAntiPattern() == null || !fieldType.getHeadAntiPattern().matcher(field).matches())) {
                return fieldType;
            }
        }
        return null;
    }

    /**
     * Try to guess field type with field content.
     */
    protected FieldType guessFieldType(final String field) {
        if (!StringUtils.hasText(field)) {
            return null;
        }
        //Match siret / siren
        if (StringUtils.isNumber(field) && (field.length() == 9 || field.length() == 14)) {
            return FieldType.ID;
        }
        //Simple tva match
        for (String tvaIntra : VAT) {
            if (field.length() >= 10 && field.length() <= 14 && field.startsWith(tvaIntra)) {
                return FieldType.ID;
            }
        }
        //Address
        Matcher matcher = ADDRESS_PATTERN.matcher(field);
        if (matcher.matches()) {
            return FieldType.ADDRESS;
        }
        //Zip code
        if (field.length() == 5 && StringUtils.isNumber(field)) {
            return FieldType.ZIP_CODE;
        }
        return null;
    }

}
