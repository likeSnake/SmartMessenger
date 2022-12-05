package com.jcl.test2.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
    private static String diver = "com.mysql.jdbc.Driver";
    //加入utf-8是为了后面往表中输入中文，表中不会出现乱码的情况
    private static String url = "jdbc:mysql://47.109.49.142:3306/contactstest?useUnicode=true&characterEncoding=UTF-8";
    private static String user = "ContactsTest";//用户名
    private static String password = "2001Jiang";//密码
    /*
     * 连接数据库
     * */
    public static Connection getConn(){
        Connection conn = null;
        try {
            Class.forName(diver);
            conn = (Connection) DriverManager.getConnection(url,user,password);//获取连接
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    /**
     * 关闭数据库
     * */

    public static void closeAll(Connection conn, PreparedStatement ps){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 关闭数据库
     * */

    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
