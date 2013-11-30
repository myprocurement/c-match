package com.avricot.cboost.service.project.matcher.match;

import com.avricot.cboost.search.company.CompanySearchRepository;
import com.avricot.cboost.service.Company;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 *
 */
@Service
public class AddressMatcher extends AbstractMatcher {

    @Inject
    private CompanySearchRepository companySearch;

    public List<Company> matchAddress(final Company company, final List<Company> companies) {
        /**
         * First search companies with the same zip code.
         */
        if(company.getZipCode() != null){
            List<Company> matchingZipCode = companySearch.searchByAddress(company.getZipCode(), company.getCity(), company.getAddress(), getIds(companies));
            if(matchingZipCode.size() == 1){
                return matchingZipCode;
            } else if (!matchingZipCode.isEmpty()){
                //Multi match with the same zip code and address. Pick the head office.
                for(Company c : matchingZipCode){
                    if(c.isHeadOffice()){
                        return Lists.newArrayList(c);
                    }
                }
                return matchingZipCode;
            }
            List<Company> matchingAddress = searchByAddressAndHeadOffice(company, companies);
            return matchingAddress.isEmpty() ? matchingZipCode : matchingAddress;
        }
        return searchByAddressAndHeadOffice(company, companies);
    }

    /**
     * Search by address. If multimatch select the headOffice matching the result.
     */
    private List<Company> searchByAddressAndHeadOffice(final Company company, final List<Company> companies) {
        List<Company> matchingAddress = companySearch.searchByAddress(company.getAddress(), getIds(companies));
        if(matchingAddress.size() == 1){
            return matchingAddress ;
        } else if (!matchingAddress.isEmpty()){
            //Multi match with the same zip code and address. Pick the head office.
            for(Company c : matchingAddress){
                if(c.isHeadOffice()){
                    return Lists.newArrayList(c);
                }
            }
        }
        return matchingAddress;
    }
}
