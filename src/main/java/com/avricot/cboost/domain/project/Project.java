package com.avricot.cboost.domain.project;

import com.avricot.cboost.domain.DefaultObject;
import com.avricot.cboost.domain.IdType;
import com.avricot.cboost.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A user.
 */
@Entity
@Table(name = "T_PROJECT")
public class Project extends DefaultObject {

    @NotNull
    @Size(min = 0, max = 50)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "charset")
    private String charset;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "T_PROJECT_USER", joinColumns = {@JoinColumn(name = "project_id")}, inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> users;

    @JsonIgnore
    @OneToMany(mappedBy = "project")
    @MapKey(name = "type")
    private Map<FieldType, Field> mapping = new HashMap<FieldType, Field>();

    @Column(name = "business_id_type")
    @Enumerated(EnumType.STRING)
    private IdType businessIdType;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(final Set<User> users) {
        this.users = users;
    }

    public Map<FieldType, Field> getMapping() {
        return mapping;
    }

    public void setMapping(final Map<FieldType, Field> mapping) {
        this.mapping = mapping;
    }

    public IdType getBusinessIdType() {
        return businessIdType;
    }

    public void setBusinessIdType(final IdType businessIdType) {
        this.businessIdType = businessIdType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(final String contentType) {
        this.contentType = contentType;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(final String charset) {
        this.charset = charset;
    }
}
