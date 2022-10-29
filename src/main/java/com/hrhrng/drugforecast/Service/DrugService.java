package com.hrhrng.drugforecast.Service;


import com.hrhrng.drugforecast.common.FileUtil;
import com.hrhrng.drugforecast.common.stub.CommandLineStub;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


@Service
public class DrugService {

    public File queryDrugByDisease(String disease) {

        return null;

    }



    // 提取转化脚本
    public void extraAndTransfer(String disease) throws IOException, InterruptedException {
        // 执行脚本
        String randomFileName = FileUtil.getRandomFileName(disease);
        CommandLineStub.execWithUtil("Rscript " + "{dir}ETL.R "+ disease + " " + randomFileName);

    }

    // 差异分析脚本
    public void diffuseAnalysis (String disease, int commonCount, int treatCount) throws IOException, InterruptedException {
        CommandLineStub.execWithUtil("Rscript " + "{dir}diffuseAnalysis.R " + disease + " " + commonCount
                + " " + treatCount);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        DrugService drugService = new DrugService();
//        drugService.extraAndTransfer("TCGA-BRCA");
//        FileUtil.appendHead("TCGA-BRCA");
        drugService.diffuseAnalysis("TCGA-BRCA", 912, 88);
    }




}
