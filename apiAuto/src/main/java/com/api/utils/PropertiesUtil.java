package com.api.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 获取用例的路径
 */
public class PropertiesUtil {
    public static Properties properties = new Properties();
    public static String getExcelPath(){
        try {
            FileInputStream fileInputStream = new FileInputStream("D:\\javaApi\\apiAuto\\src\\main\\resources\\config.properties");
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("excel.path");
    }
}
