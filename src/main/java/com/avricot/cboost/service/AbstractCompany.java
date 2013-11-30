package com.avricot.cboost.service;

import java.util.List;

/**
 *
 */
public class AbstractCompany {
    private String id;
    private String name;
    private String address;
    private String address2;
    private String zipCode;
    private String city;
    private String country;
    private boolean headOffice;

    public AbstractCompany() {

    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public boolean isHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(final boolean headOffice) {
        this.headOffice = headOffice;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(final String address2) {
        this.address2 = address2;
    }
}
