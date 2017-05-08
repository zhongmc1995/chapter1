package com.zmc.helper;

import com.zmc.utils.PropsUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Created by zhongmc on 2017/5/8.
 * 数据连接工具类
 */
public final class DataBaseHelper {
    private final static Logger LOGGER = LoggerFactory.getLogger(DataBaseHelper.class);
    private final static String DRIVER;
    private final static String URL;
    private final static String USERNAME;
    private final static String PASSWORD;
    private final static QueryRunner QUERY_RUNNER = new QueryRunner();
    //保证线程安全
    private final static ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

    static {
        Properties properties = PropsUtil.loadProps("config.properties");
        DRIVER = properties.getProperty("jdbc.driver");
        URL = properties.getProperty("jdbc.url");
        USERNAME = properties.getProperty("jdbc.user");
        PASSWORD = properties.getProperty("jdbc.password");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("fail load jdbc driver",e);
        }
    }

    /**
     * 获取连接
     * @return
     */
    public static Connection getConnection(){
        Connection connection = CONNECTION_HOLDER.get();
        if(connection==null){
            try {
                connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            } catch (SQLException e) {
                LOGGER.error("load jdbc driver failure",e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }

    /**
     * 关闭连接
     */
    public static void closeConnection(){
        Connection connection = CONNECTION_HOLDER.get();
        if (null!=connection){
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("close jdbc connection failure",e);
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体列表
     * @param entityClass
     * @param sql
     * @param objs
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass,String sql,Object ... objs){
        Connection connection = getConnection();
        List<T> entityList = null;
        try {
            entityList = QUERY_RUNNER.query(connection,sql,
                    new BeanListHandler<T>(entityClass),objs);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure",e);
        }finally {
            closeConnection();
        }
        return entityList;
    }

    /**
     * 获取单个实体
     * @param entityClass
     * @param sql
     * @param objs
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass,String sql,Object ... objs){
        Connection connection = getConnection();
        T entity = null;
        try {
            entity = QUERY_RUNNER.query(connection,sql,
                    new BeanHandler<T>(entityClass),objs);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure",e);
        }finally {
            closeConnection();
        }
        return entity;
    }
}
