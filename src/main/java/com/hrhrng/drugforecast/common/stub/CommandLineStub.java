package com.hrhrng.drugforecast.common.stub;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommandLineStub {


    /**
     * 这个路径下存放脚本
     */
    static String baseUrl = "forest/util/";

    static Runtime runtime = Runtime.getRuntime();


    public static List<String> exec(String command) throws IOException, InterruptedException {
        Process process = runtime.exec(command);
        InputStream input = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String szline;
        List<String> ret = new ArrayList<>();
        while ((szline = reader.readLine()) != null) {
            ret.add(szline);
        }
        reader.close();
        process.waitFor();
        process.destroy();
        return ret;
    }

    public static List<String> execWithUtil(String command) throws IOException, InterruptedException {
        return exec(command.replace("{dir}",baseUrl));
    }
}




