package com.hrhrng.drugforecast.common.stub;

import java.io.File;
import java.io.IOException;

public class CreateFile {


    public static void main(String[] args) throws IOException {
        File file = new File("example.txt");
        file.createNewFile();
    }
}
