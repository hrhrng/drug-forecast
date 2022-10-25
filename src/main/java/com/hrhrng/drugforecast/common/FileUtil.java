package com.hrhrng.drugforecast.common;

import java.io.File;

public class FileUtil {



    public boolean deleteFile(File dirFile) {
        // 如果dir对应的文件不存在，则退出
        if (!dirFile.exists()) {
            return false;
        }

        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {

            for (File file : dirFile.listFiles()) {
                deleteFile(file);
            }
        }

        return dirFile.delete();
    }



    public void moveFile(String disease) {

        File dir = new File("D:/forest/data/disease/data".replace("disease", disease));

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName() == "MANIFEST.txt")
                continue;
            File[] subs = file.listFiles();
            subs[0].renameTo(new File("D:/forest/data/disease/allFiles/".replace("disease", disease)+subs[0].getName()));

        }
        deleteFile(dir);
        return;
    }
}
