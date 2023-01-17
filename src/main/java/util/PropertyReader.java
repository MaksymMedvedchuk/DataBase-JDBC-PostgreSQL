package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    public static final String PATH_TO_PROPERTIES = "application.properties";
    public static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();//загружаєьбся одразу з jvm і виконується 1 раз
    }

    public static void loadProperties() {
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(PATH_TO_PROPERTIES)) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getKey(String key) {
        return PROPERTIES.getProperty(key);
    }
}
