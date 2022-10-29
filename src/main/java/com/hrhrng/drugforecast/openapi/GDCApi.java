package com.hrhrng.drugforecast.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrhrng.drugforecast.common.FileUtil;
import com.hrhrng.drugforecast.common.jsonobject.Ids;
import com.hrhrng.drugforecast.common.jsonobject.JsonRootBean;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
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
        File file = new File("D:/forest/data/{disease}".replace("{disease}",disease));

        // 存在,直接返回结果,查询结果
        if (file.exists()) {
            return Optional.empty();
        }

        file.mkdir();

        File file1 = new File(file, "meta.json");


        file1.createNewFile();


        StringBuffer sb = new StringBuffer();
        sb.append(FILES_URL);
        sb.append("?");
        sb.append("filters={json}");
        sb.append(META_CONDITION);
        Map<String, String> map = new HashMap<>();
        map.put("json", buildFilter(disease));

        JsonRootBean result = restTemplate.getForObject(sb.toString(), JsonRootBean.class, map);

        List<String> idList = result.getData().getHits().stream().map(i->i.getId()).collect(Collectors.toList());

        int[] ddd = new int[2];

        // count tumor and normal
        result.getData().getHits().stream().forEach(i->{
            String submitter_id = i.getAssociated_entities().get(0).getEntity_submitter_id();
//            System.out.println(submitter_id);
            String[] ss = submitter_id.split("-");
            if (ss[3].startsWith("0")) ddd[0]++;                    // tumor
            else ddd[1]++;                                          // normal
        });

        String meta = objectMapper.writeValueAsString(result);

        System.out.println(ddd[0]+" "+ddd[1]);

        FileWriter fw = new FileWriter(file1);


        fw.write(meta);
        fw.close();
//
        downLoadFiles(idList, disease);

        // 移动文件
        FileUtil fileUtil = new FileUtil();
        fileUtil.moveFile(disease);

        //


        return Optional.empty();
    }

    void downLoadFiles (List<String> ids, String disease) throws IOException {

        // 下载至 ~/data/{disease}/data 中

        StringBuffer sb = new StringBuffer();
        sb.append(DATA_URl);

        Ids ids1 = new Ids(ids);
        // ids 构造 json
        String body = objectMapper.writeValueAsString(ids1);
        // 发起 POST 请求
        HttpHeaders headers = new HttpHeaders();//创建请求头对象
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);//设置请求头，多个头则通过add一个一个添加
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);//将请求头传入请求体种
        ResponseEntity<Resource> in = restTemplate.exchange(DATA_URl, HttpMethod.POST, entity, Resource.class);

        try (InputStream is = in.getBody().getInputStream();)
        {//java9新特性try升级 自动关闭流
            extractTarGZ(is, "D:/forest/data/disease/data/".replace("disease", disease));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 更加通用的解压方式
    public void extractTarGZ(InputStream in, String targetDir) throws IOException {
        // tar.gz -> tar
        try (
                GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(in);
                TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)
        ) {

            TarArchiveEntry entry;

            while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {

                if (entry.isDirectory()) {
                    continue;
                }

                File outputFile = new File(targetDir + entry.getName());

                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }

                try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    tarIn.transferTo(fos);
                }
            }
        }
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
        api.getFilesMetaDate("TCGA-BRCA");
    }

}
