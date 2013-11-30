package com.avricot.cboost.search.company;

import com.avricot.cboost.service.Company;

import java.util.List;

/**
 *
 */
public class CompanySearchRepositoryImpl implements CompanySearchRepositoryCustom {
    @Override
    public List<Company> searchById(final String country, final boolean applyHeadOfficeFilter, final List<String> ids) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Company> searchByAddress(final String zipCode, final String city, final String address, final List<String> ids) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Company> searchByAddress(final String address, final List<String> ids) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Company> searchByName(final String name, final String country, final boolean onlyHeadOffice) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
