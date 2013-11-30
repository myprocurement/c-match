package com.avricot.cboost.service;

import java.util.List;

/**
 *
 */
public class Company extends AbstractCompany {
    private List<String> nationalIds;

    public Company() {
    }

    public List<String> getNationalIds() {
        return nationalIds;
    }

    public void setNationalIds(final List<String> nationalIds) {
        this.nationalIds = nationalIds;
    }
}
