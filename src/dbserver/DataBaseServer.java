package dbserver;

import base.config.ConfigReader;
import base.config.ConfigUtil;
import dbserver.dao.UserDao;
import dbserver.entity.UserInfoVo;
import loginserver.LoginServer;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/5/15 0015.
 */
public class DataBaseServer {

    public SqlSessionFactory sqlSessionFactory = null;
    private static Logger logger = Logger.getLogger(DataBaseServer.class);

    public DataBaseServer() {
        initLog();
        initSqlSessionFactory();
    }

    private void initSqlSessionFactory() {
        String resource = "dbserver/mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initLog(){
        PropertyConfigurator.configure("config/log4j.properties");
    }

    public static void main(String[] args) throws Exception {
        DataBaseServer dataBaseServer = new DataBaseServer();
        SqlSession session = dataBaseServer.sqlSessionFactory.openSession();
        try {
            // 获取数据库的dao实例
            UserDao userDao = session.getMapper(UserDao.class);
            UserInfoVo userInfoVo = (UserInfoVo) userDao.find("小米");
            logger.debug("数据库的用户信息："+userInfoVo.getUsername()+","+userInfoVo.getRealname());
        } finally {
            session.close();
        }
    }
}
