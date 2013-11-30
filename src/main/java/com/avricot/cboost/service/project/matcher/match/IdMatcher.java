package com.avricot.cboost.service.project.matcher.match;

import com.avricot.cboost.search.company.CompanySearchRepository;
import com.avricot.cboost.service.Company;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class IdMatcher {

    @Inject
    private CompanySearchRepository companySearch;

    public List<Company> matchBusinessId(final Company companyFile, final boolean onlyHeadOffice){
//        if(companyFile.getNationalId() == null){
//            return Collections.emptyList();
//        }
        String cleanedId = null; //companyFile.getNationalId().replaceAll("[-.\\s]", "");
        ArrayList<String> ids = Lists.newArrayList(cleanedId);
        if(cleanedId.startsWith("FR") && cleanedId.length() == 2+2+9){
            ids.add(cleanedId.substring(4));
        }
        List<Company> companies = companySearch.searchById(companyFile.getCountry(), onlyHeadOffice, ids);
        //A search with a siret will return the siret and all the companies with the same siren.
        //In this case, we just want to return the siret.
        List<Company> exactMatch = new ArrayList<Company>();
        for(Company company : companies){
//            if(ids.contains(company.getNationalId())){
//                exactMatch.add(company);
//            }
        }
        if(!exactMatch.isEmpty()){
            return exactMatch;
        }
        return companies;
    }
}
