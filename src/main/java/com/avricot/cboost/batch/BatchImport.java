package com.avricot.cboost.batch;

import com.avricot.cboost.service.Company;
import com.avricot.cboost.service.project.reader.FileReaderService;
import com.avricot.cboost.service.project.reader.ILineReader;
import com.avricot.cboost.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 */
public class BatchImport {
    public static void main(String[] args) throws IOException {
//        InputStream in = Files.newInputStream(Paths.get("/home/quentin/tmp/export.csv"));
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        File file = Paths.get("/home/quentin/tmp/export.csv").toFile();
        Company lastCompany = new Company();
        Node node = NodeBuilder.nodeBuilder().client(true).clusterName("cboost").node();
        final Client client = node.client();
        //client.admin().indices().prepareDelete("company").execute().actionGet();
        final BulkRequestBuilder bulkRequest = client.prepareBulk();
        final ObjectMapper mapper = new ObjectMapper();
        FileReaderService readerService = new FileReaderService();
        readerService.process(file, new ILineReader<Company>() {
            @Override
            public Company processLine(final Integer lineNumber, final String[] fields, final Company lastCompany) {
                int i = 0;
                String id = fields[i++];
                Company company;
                if (lastCompany != null && lastCompany.getId().equals(id)) {
                    company = lastCompany;
                } else {
                    if (lastCompany != null) {
                        try {
                            String json = mapper.writeValueAsString(lastCompany);
                            bulkRequest.add(client.prepareIndex("cboost", "company", lastCompany.getId()).setSource(json));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                    company = new Company();
                    company.setId(id);
                    company.setNationalIds(new ArrayList<String>());
                }
                company.setName(fields[i++]);
                String nationalId = fields[i++];
                company.getNationalIds().add(nationalId);
                company.setAddress(fields[i++]);
                company.setAddress2(fields[i++]);
                company.setZipCode(fields[i++]);
                company.setCity(fields[i++]);
                company.setCountry(fields[i++]);
                company.setHeadOffice(fields[i++].equals("1"));
                return company;
            }
        }, "UTF8");
        if (lastCompany.getId() != null) {
            String json = mapper.writeValueAsString(lastCompany);
            bulkRequest.add(client.prepareIndex("cboost", "company", lastCompany.getId()).setSource(json));
        }
        //Insert company.
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            for (BulkItemResponse item : bulkResponse.getItems()) {
                System.err.println(item.getFailureMessage());
            }
        }
    }
}

/*
select c.ID, c.NAME, bi.BUSINESS_ID, o.ADDRESS, o.ADDRESS2, o.ZIP_CODE, o.CITY_NAME, o.COUNTRY_CODE, c.HEAD_OFFICE from COMPANY c
       inner join COMPANY_BUSINESS_ID bi on bi.COMPANY_ID = c.ID
       inner join OFFICE o on o.COMPANY_ID = c.ID;

*/