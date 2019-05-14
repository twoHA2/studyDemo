package com.southintel.zaokin.base.util;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class ServiceUtil {
    public static Object getDataBykey(String properties ,String key) throws Exception {
        Properties prop = new Properties();
        String value = "";
        InputStream fis = null;
        try {
            fis = ServiceUtil.class.getResourceAsStream("/" + properties + ".properties");
            prop.load(fis);
            value = (String) prop.get(key);
            System.err.println("1:::"+value);

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("",e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e2) {
                throw new Exception("",e2);
            }
        }

        URL resource1 = Thread.currentThread().getContextClassLoader().getResource("");
        File file = new File(resource1.toString().substring(6) + File.separator + "xx.properties");

        InputStream is = new FileInputStream(file);
        Properties prop2 = new Properties();
        prop2.load(is);
        System.err.println("2:::"+prop2.getProperty(key));
        return value;
    }
}
