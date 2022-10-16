package com.hrhrng.drugforecast.openapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrhrng.drugforecast.common.jsonobject.Ids;
import com.hrhrng.drugforecast.common.jsonobject.JsonRootBean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GDCApi {

//    @Autowired
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();


    /**
     * 用来下载文件
     * Post方法
     * {
     *     "ids":[
     *         "UUID1",
     *         "UUID2",
     *                 ...
     *         "UUID3"
     *     ]
     * }
     */
    String DATA_URl = "https://api.gdc.cancer.gov/data";


    /**
     * 用来获取文件元数据
     * 包括：
     *  1. file_id
     *  2. file_name
     *  3. associated_entities.entity_submitter_id
     */
    String FILES_URL = "https://api.gdc.cancer.gov/files";

    String META_CONDITION = "&fields=associated_entities.entity_submitter_id,file_id,file_name&format=JSON&size=1000";
    String FILTER_BASE = "{\"op\":\"and\",\"content\":[{\"op\":\"in\",\"content\":{\"field\":\"cases.project.program.name\",\"value\":[\"TCGA\"]}},{\"op\":\"in\",\"content\":{\"field\":\"cases.project.project_id\",\"value\":[\"disease\"]}},{\"op\":\"in\",\"content\":{\"field\":\"files.data_category\",\"value\":[\"transcriptome profiling\"]}},{\"op\":\"in\",\"content\":{\"field\":\"files.data_type\",\"value\":[\"Gene Expression Quantification\"]}}]}";
    /**
     *
     * @param disease 疾病
     * @return list of id
     */
    Optional<List<String>> getFilesMetaDate (String disease) throws IOException {

        /** 调用 HTTP Client 的 get ，获取元数据
         *  方案1：存入对象
         *  方案2：写入文件中，供脚本
         */
//        File file = new File("./data/{disease}/meta.json".replace("{disease}",disease));
//
//        // 存在,直接返回结果
//        if (file.exists()) {
//            return Optional.empty();
//        }
//
//        file.createNewFile();

        StringBuffer sb = new StringBuffer();
        sb.append(FILES_URL);
        sb.append("?");
        sb.append("filters={json}");
        sb.append(META_CONDITION);
        Map<String, String> map = new HashMap<>();
        map.put("json", buildFilter(disease));

        JsonRootBean result = restTemplate.getForObject(sb.toString(), JsonRootBean.class, map);

        List<String> idList = result.getData().getHits().stream().map(i->i.getId()).collect(Collectors.toList());


        String meta = objectMapper.writeValueAsString(result);

//        FileWriter fw = new FileWriter("./data/{disease}/meta.json");

        downLoadFiles(idList);
//        fw.write(meta);

        return Optional.of(idList);
    }

    void downLoadFiles (List<String> ids) throws JsonProcessingException {

        // 下载至 ~/data/{disease}/data 中

        StringBuffer sb = new StringBuffer();
        sb.append(DATA_URl);

        Ids ids1 = new Ids(ids);
        // ids 构造 json
        String body = objectMapper.writeValueAsString(ids1);
        // 发起 POST 请求

        // 下载文件

    }

    /**
     * 构造疾病的Filter
     * @param disease
     * @return
     */
    String buildFilter(String disease) {
        String ret = FILTER_BASE.replace("disease",disease);
        return ret;
    }

    public static void main(String[] args) throws IOException {
        GDCApi api = new GDCApi();
        api.getFilesMetaDate("TCGA-OV");
    }

}
