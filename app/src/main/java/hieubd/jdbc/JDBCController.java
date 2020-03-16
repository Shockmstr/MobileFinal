package hieubd.jdbc;

import java.sql.Connection;

public class JDBCController {
    JDBCModel jdbcModel = new JDBCModel();

    public Connection connectionData(){
        return jdbcModel.getMyConnection();
    }
}
