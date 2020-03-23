package hieubd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.jdbc.JDBCUtils;

public class PersonalTaskTimeDAO {

    public PersonalTaskTimeDAO() {
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

    public boolean createNewTaskTime(PersonalTaskTimeDTO timeDTO){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Insert into PersonalTaskTime values(?,?,?,?)";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, timeDTO.getId());
                stm.setTimestamp(2, timeDTO.getTimeBegin());
                stm.setTimestamp(3, timeDTO.getTimeFinish());
                stm.setTimestamp(4, timeDTO.getTimeCreated());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public List<PersonalTaskTimeDTO> getAllTasks(){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<PersonalTaskTimeDTO> result = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select TaskID, TimeBegin, TimeFinish, TimeCreated from PersonalTaskTime";
                stm = conn.prepareStatement(sql);
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("TaskID");
                    Timestamp timeBegin = rs.getTimestamp("TimeBegin");
                    Timestamp timeFinished = rs.getTimestamp("TimeFinish");
                    Timestamp timeCreated = rs.getTimestamp("TimeCreated");
                    PersonalTaskTimeDTO timeDTO = new PersonalTaskTimeDTO(id, timeBegin, timeFinished, timeCreated);
                    result.add(timeDTO);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public PersonalTaskTimeDTO getTaskById(int id){
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        PersonalTaskTimeDTO timeDTO = null;
        try{
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "select TimeBegin, TimeFinish, TimeCreated from PersonalTaskTime where TaskID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if (rs.next()){
                    Timestamp timeBegin = rs.getTimestamp("TimeBegin");
                    Timestamp timeFinish = rs.getTimestamp("TimeFinish");
                    Timestamp timeCreated = rs.getTimestamp("TimeCreated");
                    timeDTO = new PersonalTaskTimeDTO(id, timeBegin, timeFinish, timeCreated);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stm, rs);
        }
        return timeDTO;
    }

    public List<PersonalTaskTimeDTO> getMultipleTaskById(List<PersonalTaskInfoDTO> infoDTOList){
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        List<PersonalTaskTimeDTO> timeDTOList = null;
        try{
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                if (infoDTOList != null){
                    for (PersonalTaskInfoDTO infoDTO: infoDTOList) {
                        int id = infoDTO.getId();
                        String sql = "select TaskID, TimeBegin, TimeFinish, TimeCreated from PersonalTaskTime where TaskID=?";
                        stm = conn.prepareStatement(sql);
                        stm.setInt(1, id);
                        rs = stm.executeQuery();
                        if (rs.next()){
                            if (timeDTOList == null) timeDTOList = new ArrayList<>();
                            Timestamp timeBegin = rs.getTimestamp("TimeBegin");
                            Timestamp timeFinish = rs.getTimestamp("TimeFinish");
                            Timestamp timeCreated = rs.getTimestamp("TimeCreated");
                            PersonalTaskTimeDTO timeDTO = new PersonalTaskTimeDTO(id, timeBegin, timeFinish, timeCreated);
                            timeDTOList.add(timeDTO);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn, stm, rs);
        }
        return timeDTOList;
    }

    public boolean updateTask(PersonalTaskTimeDTO timeDTO){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Update PersonalTaskTime set TimeBegin=?, TimeFinish=?, TimeCreated=? where TaskID=?";
                stm = conn.prepareStatement(sql);
                stm.setTimestamp(1, timeDTO.getTimeBegin());
                stm.setTimestamp(2, timeDTO.getTimeFinish());
                stm.setTimestamp(3, timeDTO.getTimeCreated());
                stm.setInt(4, timeDTO.getId());
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
                String sql = "Delete from PersonalTaskTime where TaskID=?";
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
