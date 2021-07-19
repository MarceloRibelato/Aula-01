package utils;

public class LogUtils {

    public void logInfo(String value) {
        if (!value.contains("{}")) {
            ScenarioUtils.addText(value);
        }
    }


    public void logError(String value) {
        if (!value.contains("{}")) {
            ScenarioUtils.addText(value);
        }
    }
}
