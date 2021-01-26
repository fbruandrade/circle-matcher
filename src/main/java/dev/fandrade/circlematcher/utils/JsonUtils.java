package dev.fandrade.circlematcher.utils;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JsonUtils {

    public static void check(List<String> list, String key, JsonElement jsonElement) {

        if (jsonElement.isJsonArray()) {
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
                check(list, key, jsonElement1);
            }
        } else {
            if (jsonElement.isJsonObject()) {
                Set<Map.Entry<String, JsonElement>> entrySet = jsonElement
                        .getAsJsonObject().entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet) {
                    String key1 = entry.getKey();
                    if (key1.equals(key)) {
                        list.add(entry.getValue().toString());
                    }
                    check(list, key, entry.getValue());
                }
            } else {
                if (jsonElement.toString().equals(key)) {
                    list.add(jsonElement.toString());
                }
            }
        }
    }
}
