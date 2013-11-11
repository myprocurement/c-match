package com.avricot.cboost.domain;

import org.springframework.util.ObjectUtils;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class DefaultObject implements Identifiable<Long>, Serializable {
    private static final long serialVersionUID = -4505680552194377804L;

    @Column(name = "ID")
    @Id
    @GeneratedValue
    private java.lang.Long id;

    public DefaultObject() {
    }

    public DefaultObject(final Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            return 0;
        }
        return getId().intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (!(obj instanceof Identifiable)) {
            return false;
        }
        final Identifiable<Long> otherObj = (Identifiable<Long>) obj;

        if (otherObj.getId() == null && getId() == null) {
            return false;
        }
        if (!getId().equals(otherObj.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        if (getId() == null) {
            return "no Id, new object";
        }
        return ObjectUtils.nullSafeToString(getId());
    }
}
