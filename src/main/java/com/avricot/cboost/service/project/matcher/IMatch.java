package com.avricot.cboost.service.project.matcher;

import com.avricot.cboost.domain.project.FieldType;
import com.avricot.cboost.service.Company;

import java.util.List;

/**
 *
 */
public interface IMatch {

    List<Company> match(Company company, List<Company> companies);

    FieldType getFieldType();
}
