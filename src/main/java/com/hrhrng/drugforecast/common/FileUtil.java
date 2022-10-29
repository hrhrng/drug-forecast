package com.hrhrng.drugforecast.common;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {


    public static String getRandomFileName(String disease) {
        File dir = new File("D:/forest/data/disease/allFiles/".replace("disease", disease));
        File[] files = dir.listFiles();
        return files[0].getName();
    }

    public static boolean deleteFile(File dirFile) {
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
        File dirNew = new File("D:/forest/data/disease/allFiles".replace("disease", disease));
        dirNew.mkdir();
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().equals("MANIFEST.txt")) {
                System.out.println(1);
                continue;
            }
            System.out.println(file.getName());
            File[] subs = file.listFiles();
            subs[0].renameTo(new File("D:/forest/data/disease/allFiles/".replace("disease", disease)+subs[0].getName()));

        }
        deleteFile(dir);
        return;
    }

    public static void main(String[] args) throws IOException {

//        File file = new File("D:\\forest\\data\\TCGA-BRCA\\diff_genes.txt");
//        FileReader fileReader = new FileReader(file);
//        BufferedReader bufferedReader = new BufferedReader(fileReader);
//        String x;
//        bufferedReader.readLine();
//        List<String> diffGenes = new ArrayList<>();
//        while ((x = bufferedReader.readLine()) != null) {
////            System.out.println(x);
//            String y = x.split("\t")[0];
//            diffGenes.add(y);
//            System.out.println(y);
//        }
    }

    public static void appendHead(String disease) throws IOException {
        File file = new File("D:/forest/data/disease/mRNAmatrix.txt".replace("disease", disease));


        FileWriter fileWriter = new FileWriter(file);
        String s = "GeneName\t";
        fileWriter.write(s, 0, s.length());
        fileWriter.flush();
        fileWriter.close();

        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

//        randomAccessFile.writeChars(s);

//        randomAccessFile.seek(randomAccessFile.length());

        FileChannel ch = randomAccessFile.getChannel();

        RandomAccessFile file2 = new RandomAccessFile("D:/forest/data/disease/mRNAmatrix_t.txt".replace("disease", disease), "rw");
        FileChannel channel2 = file2.getChannel();

        ch.transferFrom(channel2, file.length(), file2.length());
        ch.close();


        deleteFile(new File("D:/forest/data/disease/mRNAmatrix_t.txt".replace("disease", disease)));
    }

    public List<String> getDiffGene(String disease) throws IOException {
        File file = new File("D:\\forest\\data\\disease\\diff_genes.txt".replace("disease", disease));
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String x;
        bufferedReader.readLine();
        List<String> diffGenes = new ArrayList<>();
        while ((x = bufferedReader.readLine()) != null) {
            String y = x.split("\t")[0];
            diffGenes.add(y);
            System.out.println(y);
        }
        return diffGenes;
    }
}
