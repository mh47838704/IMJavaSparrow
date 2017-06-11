package base.config;

import base.exception.ConfigException;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Administrator on 2017/5/15 0015.
 */
public class ConfigReader {
    private Map<String, Object> configMap = new HashMap<>();
    private static Logger logger = Logger.getLogger(ConfigReader.class);

    public ConfigReader(String filePath){
        if(filePath != null){
            logger.debug("正在解析配置文件："+filePath);
            this.read(filePath);
        }
    }

    public boolean read(String propFile) {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(propFile);
            if (in != null) {
                props.load(in);
            }else{
                logger.error("加载配置文件错误");
                return false;
            }
            // 解析properties文件
            Set<Map.Entry<Object, Object>> set = props.entrySet();
            Iterator<Map.Entry<Object, Object>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<Object, Object> entry = it.next();
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                String fuKey = getWildcard(value);

                if(null != fuKey && null != props.get(fuKey)){
                    String fuValue = props.get(fuKey).toString();
                    value = value.replaceAll("\\$\\{" + fuKey + "\\}", fuValue);
                }
                configMap.put(key, value);
            }
            return true;
        } catch (IOException e) {
            throw new ConfigException("load properties file error!");
        } finally {
            if(in != null){
                try{
                    in.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private String getWildcard(String str){
        if(null != str && str.indexOf("${") != -1){
            int start = str.indexOf("${");
            int end = str.indexOf("}");
            if(start != -1 && end != -1){
                return str.substring(start + 2, end);
            }
        }
        return null;
    }

    public String getString(String key) {
        Object object = configMap.get(key);
        if(null != object){
            return object.toString();
        }
        return null;
    }

    public Integer getInt(String key) {
        String value = this.getString(key);
        if(null != value){
            return Integer.parseInt(value);
        }
        return null;
    }

    public Long getLong(String key) {
        String value = this.getString(key);
        if(null != value){
            return Long.parseLong(value);
        }
        return null;
    }

    public Boolean getBoolean(String key) {
        String value = this.getString(key);
        if(null != value){
            return Boolean.parseBoolean(value);
        }
        return null;
    }

    public Double getDouble(String key) {
        String value = this.getString(key);
        if(null != value){
            return Double.parseDouble(value);
        }
        return null;
    }
}
