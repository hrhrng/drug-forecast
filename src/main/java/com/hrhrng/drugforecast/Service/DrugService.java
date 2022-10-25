package com.hrhrng.drugforecast.Service;


import com.hrhrng.drugforecast.common.stub.CommandLineStub;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;


@Service
public class DrugService {

    public File queryDrugByDisease(String disease) {

        return null;

    }



    public void extraAndTransfer() throws IOException, InterruptedException {
        // 执行脚本
        CommandLineStub.execWithUtil("ETL.R -x");

    }

    public static void main(String[] args) {

    }




}
