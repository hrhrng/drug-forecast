package com.hrhrng.drugforecast.common.stub;

import com.hrhrng.drugforecast.common.jsonobject.JsonRootBean;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HttpStub {

    RestTemplate restTemplate;

    public HttpStub() {
        restTemplate = new RestTemplate();
    }

    public List<String> queryTheIdList(String data) throws DocumentException {

        Map<String, String> map = new HashMap<>();
        map.put("json", "{\"op\":\"and\",\"content\":[{\"op\":\"in\",\"content\":{\"field\":\"cases.project.program.name\",\"value\":[\"TCGA\"]}},{\"op\":\"in\",\"content\":{\"field\":\"cases.project.project_id\",\"value\":[\"TCGA-OV\"]}},{\"op\":\"in\",\"content\":{\"field\":\"files.data_category\",\"value\":[\"transcriptome profiling\"]}},{\"op\":\"in\",\"content\":{\"field\":\"files.data_type\",\"value\":[\"Gene Expression Quantification\"]}}]}");
        JsonRootBean x = restTemplate.getForObject("https://api.gdc.cancer.gov/files?filters={json}&fields=file_id&format=JSON&size=1000" ,
                                                JsonRootBean.class,
                                                map);
        List<String> list = x.getData().getHits().stream().map(i->i.getId()).collect(Collectors.toList());

        return list;

    }

    public File queryTheMetadata() throws IOException {
        File file = new File("/metadata.json");
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        return file;
    }

    String makeBody() {
        String body = "https://api.gdc.cancer.gov/files?filters={%22op%22%3A%22and%22%2C%22content%22%3A[{%22op%22%3A%22in%22%2C%22content%22%3A{%22field%22%3A%22cases.project.program.name%22%2C%22value%22%3A[%22TCGA%22]}}%2C{%22op%22%3A%22in%22%2C%22content%22%3A{%22field%22%3A%22cases.project.project_id%22%2C%22value%22%3A[%22TCGA-OV%22]}}%2C{%22op%22%3A%22in%22%2C%22content%22%3A{%22field%22%3A%22files.data_category%22%2C%22value%22%3A[%22transcriptome%20profiling%22]}}%2C{%22op%22%3A%22in%22%2C%22content%22%3A{%22field%22%3A%22files.data_type%22%2C%22value%22%3A[%22Gene%20Expression%20Quantification%22]}}]}&fields=file_id&format=JSON&size=1000";
        return body;
    }

    public static void main(String[] args) throws DocumentException {
        HttpStub httpStub = new HttpStub();
        httpStub.queryTheIdList(httpStub.makeBody());
    }
}
