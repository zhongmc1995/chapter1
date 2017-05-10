package com.zmc.helper;

import com.zmc.utils.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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
    private final static BasicDataSource DATA_SOURCE = new BasicDataSource();
    //保证线程安全
    private final static ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();
    static {
        Properties properties = PropsUtil.loadProps("config.properties");
        DRIVER = properties.getProperty("jdbc.driver");
        URL = properties.getProperty("jdbc.url");
        USERNAME = properties.getProperty("jdbc.user");
        PASSWORD = properties.getProperty("jdbc.password");

        DATA_SOURCE.setDriverClassName(DRIVER);
        DATA_SOURCE.setUrl(URL);
        DATA_SOURCE.setUsername(USERNAME);
        DATA_SOURCE.setPassword(PASSWORD);
        /*try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            LOGGER.error("fail load jdbc driver", e);
        }*/
    }

    /**
     * 获取连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection == null) {
            try {
                //connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                connection = DATA_SOURCE.getConnection();
                CONNECTION_HOLDER.set(connection);
            } catch (SQLException e) {
                LOGGER.error("load jdbc driver failure", e);
            } finally {
            }
        }
        return connection;
    }

    /**
     * 关闭连接
     */
    public static void closeConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if (null != connection) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("close jdbc connection failure", e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 查询实体列表
     *
     * @param entityClass
     * @param sql
     * @param objs
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... objs) {
        Connection connection = getConnection();
        List<T> entityList = null;
        try {
            entityList = QUERY_RUNNER.query(connection, sql,
                    new BeanListHandler<T>(entityClass), objs);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
        } finally {
            closeConnection();
        }
        String paramsValues = "[ ";
        if (null!=objs){
            for (Object o : objs){
                paramsValues+=o+",";
            }
        }
        paramsValues = paramsValues.substring(0,paramsValues.length() - 1)+" ]";
        LOGGER.info("execute sql: "+sql+" params: "+ paramsValues);
        return entityList;
    }

    /**
     * 获取单个实体
     *
     * @param entityClass
     * @param sql
     * @param objs
     * @param <T>
     * @return
     */
    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... objs) {
        Connection connection = getConnection();
        T entity = null;
        try {
            entity = QUERY_RUNNER.query(connection, sql,
                    new BeanHandler<T>(entityClass), objs);
        } catch (SQLException e) {
            LOGGER.error("query entity list failure", e);
        } finally {
            closeConnection();
        }
        String paramsValues = "[ ";
        if (null!=objs){
            for (Object o : objs){
                paramsValues+=o+",";
            }
        }
        paramsValues = paramsValues.substring(0,paramsValues.length() - 1)+" ]";
        LOGGER.info("execute sql: "+sql+" params: "+ paramsValues);
        return entity;

    }

    /**
     * 执行语句 包括（update，insert，delete）
     *
     * @param sql
     * @param params
     * @return
     */
    public static int executeUpdate(String sql, Object... params) {
        int row = 0;
        Connection conn = getConnection();
        try {
            row = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            LOGGER.error("execute sql failure", e);
        } finally {
            closeConnection();
        }
        String paramsValues = "[ ";
        if (null!=params){
            for (Object o : params){
                paramsValues+=o+",";
            }
        }
        paramsValues = paramsValues.substring(0,paramsValues.length() - 1)+" ]";
        LOGGER.info("execute sql: "+sql+" params: "+ paramsValues);
        return row;
    }


    /**
     * 更新实体
     *
     * @param id
     * @param fieldMap
     * @return
     */
    public static boolean executeUpdate(Class<?> entityClass, long id, Map<String, Object> fieldMap) {
        if (fieldMap == null && fieldMap.size() == 0) {
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }
        int row = 0;
        Connection conn = getConnection();
        StringBuffer sql = new StringBuffer("update " + getTableName(entityClass) + " set ");
        Object[] params = new Object[fieldMap.size() + 1];
        int cusor = 0;
        for (Map.Entry entry : fieldMap.entrySet()) {
            params[cusor++] = entry.getValue();
            //sql+=entry.getKey()+"=?,";
            sql.append(entry.getKey()).append("=?,");
        }
        params[cusor] = id;
        String sqlStr = sql.substring(0, sql.length() - 1);
        sqlStr += " where id=?";
        row = executeUpdate(sqlStr, params);
        return row == 1;
    }

    /**
     * 插入实体
     *
     * @param entityClass
     * @param fieldMap
     * @return
     */
    public static boolean insertEntity(Class<?> entityClass, Map<String, Object> fieldMap) {
        if (fieldMap == null && fieldMap.size() == 0) {
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }
        String sql = "insert into " + getTableName(entityClass);
        StringBuffer columns = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");
        for (Map.Entry entry : fieldMap.entrySet()) {
            columns.append(entry.getKey()).append(",");
            values.append("?,");
        }
        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql += columns + " values " + values;
        return executeUpdate(sql, fieldMap.values().toArray()) == 1;
    }

    public static boolean deleteEntity(Class<?> entityClass, long id) {
        String sql = "delete from " + getTableName(entityClass) + " where id = ?";
        return executeUpdate(sql, id) == 1;
    }

    public static void executeSqlFile(String path){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(path)));
            String sql ;
            while ((sql = reader.readLine()) != null){
                DataBaseHelper.executeUpdate(sql);
            }
        } catch (Exception e) {
            LOGGER.error("execute sql failure",e);
            throw new RuntimeException(e);
        }finally {
            if (null != reader)
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("close reader failure",e);
                    throw new RuntimeException(e);
                }
        }
    }

    public static String getTableName(Class<? extends Object> entity) {
        return entity.getSimpleName();
    }
}
