package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * @author javaok
 * 2023/6/21 10:01
 */
public class JdbcUtil {
    private final static String URL = "jdbc:mysql://localhost:3306/jav";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "123456789";
    private final static ThreadLocal<Connection> LOCAL_CONNECTION = new ThreadLocal<Connection>();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("加载驱动失败");
            throw new RuntimeException(e);
        }
    }


    public static Connection getConnection() {
        makeSureConnection();
        return LOCAL_CONNECTION.get();
    }


    private static void makeSureConnection() {
        if (LOCAL_CONNECTION.get() == null) {
            try {
                LOCAL_CONNECTION.set(DriverManager.getConnection(URL, USERNAME, PASSWORD));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public static void start() throws SQLException {
        makeSureConnection();
        LOCAL_CONNECTION.get().setAutoCommit(false);
    }

    public static void commit() throws SQLException {
        makeSureConnection();
        LOCAL_CONNECTION.get().commit();
        LOCAL_CONNECTION.get().close();
        LOCAL_CONNECTION.remove();
    }

    public static void rollback() throws SQLException {
        makeSureConnection();
        LOCAL_CONNECTION.get().rollback();
        LOCAL_CONNECTION.get().close();
        LOCAL_CONNECTION.remove();
    }
}
