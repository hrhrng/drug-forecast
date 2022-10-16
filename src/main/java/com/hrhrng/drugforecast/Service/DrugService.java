package com.hrhrng.drugforecast.Service;


import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class DrugService {

    public File queryDrugByDisease(String disease) {
        try {
            Thread.sleep(10000);
        } catch (Exception e) {

        }
        return new File("D:\\Downloads\\1658302385177.xls");
    }


}
