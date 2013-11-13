package com.avricot.cboost.domain.project;

import com.avricot.cboost.domain.DefaultObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "T_FIELD")
public class Field extends DefaultObject {

    public Field(final FieldType type, final Integer position) {
        this.type = type;
        this.position = position;
    }

    public Field() {
    }

    public Field(final FieldType type, final Integer position, final Project project) {
        this.type = type;
        this.position = position;
        this.project = project;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    private FieldType type;

    private Integer position;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    public FieldType getType() {
        return type;
    }

    public void setType(final FieldType type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(final Integer position) {
        this.position = position;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }
}
