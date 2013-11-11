package com.avricot.cboost.service.project;

import com.avricot.cboost.domain.project.FieldType;
import com.avricot.cboost.domain.project.Project;
import com.avricot.cboost.utils.EnumJson;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ProjectMappingJson {
    private final Project project;
    private final List<String[]> filePreview = new ArrayList<String[]>();
    private final List<EnumJson> fieldTypes ;
    private List<FieldType> matchingFieldType = new ArrayList<FieldType>();

    public ProjectMappingJson(final Project project, final List<EnumJson> fieldTypes) {
        this.project = project;
        this.fieldTypes = fieldTypes;
    }

    public Project getProject() {
        return project;
    }

    public List<String[]> getFilePreview() {
        return filePreview;
    }

    public List<EnumJson> getFieldTypes() {
        return fieldTypes;
    }

    public List<FieldType> getMatchingFieldType() {
        if(matchingFieldType.isEmpty()){
            for(int i=0;i<filePreview.get(0).length;i++){
                matchingFieldType.add(null);
            }
        }
        return matchingFieldType;
    }

    public void setMatchingFieldType(final List<FieldType> matchingFieldType) {
        this.matchingFieldType = matchingFieldType;
    }

}
