package com.hrhrng.drugforecast.openapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrhrng.drugforecast.common.Enrichr.GetData;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnrichrApi {

    RestTemplate restTemplate = new RestTemplate();

    ObjectMapper objectMapper = new ObjectMapper();

    String ADD_URL = "https://maayanlab.cloud/Enrichr/addList";

    String RESULT_URL = "https://maayanlab.cloud/Enrichr/enrich?userListId={id}&backgroundType=DSigDB";


    public String createList(List<String> genes) throws JsonProcessingException {

        /**
         *
         * {
         *  "list" : "gene1\ngene2\n..."
         * }
         *
         */
        HttpHeaders headers = new HttpHeaders();//创建请求头对象
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
        LinkedMultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();;
        valueMap.add("list", String.join("\n",genes));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(valueMap, headers);
        ResponseEntity<String> res = restTemplate.exchange(ADD_URL, HttpMethod.POST, request, String.class);


        GetData data = objectMapper.readValue(res.getBody(), GetData.class);
        return data.getUserListId();


    }


    public List<String> getResult(String userListId) throws JsonProcessingException {


        String re = restTemplate.getForObject(RESULT_URL.replace("{id}", userListId),String.class);

        System.out.println(re);

        String[] xx = re.split("],\\[");

        List<String> med = new ArrayList<>();

        Arrays.stream(xx).forEach( i -> {
            med.add(i.split(",")[1]);
        });

        return med;
    }

    public static void main(String[] args) throws JsonProcessingException {
        EnrichrApi enrichrApi = new EnrichrApi();
        String[] ss = {"AMBP", "CHGA", "ORM1", "ORM2", "CDK5R2", "PMEL", "VGF"};
        String id = enrichrApi.createList(Arrays.asList(ss));
        enrichrApi.getResult(id);

    }

}
