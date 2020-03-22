package hieubd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
