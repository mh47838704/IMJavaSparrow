package loginserver.test;

import base.config.ConfigReader;
import base.config.ConfigUtil;
import org.apache.log4j.PropertyConfigurator;

/**
 * Created by Administrator on 2017/5/15 0015.
 */
public class Test1 {
    public static void main(String[] args) {
        Test1.initLog();
        System.out.println("配置文件测试");
        String fileName = "LoginServer.properties";
        String relativePath = "\\src\\loginserver\\";
        String fullPath = ConfigUtil.getAbsolateConfPath(relativePath, fileName);
        System.out.println(fullPath);
        ConfigReader configReader = new ConfigReader(fullPath);

        System.out.println(configReader.getString("ClientListenIP"));
    }

    public static void  initLog(){
        PropertyConfigurator.configure("config/log4j.properties");
    }

}
