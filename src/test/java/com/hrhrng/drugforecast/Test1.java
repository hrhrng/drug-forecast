package com.hrhrng.drugforecast;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Test1 {


    @Test
    public Optional<Object> test1() throws IOException {
        String disease = "vvv";

        File file = new File("D:/forest/data/{disease}".replace("{disease}",disease));

        // 存在,直接返回结果,查询结果
        if (!file.exists()) {
            return Optional.empty();
        }

        file.mkdir();

        File file1 = new File(file, "meta.json");


        file1.createNewFile();


        return null;
    }

}
