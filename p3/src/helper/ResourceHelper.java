package helper;

import java.io.InputStream;

public class ResourceHelper {
    public ResourceHelper() {}
    public InputStream getFileInputStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }
}
