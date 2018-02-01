package tools.sqlRequest;

import org.testng.Reporter;

import java.sql.*;
import java.util.regex.Pattern;

/**
 * @author 李振7
 * Created Time: 2017/12/26 上午10:50
 */
public class DBHelper {
    private String DBurl = "jdbc:mysql://127.0.0.1/student";
    private String JdbcName = "com.mysql.jdbc.Driver";
    private String UserName = "root";
    private String PassWord = "root";
    private String sql = "";

    private Connection conn = null;
    private PreparedStatement pst = null;
    private Statement stmt = null;
    private boolean isSuccessful = false;

    public DBHelper(String DBurl, String JdbcName, String UserName, String PassWord, String sql) {
        this.DBurl = DBurl;
        this.JdbcName = JdbcName;
        this.UserName = UserName;
        this.PassWord = PassWord;
        this.sql = sql;

        // 1. 加载驱动程序
        try {
            Class.forName(JdbcName);
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        // 2. 获取DB连接
        try {
            conn = DriverManager.getConnection(DBurl, UserName, PassWord);
        }catch (SQLException e2) {
            System.out.println("数据库登录失败，数据库为：" + DBurl + "，用户名为：" + UserName + "，密码为：" + PassWord);
            Reporter.log("数据库登录失败，数据库为：" + DBurl + "，用户名为：" + UserName + "，密码为：" + PassWord);
            Reporter.log("数据路登录失败原因为：" +e2.getMessage());
            e2.printStackTrace();
        }

        // 3. 通过数据库的连接操作数据库，实现增删改查
        if (conn != null) {

            try {
                pst = conn.prepareStatement(sql);//准备执行语句
            } catch (SQLException e3) {
                e3.printStackTrace();
            }

            try {
                stmt = conn.createStatement();

                //  (?i)相当于 Pattern.CASE_INSENSITIVE,启用不区分大小写的匹配
                if(Pattern.compile("(?i)select.*").matcher(sql).find()) {
                    boolean sqlResult = pst.execute(sql);
                    if(sqlResult){
                        System.out.println("sql语句执行成功，sql语句为：" + sql);
                        isSuccessful = true;
                    }else {
                        Reporter.log("sql语句执行失败，sql语句为：" + sql);
                        System.out.println("sql语句执行失败，sql语句为：" + sql);
                        isSuccessful = false;
                    }
                } else if(Pattern.compile("(?i)update.*").matcher(sql).find()) {
                    int rowsAffected = stmt.executeUpdate(sql);
                    if(rowsAffected>0) {
                        System.out.println("sql语句执行成功，sql语句为：" + sql);
                        isSuccessful = true;
                    }else {
                        Reporter.log("sql语句执行失败，sql语句为：" + sql);
                        System.out.println("sql语句执行失败，sql语句为：" + sql);
                        isSuccessful = false;
                    }
                } else if (Pattern.compile("(?i)insert.*").matcher(sql).find()) {
                    int rowsAffected = stmt.executeUpdate(sql);
                    if(rowsAffected>0) {
                        System.out.println("sql语句执行成功，sql语句为：" + sql);
                        isSuccessful = true;
                    }else {
                        Reporter.log("sql语句执行失败，sql语句为：" + sql);
                        System.out.println("sql语句执行失败，sql语句为：" + sql);
                        isSuccessful = false;
                    }
                } else if (Pattern.compile("(?i)delete.*").matcher(sql).find()) {
                    int rowsAffected = stmt.executeUpdate(sql);
                    if(rowsAffected>0) {
                        System.out.println("sql语句执行成功，sql语句为：" + sql);
                        isSuccessful = true;
                    }else {
                        Reporter.log("sql语句执行失败，sql语句为：" + sql);
                        System.out.println("sql语句执行失败，sql语句为：" + sql);
                        isSuccessful = false;
                    }
                }
            } catch (SQLException e6) {
                Reporter.log("sql语句执行失败，sql语句为：" + sql);
                System.out.println("sql语句执行失败，sql语句为：" + sql);
                Reporter.log("sql执行失败原因：" + e6.getMessage());
                e6.printStackTrace();
            }

            close();

        } else {
            isSuccessful = false;
        }

    }

    public Boolean isRequestSuccessful() {
        if (isSuccessful) {
            return true;
        } else {
            return false;
        }
    }


        public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e5) {
            e5.printStackTrace();
        }
    }
}
