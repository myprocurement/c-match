package com.avricot.cboost.search.country;

import com.avricot.cboost.search.company.CompanySearchException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;

/**
 *
 */
//@Service
public class CountrySearch {
    private static final Logger log = LoggerFactory.getLogger(CountrySearch.class);

    private final Client client;
    private final ObjectMapper mapper;

    //@Inject
    public CountrySearch (@Value("es.cluster.name") final String clusterName){
        Node node = NodeBuilder.nodeBuilder().client(true).clusterName(clusterName).node();
        this.client = node.client();
        this.mapper = new ObjectMapper();
    }

    public Country searchCountry(final String value){
        SearchResponse response = this.client.prepareSearch("cboost").setTypes("country").setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("values", value))
                .setSize(1).execute().actionGet();
        if(response.getHits().getTotalHits() == 0){
            return null;
        }
        SearchHit country = response.getHits().getAt(0);
        String json = country.getSourceAsString();
        try {
            return mapper.readValue(country.getSourceAsString(), Country.class);
        } catch (IOException e) {
            log.error("error reading json value");
            throw new CompanySearchException("error reading json value :"+json);
        }
    }

    public void insertCountry(final Country country){
        try {
            String json = mapper.writeValueAsString(country);
            client.prepareIndex("cboost", "country").setSource(json).execute();
        } catch (JsonProcessingException e) {
            log.error("error writing json", e);
            throw new CompanySearchException("error writing json", e);
        }

    }

}
