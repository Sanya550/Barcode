package com.example.barcode.helper;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GeneralHelper {

    public static boolean isPngFileExists(String directoryPath, String fileName) {
        String filePath = directoryPath + File.separator + fileName + ".png";
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static Image loadImageFromFile(String filePath) {
        try {
            return new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            System.err.println("Файл не знайдений: " + e.getMessage());
            return null;
        }
    }

    public static void deleteFilesInFolder(String folderPath) {
        folderPath = folderPath.replaceAll("\\\\+$", "");
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file.delete();
                }
            }
        }
    }
}
