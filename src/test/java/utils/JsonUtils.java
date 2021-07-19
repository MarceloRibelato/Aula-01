package utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils {

    public JSONObject parseJSONFile(String json) throws IOException, JSONException {
        String content = new String(Files.readAllBytes(Paths.get("src/test/resources/jsons/" + json + ".json")));
        return new JSONObject(content);
    }

    public JSONObject updateJson(JSONObject obj, String keyMain, String newValue) throws JSONException {

        Iterator iterator = obj.keys();
        String key = null;
        while (iterator.hasNext()) {
            key = (String) iterator.next();

            if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
                if ((key.equals(keyMain))) {
                    obj.put(key, newValue);
                    return obj;
                }
            }

            if (obj.optJSONObject(key) != null) {
                updateJson(obj.getJSONObject(key), keyMain, newValue);
            }

            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                for (int i = 0; i < jArray.length(); i++) {
                    updateJson(jArray.getJSONObject(i), keyMain, newValue);
                }
            }
        }
        return obj;
    }

    public JSONObject setJsonValues(JSONObject json, Map<String, String> values) {
        JSONObject newJson = null;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            try {
                newJson = updateJson(json, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newJson;
    }

}
