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



    public void extraAndTransfer(String disease) throws IOException, InterruptedException {
        // 执行脚本
        String randomFileName = FileUtil.getRandomFileName(disease);
        CommandLineStub.execWithUtil("Rscript " + "{dir}ETL.R "+ disease + " " + randomFileName);

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        DrugService drugService = new DrugService();
        drugService.extraAndTransfer("TCGA-OV");
    }




}
