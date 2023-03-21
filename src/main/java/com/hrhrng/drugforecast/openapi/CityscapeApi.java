package com.hrhrng.drugforecast.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrhrng.drugforecast.common.FileUtil;
import com.hrhrng.drugforecast.common.jsonobject.cyto.CytoData;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CityscapeApi {


    RestTemplate restTemplate = new RestTemplate();

    ObjectMapper objectMapper = new ObjectMapper();

    String STRING_URl = "http://localhost:1234/v1/commands/string/protein query";

    String MCODE_URL = "http://localhost:1234/v1/commands/mcode/cluster";

    String NODE_URL = "http://localhost:1234/v1/commands/node/get attribute";


    public void buildNetwork(String disease) throws IOException {


        /**
         *
         * {
         *   "cutoff": "0.4",
         *   "includesViruses": "false",
         *   "limit": "100",
         *   "networkType": "full STRING network",
         *   "newNetName": "String Network",
         *   "query": "EGFR,BRCA1,BRCA2,TP53",
         *   "species": "homo sapiens",
         *   "taxonID": "9606"
         * }
         *
         */
        List<String> diffGene = FileUtil.getDiffGene(disease);

        StringBuffer sb = new StringBuffer();

        diffGene.forEach(i -> {
            sb.append(i);
            sb.append(',');
        });
        sb.deleteCharAt(sb.length() - 1);

        String body = "{\n" +
                "  \"cutoff\": \"0.4\",\n" +
                "  \"includesViruses\": \"false\",\n" +
                "  \"limit\": \"0\",\n" +
                "  \"networkType\": \"full STRING network\",\n" +
                "  \"newNetName\": "+ "\"" + disease + "\",\n" +
                "  \"query\": "+ "\"" + sb + "\",\n" +
                "  \"species\": \"homo sapiens\",\n" +
                "  \"taxonID\": \"9606\"\n" +
                "}";

        HttpHeaders headers = new HttpHeaders();//创建请求头对象
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);//将请求头传入请求体种
        ResponseEntity<String> res = restTemplate.exchange(STRING_URl, HttpMethod.POST, entity, String.class);
        System.out.println(res.getBody());
    }

    public List<String> mcodeCluster(String disease) {

        String body = "{\n" +
                "  \"degreeCutoff\": \"2\",\n" +
                "  \"fluff\": \"true\",\n" +
                "  \"fluffNodeDensityCutoff\": \"0.1\",\n" +
                "  \"haircut\": \"true\",\n" +
                "  \"includeLoops\": \"false\",\n" +
                "  \"kCore\": \"2\",\n" +
                "  \"maxDepthFromStart\": \"100\",\n" +
                "  \"network\": "+ "\"" + disease + "\",\n" +
                "  \"nodeScoreCutoff\": \"0.2\",\n" +
                "  \"scope\": \"NETWORK\"\n" +
                "}";
        //创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        //将请求头传入请求体种
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> res = restTemplate.exchange(MCODE_URL, HttpMethod.POST, entity, String.class);
        System.out.println(res.getBody());
        String r = res.getBody();
        // body 转化为 基因集合
        // 正则
        System.out.println(r);

        List<String> nodeLists = regEx("\\[[0-9,]+\\]", r).stream().map(i -> {
            String[] list = i.substring(1, i.length() - 1).split(",");
            return sGetNameById(Arrays.asList(list));
        }).flatMap(i -> i.stream()).collect(Collectors.toList());

        return nodeLists;
        // 热点基因
        //
    }


    public List<String> sGetNameById (List<String> ids) {
        StringBuffer sb = new StringBuffer();
        String raw = "{\n" +
                "  \"columnList\": \"name\",\n" +
                "  \"namespace\": \"default\",\n" +
                "  \"network\": \"current\",\n" +
                "  \"nodeList\": ";
        sb.append(raw);
        sb.append("\"");
        for (int i = 0; i < ids.size(); i++) {
            String j = ids.get(i);
            sb.append("suid:");
            sb.append(j);
            if (i != ids.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("\"\n}");
        HttpHeaders headers = new HttpHeaders();//创建请求头对象
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);//设置请求头，多个头则通过add一个一个添加
        HttpEntity<String> entity = new HttpEntity<String>(sb.toString(), headers);//将请求头传入请求体种
        CytoData result = restTemplate.postForObject(NODE_URL, entity, CytoData.class);

        return result.getData().stream().map( i -> i.getName())
                .collect(Collectors.toList());
    }
    private static List<String> regEx(String patten,String textArea) {
        String pattern = patten;
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(textArea);
        List<String> targetList = new ArrayList<String>();
        while (matcher.find()) {
            String substring = textArea.substring(matcher.start(), matcher.end());
            targetList.add(substring);
        }
        return targetList;
    }

    public static void main(String[] args) throws IOException {
        CityscapeApi cityscapeApi = new CityscapeApi();
        cityscapeApi.mcodeCluster("TCGA-BRCA");
    }
}
