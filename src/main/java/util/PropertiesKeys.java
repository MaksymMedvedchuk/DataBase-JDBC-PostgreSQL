package util;

public enum PropertiesKeys {

    PASSWORD("password"),
    USER_NAME("userName"),
    URL("url");

    private final String key;

    PropertiesKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
