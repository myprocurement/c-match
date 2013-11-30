package com.avricot.cboost.service.project.matcher;

import com.avricot.cboost.domain.project.Field;
import com.avricot.cboost.domain.project.FieldType;
import com.avricot.cboost.domain.project.Project;
import com.avricot.cboost.repository.ProjectRepository;
import com.avricot.cboost.search.company.CompanySearchRepository;
import com.avricot.cboost.service.Company;
import com.avricot.cboost.service.project.matcher.match.AddressMatcher;
import com.avricot.cboost.service.project.matcher.match.IdMatcher;
import com.avricot.cboost.service.project.matcher.match.NameMatcher;
import com.avricot.cboost.service.project.reader.FileReaderService;
import com.avricot.cboost.service.project.reader.ILineReader;
import com.avricot.cboost.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class MatchingService {

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private IdMatcher idMatcher;

    @Inject
    private NameMatcher nameMatcher;

    @Inject
    private AddressMatcher addressMatcher;

    @Inject
    private FileReaderService fileReaderService;

    @Inject
    private CompanySearchRepository companySearch;

    @Value("${tmp.file.location}")
    private String tmpFileLocation;

    public void match(final Long projectId, final MatchConfiguration matchConfiguration) {
        final Project project = projectRepository.findOneFetchingMapping(projectId);
        String extension = StringUtils.getFilenameExtension(project.getFileName());
        File file = new File(tmpFileLocation + "/cboost-" + project.getId() + "." + extension);
        final AtomicInteger lineNumber = new AtomicInteger();
        final List<Company> companies = new ArrayList<Company>();
        fileReaderService.process(file, new ILineReader() {
            @Override
            public void processLine(final Integer i, final String[] split) {
                Company company = null ; //new Company(projectId);
                for (Field field : project.getMapping().values()) {
                    String value = split[field.getPosition()];
                    value = StringUtils.trimWhitespace(value);
                    if (StringUtils.hasTextNotNullString(value)) {
                        if (field.getType() == FieldType.ADDRESS) {
                            company.setAddress(value);
                        } else if (field.getType() == FieldType.NATIONAL_ID) {
                            company.getNationalIds().add(value);
                        } else if (field.getType() == FieldType.CITY) {
                            company.setCity(value);
                        } else if (field.getType() == FieldType.COUNTRY) {
                            company.setCountry(value);
                        } else if (field.getType() == FieldType.ID) {
                            company.setId(value);
                        } else if (field.getType() == FieldType.NAME) {
                            company.setName(value);
                        } else if (field.getType() == FieldType.ZIP_CODE) {
                            company.setZipCode(value);
                        }
                    }
                }
                List<Company> matching = matchCompany(company, matchConfiguration);
                if(matching.size() == 1){
                    //company.setCompany(matching.get(0));
                }
                companies.add(company);
            }
        }, project.getCharset());
        companySearch.save(companies);
    }

    /**
     * Match the company with the given matcher.
     */
    public List<Company> matchCompany(final Company company, final MatchConfiguration matchConfiguration) {
        List<Company> companies = idMatcher.matchBusinessId(company, matchConfiguration.isOnlyHeadOffice());
        if(companies.size() == 1){
            return companies;
        } else if (!companies.isEmpty()){
            companies = addressMatcher.matchAddress(company, companies);
        } else {
            return nameMatcher.matchByName(company, matchConfiguration.isOnlyHeadOffice());
        }
        return companies;
    }
}
