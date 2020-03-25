package hieubd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hieubd.dto.GroupDTO;
import hieubd.dto.UserDTO;
import hieubd.jdbc.JDBCUtils;

public class GroupDAO {

    public GroupDAO() {
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

    public boolean createGroup(GroupDTO dto){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Insert into GroupInfo values(?,?)";
                stm = conn.prepareStatement(sql);
                stm.setString(1, dto.getName());
                stm.setInt(2, dto.getManagerId());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean createUserInGroup(GroupDTO dto, UserDTO userDTO){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Insert into GroupUser values(?,?)";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, dto.getId());
                stm.setInt(2, userDTO.getId());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public List<GroupDTO> getAllGroup(){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<GroupDTO> result = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select GroupID, GroupName, ManagerID from GroupInfo";
                stm = conn.prepareStatement(sql);
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("GroupID");
                    String name = rs.getString("GroupName");
                    int managerId = rs.getInt("ManagerID");
                    GroupDTO dto = new GroupDTO(id, name, managerId);
                    result.add(dto);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public GroupDTO getGroupById(int id){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        GroupDTO dto = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select GroupName, ManagerID from GroupInfo where GroupID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if(rs.next()){
                    String name = rs.getString("GroupName");
                    int managerId = rs.getInt("ManagerID");
                    dto = new GroupDTO(id, name, managerId);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn, stm, rs);
        }
        return dto;
    }

    public boolean updateGroup(GroupDTO dto){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Update GroupInfo set GroupName=?, ManagerID=? where GroupID=?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, dto.getName());
                stm.setInt(2, dto.getManagerId());
                stm.setInt(3, dto.getId());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean deleteGroup(int id){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Delete from GroupInfo where GroupID=?";
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

    public boolean deleteManagerFromGroup(GroupDTO dto){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Update GroupInfo set ManagerID=? where GroupID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, -1);
                stm.setInt(2, dto.getId());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean deleteUserFromGroup(int userId){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Delete from GroupUser where UserID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, userId);
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean isManagerExistedInGroup(GroupDTO dto){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = true;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select ManagerID from GroupInfo where GroupID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, dto.getId());
                rs = stm.executeQuery();
                if (rs.next()){
                    int managerId = rs.getInt("ManagerID");
                    if (managerId < 0){
                        result = false;
                    }else{
                        result = true;
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean isUserExistedInGroup(GroupDTO dto, int userId){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select UserID from GroupUser where GroupID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, dto.getId());
                rs = stm.executeQuery();
                if (rs.next()){
                    int idUser = rs.getInt("UserID");
                    if (userId == idUser){
                        result = true;
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }


}
