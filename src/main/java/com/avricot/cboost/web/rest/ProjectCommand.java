package com.avricot.cboost.web.rest;

import com.avricot.cboost.domain.project.FieldType;

import java.util.List;

/**
 *
 */
public class ProjectCommand {
    private Long id;
    private List<FieldType> fields;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<FieldType> getFields() {
        return fields;
    }

    public void setFields(final List<FieldType> fields) {
        this.fields = fields;
    }
}
