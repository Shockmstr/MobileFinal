package hieubd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import hieubd.dto.PersonalTaskManagerDTO;
import hieubd.jdbc.JDBCUtils;

public class PersonalTaskManagerDAO {

    public PersonalTaskManagerDAO() {
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

    public boolean createNewTaskManager(PersonalTaskManagerDTO managerDTO){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Insert into PersonalTaskManager values(?,?,?,?)";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, managerDTO.getId());
                stm.setString(2, managerDTO.getManagerComment());
                stm.setInt(3, managerDTO.getManagerMark());
                stm.setTimestamp(4, managerDTO.getManagerCommentBeginTime());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public PersonalTaskManagerDTO getTaskById(int id){
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        PersonalTaskManagerDTO managerDTO = null;
        try{
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "select ManagerComment, ManagerMark, ManagerCommentBeginTime from PersonalTaskManager where TaskID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if (rs.next()){
                    String comment = rs.getString("ManagerComment");
                    int mark = rs.getInt("ManagerMark");
                    Timestamp timeComment = rs.getTimestamp("ManagerCommentBeginTime");
                    managerDTO = new PersonalTaskManagerDTO(id, comment, mark, timeComment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stm, rs);
        }
        return managerDTO;
    }

    public boolean updateTask(PersonalTaskManagerDTO managerDTO){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Update PersonalTaskManager set ManagerComment=?, ManagerMark=?, ManagerCommentBeginTime=? where TaskID=?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, managerDTO.getManagerComment());
                stm.setInt(2, managerDTO.getManagerMark());
                stm.setTimestamp(3, managerDTO.getManagerCommentBeginTime());
                stm.setInt(4, managerDTO.getId());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean deleteTask(int id){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Delete from PersonalTaskManager where TaskID=?";
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
