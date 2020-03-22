package hieubd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.Role;
import hieubd.jdbc.JDBCUtils;

public class PersonalTaskInfoDAO {

    public PersonalTaskInfoDAO() {
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

    public boolean createNewTask(PersonalTaskInfoDTO infoDTO, Role role){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Insert into PersonalTaskInfo values(?,?,?,?,?,?,?,?,?,?)";
                stm = conn.prepareStatement(sql);
                stm.setString(1, infoDTO.getName());
                stm.setString(2, infoDTO.getSource());
                stm.setString(3, infoDTO.getDescription());
                stm.setString(4, infoDTO.getHandlingContent());
                stm.setString(5, infoDTO.getStatus());
                stm.setString(6, infoDTO.getCreator());
                stm.setString(7, role.toString());
                stm.setString(8, infoDTO.getTaskHandler());
                stm.setString(9, infoDTO.getConfirmation());
                stm.setBytes(10, infoDTO.getConfirmationImage());
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public int getNewTaskId(){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select MAX(TaskID) from PersonalTaskInfo";
                stm = conn.prepareStatement(sql);
                rs = stm.executeQuery();
                if (rs.next()){
                    int id = rs.getInt(1);
                    return id;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn, stm, rs);
        }
        return -1;
    }

    public List<PersonalTaskInfoDTO> getAllTasks(){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<PersonalTaskInfoDTO> result = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select TaskID, TaskName, Source, TaskDescription, TaskHandlingContent, Status, Creator, " +
                        "CreatorRole, TaskHandler, TaskConfirmation, ConfirmationImage from PersonalTaskInfo";
                stm = conn.prepareStatement(sql);
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("TaskID");
                    String name = rs.getString("TaskName");
                    String source = rs.getString("Source");
                    String desc = rs.getString("TaskDescription");
                    String handlingContent = rs.getString("TaskHandlingContent");
                    String status = rs.getString("Status");
                    String creator = rs.getString("Creator");
                    String sRole = rs.getString("CreatorRole");
                    String handler = rs.getString("TaskHandler");
                    String confirm = rs.getString("TaskConfirmation");
                    byte[] image = rs.getBytes("ConfirmationImage");
                    PersonalTaskInfoDTO infoDTO = new PersonalTaskInfoDTO(name, source, desc, handlingContent, status, creator, handler, confirm, image);
                    infoDTO.setId(id);
                    infoDTO.setCreatorRole(Role.valueOf(sRole));
                    result.add(infoDTO);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn, stm, rs);
        }
        return result;
    }
}
