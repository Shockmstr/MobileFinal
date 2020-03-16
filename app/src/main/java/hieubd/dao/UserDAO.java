package hieubd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.jdbc.JDBCController;
import hieubd.jdbc.JDBCUtils;

public class UserDAO {

    public UserDAO() {
    }

    public Role checkLogin(String username, String password){
        Role role = Role.None;
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select Role from UserInfo where Username = ? and Password = ?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, username);
                stm.setString(2, password);
                rs = stm.executeQuery();
                if (rs.next()){
                    String sRole = rs.getString("Role");
                    role = Role.valueOf(sRole);
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
        return role;
    }

    public UserDTO getUserById(int id){
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        try{
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "select FullName, Role from UserInfo where UserID = ?";
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
