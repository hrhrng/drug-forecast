package com.hrhrng.drugforecast.Service;


import com.hrhrng.drugforecast.common.FileUtil;
import com.hrhrng.drugforecast.common.stub.CommandLineStub;
import com.hrhrng.drugforecast.openapi.CityscapeApi;
import com.hrhrng.drugforecast.openapi.EnrichrApi;
import com.hrhrng.drugforecast.openapi.GDCApi;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class DrugService {


    private final EmailService emailService;

    public DrugService(EmailService emailService) {
        this.emailService = emailService;
    }

    public List<String> queryDrugByDisease(String disease) throws IOException, InterruptedException {
        GDCApi api = new GDCApi();
        var tuple = api.getFilesMetaDate(disease);
        extraAndTransfer(disease);
        FileUtil.appendHead(disease);
        diffuseAnalysis(disease, tuple.get(1), tuple.get(0));
        CityscapeApi cityscapeApi = new CityscapeApi();
        var nodeList = cityscapeApi.mcodeCluster(disease);
        EnrichrApi enrichrApi = new EnrichrApi();
        String id = enrichrApi.createList(nodeList);

        var result = enrichrApi.getResult(id);
        // write the result to a file
        File file = new File("forest/data/result/disease.txt".replace("disease", disease));
        file.createNewFile();
        FileUtil.writeToFile(file, result);
        emailService.notifyEmail(disease);
        return result;

    }


    // 提取转化脚本
    public void extraAndTransfer(String disease) throws IOException, InterruptedException {
        // 执行脚本
        String randomFileName = FileUtil.getRandomFileName(disease);
        CommandLineStub.execWithUtil("Rscript " + "{dir}ETL.R " + disease + " " + randomFileName);

    }

    // 差异分析脚本
    public void diffuseAnalysis(String disease, int commonCount, int treatCount) throws IOException, InterruptedException {
        CommandLineStub.execWithUtil("Rscript " + "{dir}diffuseAnalysis.R " + disease + " " + commonCount
                + " " + treatCount);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
//        DrugService drugService = new DrugService(emailService);
////        drugService.extraAndTransfer("TCGA-BRCA");
////        FileUtil.appendHead("TCGA-BRCA");
//        drugService.diffuseAnalysis("TCGA-BRCA", 912, 88);
    }


}
