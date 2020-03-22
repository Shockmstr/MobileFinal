package hieubd.jdbc;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtils {
    @SuppressLint("NewApi")
    public static Connection getMyConnection(){
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
        Connection conn = null;
        String url = null;
        JDBCObject jdbcObject = new JDBCObject("10.0.2.2", "sa", "1234",
                "MobileProject", "1111");
        try {
            Class.forName(jdbcObject.getsClass());
            url = "jdbc:jtds:sqlserver://"
                    + jdbcObject.getServerName() + ":" + jdbcObject.getPort()
                    + "/" + jdbcObject.getDatabase() + ";"
                    + "user=" + jdbcObject.getUserId() + ";"
                    + "password=" + jdbcObject.getPassword() + ";";
            conn = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static String fromBytesToString(byte[] imageBytes){
        Charset charset = StandardCharsets.UTF_16;
        String imagePath = new String(imageBytes, charset);
        return imagePath;
    }

    public static byte[] fromStringToBytes(String imagePath){
        Charset charset = StandardCharsets.UTF_16;
        return imagePath.getBytes(charset);
    }
}
