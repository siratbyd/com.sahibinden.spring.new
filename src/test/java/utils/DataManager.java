package utils;


import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class DataManager {


    // testler boyunca herhangi bir data kullanılmadı. Kullanıcı ismi, şifre, token vs

    private static Object document;

    private DataManager() {
    }

    public static String getData(String key) {
        if (document == null) {
            String jsonString;
            try {
                jsonString = FileUtils.readFileToString(new File("src/test/resources/data.json"), StandardCharsets.UTF_8);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
        }
        return JsonPath.read(document, key);
    }

}


