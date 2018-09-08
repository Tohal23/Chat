package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String PROPERTIES_FILE_PATH = "src/config.properties";

    public static int PORT;
    public static int HISTORY_LENGHT;
    public static String HELLO_TEXT;
    public static int SUMM_ONLINE;
    public static String HASH_KEY;

    static {
        Properties properties = new Properties();
        FileInputStream propertiesFile = null;

        try {
            propertiesFile = new FileInputStream(PROPERTIES_FILE_PATH);

            properties.load(propertiesFile);

            PORT = Integer.parseInt(properties.getProperty("PORT"));
            HISTORY_LENGHT = Integer.parseInt(properties.getProperty("HISTORY_LENGHT"));
            HELLO_TEXT = properties.getProperty("HELLO_TEXT");
            SUMM_ONLINE = Integer.parseInt(properties.getProperty("SUMM_ONLINE"));
            HASH_KEY = properties.getProperty("HASH_KEY");
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error while reading file");
            e.printStackTrace();
        } finally {
            try {
                assert propertiesFile != null;
                propertiesFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
