package com.avricot.cboost.service.project.matcher.match;


import com.avricot.cboost.service.Company;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AbstractMatcher {

    public List<String> getIds(List<Company> companies){
        List<String> ids = new ArrayList<String>();
        for(Company c : companies){
            //ids.add(c.getNationalId());
        }
        return ids;
    }
}
