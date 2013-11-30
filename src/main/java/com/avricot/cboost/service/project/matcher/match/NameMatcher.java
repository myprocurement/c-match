package com.avricot.cboost.service.project.matcher.match;

import com.avricot.cboost.search.company.CompanySearchRepository;
import com.avricot.cboost.service.Company;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Service
public class NameMatcher {

    @Inject
    private CompanySearchRepository companySearch;

    public List<Company> matchByName(final Company company, final boolean onlyHeadOffice){
        if(company.getName() == null){
            return Collections.emptyList();
        }
        return companySearch.searchByName(company.getName(), company.getCountry(), onlyHeadOffice);
    }
}
