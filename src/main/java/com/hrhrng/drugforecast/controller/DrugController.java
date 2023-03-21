package com.hrhrng.drugforecast.controller;

import com.hrhrng.drugforecast.Service.DrugService;
import com.hrhrng.drugforecast.Service.EmailService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/drug")
public class DrugController {

    private final DrugService drugService;
    private final EmailService emailService;
    public DrugController(DrugService drugService, EmailService emailService) {
        this.drugService = drugService;
        this.emailService = emailService;
    }


    @GetMapping(value = "/")
    public ResponseEntity queryDrugByDisease (String disease) throws IOException, InterruptedException, ExecutionException {
        HttpHeaders headers = new HttpHeaders();
        // write the result to a file
        File file = new File("data/result/{disease}.txt".replace("{disease}", disease));
        if (file.exists())
        {
            headers.setContentDispositionFormData("attachment", file.getName());
            headers.setContentType(MediaType.TEXT_EVENT_STREAM);
            try (InputStream in = new FileInputStream(file);) {
                byte[] bytes = new byte[in.available()];
                in.read(bytes);
                return ResponseEntity
                        .status(200)
                        .headers(headers)
                        .contentLength(file.length())
                        .body(bytes);
            } catch (IOException e) {
                // todo add log
                return null;
            }
        } else {
            CompletableFuture.runAsync(() -> {
                try {
                    drugService.queryDrugByDisease(disease);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            return ResponseEntity
                    .status(202).body("{status: \"accepted\")");

        }
    }

    @PostMapping("/")
    public ResponseEntity<Void> addDisease(@RequestParam("email") String email, @RequestParam("disease") String disease) {
        // 处理收到的email和disease参数的逻辑
        emailService.addNotify(email, disease);
        // 返回HTTP状态码200
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
