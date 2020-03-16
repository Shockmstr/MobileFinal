package hieubd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.jdbc.JDBCController;

public class UserDAO {
    private JDBCController jdbcController;

    public UserDAO() {
        jdbcController = new JDBCController();
    }

    public UserDTO getUserById(int id){
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        try{
            conn = jdbcController.connectionData();
            if (conn != null){
                String sql = "select FullName, Role from [User] where UserID = ?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if (rs.next()){
                    String fullName = rs.getString("FullName");
                    String role = rs.getString("Role");
                    UserDTO dto = new UserDTO();
                    dto.setFullName(fullName);
                    dto.setRole(Role.valueOf(role));
                    dto.setId(id);
                    return dto;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if (rs != null) rs.close();
                if (stm != null) stm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
