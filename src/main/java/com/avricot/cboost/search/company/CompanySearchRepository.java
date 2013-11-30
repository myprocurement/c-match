package com.avricot.cboost.search.company;

import com.avricot.cboost.service.Company;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 *
 */
public interface CompanySearchRepository extends ElasticsearchCrudRepository<Company, String>, CompanySearchRepositoryCustom {

}
