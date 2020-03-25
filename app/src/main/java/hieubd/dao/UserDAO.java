package hieubd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.jdbc.JDBCUtils;

public class UserDAO {

    public UserDAO() {
    }

    private void closeConnection(Connection conn, PreparedStatement stm, ResultSet rs){
        try{
            if (rs != null) rs.close();
            if (stm != null) stm.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                String sql = "select Username, FullName, Role from UserInfo where UserID = ?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if (rs.next()){
                    String username = rs.getString("Username");
                    String fullName = rs.getString("FullName");
                    String role = rs.getString("Role");
                    UserDTO dto = new UserDTO();
                    dto.setUsername(username);
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

    public UserDTO getUserByUsername(String username){
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        try{
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "select UserID, FullName, Role from UserInfo where Username = ?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, username);
                rs = stm.executeQuery();
                if (rs.next()){
                    int id = rs.getInt("UserID");
                    String fullName = rs.getString("FullName");
                    String role = rs.getString("Role");
                    UserDTO dto = new UserDTO();
                    dto.setId(id);
                    dto.setFullName(fullName);
                    dto.setRole(Role.valueOf(role));
                    dto.setUsername(username);
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

    public List<UserDTO> getAllUserByRole(Role role){
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        List<UserDTO> result =  null;
        try{
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "select UserID, Username, FullName, Password from UserInfo where Role = ?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, role.toString());
                rs = stm.executeQuery();
                while (rs.next()){
                    if (result == null) result  = new ArrayList<>();
                    int id = rs.getInt("UserID");
                    String username = rs.getString("Username");
                    String password = rs.getString("Password");
                    String fullName = rs.getString("FullName");
                    UserDTO dto = new UserDTO(id, username, password, fullName, role);
                    result.add(dto);
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
        return result;
    }

    public List<UserDTO> getAllUserByGroupID(int groupId){
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        List<UserDTO> result =  null;
        try{
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "select UserID from GroupUser where GroupID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, groupId);
                rs = stm.executeQuery();
                while (rs.next()){
                    if (result == null) result  = new ArrayList<>();
                    int id = rs.getInt("UserID");
                    UserDTO dto = this.getUserById(id);
                    result.add(dto);
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
        return result;
    }

    public boolean checkUsernameIsExisted(String username){
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        try{
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "select UserID from UserInfo where Username = ?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, username);
                rs = stm.executeQuery();
                if (rs.next()) {
                    return true;
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
        return false;
    }

    public boolean createNewUser(UserDTO dto){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Insert into UserInfo values(?,?,?,?)";
                stm = conn.prepareStatement(sql);
                stm.setString(1, dto.getUsername());
                stm.setString(2, dto.getPassword());
                stm.setString(3, dto.getFullName());
                stm.setString(4, dto.getRole().toString());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                if (rs != null) rs.close();
                if (stm != null) stm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean updateUser(UserDTO dto){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Update UserInfo set Username=?, Password=?, Fullname=?, Role=? where UserID=?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, dto.getUsername());
                stm.setString(2, dto.getPassword());
                stm.setString(3, dto.getFullName());
                stm.setString(4, dto.getRole().toString());
                stm.setInt(5, dto.getId());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean deleteUser(int id){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Delete from UserInfo where UserID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }
}
