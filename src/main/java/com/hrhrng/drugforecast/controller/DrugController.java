package com.hrhrng.drugforecast.controller;

import com.hrhrng.drugforecast.Service.DrugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/drug")
public class DrugController {

    @Autowired
    DrugService drugService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<byte[]> queryDrugByDisease (String disease){
        File file = drugService.queryDrugByDisease(disease);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", file.getName());
        headers.setContentType(MediaType.TEXT_EVENT_STREAM);
        try (InputStream in = new FileInputStream(file);){
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .body(bytes);
        } catch (IOException e) {
            // todo add log
            return null;
        }
    }

}
