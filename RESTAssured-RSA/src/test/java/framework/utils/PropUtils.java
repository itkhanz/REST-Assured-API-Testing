package framework.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropUtils {
    public static String getGlobalValue(String key) throws IOException {
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\global.properties");
        prop.load(fis);
        return prop.getProperty(key);
    }
}
