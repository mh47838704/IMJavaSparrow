package base.config;

public class ConfigUtil {
    public static String getAbsolateConfPath(String relativePath, String configFileName){
        String projectPath = System.getProperty("user.dir");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(projectPath);
        stringBuilder.append(relativePath);
        stringBuilder.append(configFileName);

        return stringBuilder.toString();
    }
}
