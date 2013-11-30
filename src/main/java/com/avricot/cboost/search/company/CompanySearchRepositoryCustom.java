package com.avricot.cboost.search.company;

import com.avricot.cboost.service.Company;

import java.util.List;

/**
 *
 */
public interface CompanySearchRepositoryCustom {
    /**
     * Search a company by its national id.
     * Must be indexed with all different id type (should contain TVA, SIREN, SIRET when possible).
     * Must perform exact match.
     * Must perform a filter by country if not null.
     * Must perform a filter on headOffice=true when applyHeadOfficeFilter=true.
     *
     * Use multivalued field to store id. 123456789 must match :
     * TVA : FR01123456789
     * SIREN : 123456789
     * SIRET : 12345678912345
     */
    public List<Company> searchById(final String country, final boolean applyHeadOfficeFilter, final List<String> ids);

    /**
     * Search companies with the given address.
     * Must perform (+(:zipCode Or :city) :address)
     * Should use EdgeNgram for zipCode (92 should match 92500)
     * Should use exact match on city ?
     *
     * Must perform a filter by company ids.
     */
    public List<Company> searchByAddress(final String zipCode, final String city, final String address, final List<String> ids);

    /**
     * Search companies with the given address.
     * mainAddress must be address with stopword on all numbers/bis and street qualifier(rue|boulevard|avenue|place etc)
     * Must perform (+(:mainAddress=address) :address=address).
     *
     * Must perform a filter by company ids.
     */
    public List<Company> searchByAddress(final String address, final List<String> ids);

    /**
     * Search companies with the same name.
     * Must match on (:exactName=name, :name=name)
     *
     * Must perform filter by country if not null.
     * Must perform a filter on headOffice=true when applyHeadOfficeFilter=true.
     */
    public List<Company> searchByName(final String name, final String country, final boolean onlyHeadOffice);

}
