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
                String sql = "Insert into PersonalTaskInfo values(?,?,?,?,?,?,?,?,?)";
                stm = conn.prepareStatement(sql);
                stm.setString(1, infoDTO.getName());
                stm.setString(2, infoDTO.getDescription());
                stm.setString(3, infoDTO.getHandlingContent());
                stm.setString(4, infoDTO.getStatus());
                stm.setString(5, infoDTO.getCreator());
                stm.setString(6, role.toString());
                stm.setString(7, infoDTO.getTaskHandler());
                stm.setString(8, infoDTO.getConfirmation());
                stm.setBytes(9, infoDTO.getConfirmationImage());
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
                String sql = "Select TaskID, TaskName, TaskDescription, TaskHandlingContent, Status, Creator, " +
                        "CreatorRole, TaskHandler, TaskConfirmation, ConfirmationImage from PersonalTaskInfo";
                stm = conn.prepareStatement(sql);
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("TaskID");
                    String name = rs.getString("TaskName");
                    String desc = rs.getString("TaskDescription");
                    String handlingContent = rs.getString("TaskHandlingContent");
                    String status = rs.getString("Status");
                    String creator = rs.getString("Creator");
                    String sRole = rs.getString("CreatorRole");
                    String handler = rs.getString("TaskHandler");
                    String confirm = rs.getString("TaskConfirmation");
                    byte[] image = rs.getBytes("ConfirmationImage");
                    PersonalTaskInfoDTO infoDTO = new PersonalTaskInfoDTO(name, desc, handlingContent, status, creator, handler, confirm, image);
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

    public List<PersonalTaskInfoDTO> getAllTasksHandlerByUsername(String username){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<PersonalTaskInfoDTO> result = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select TaskID, TaskName, TaskDescription, TaskHandlingContent, Status, Creator, " +
                        "CreatorRole, TaskConfirmation, ConfirmationImage from PersonalTaskInfo where TaskHandler=?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, username);
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("TaskID");
                    String name = rs.getString("TaskName");
                    String desc = rs.getString("TaskDescription");
                    String handlingContent = rs.getString("TaskHandlingContent");
                    String status = rs.getString("Status");
                    String creator = rs.getString("Creator");
                    String sRole = rs.getString("CreatorRole");
                    String handler = username;
                    String confirm = rs.getString("TaskConfirmation");
                    byte[] image = rs.getBytes("ConfirmationImage");
                    PersonalTaskInfoDTO infoDTO = new PersonalTaskInfoDTO(name, desc, handlingContent, status, creator, handler, confirm, image);
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

    public List<PersonalTaskInfoDTO> getAllTaskFromUserWithManagerId(int managerId){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<PersonalTaskInfoDTO> result = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select TaskID, TaskName, TaskDescription, TaskHandlingContent, Status, Creator, " +
                        "CreatorRole, TaskHandler, TaskConfirmation, ConfirmationImage FROM PersonalTaskInfo WHERE TaskHandler IN (SELECT Username FROM UserInfo WHERE ManagerID = ?)";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, managerId);
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("TaskID");
                    String name = rs.getString("TaskName");
                    String desc = rs.getString("TaskDescription");
                    String handlingContent = rs.getString("TaskHandlingContent");
                    String status = rs.getString("Status");
                    String creator = rs.getString("Creator");
                    String sRole = rs.getString("CreatorRole");
                    String handler = rs.getString("TaskHandler");;
                    String confirm = rs.getString("TaskConfirmation");
                    byte[] image = rs.getBytes("ConfirmationImage");
                    PersonalTaskInfoDTO infoDTO = new PersonalTaskInfoDTO(name, desc, handlingContent, status, creator, handler, confirm, image);
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

    public List<PersonalTaskInfoDTO> getAllTaskNotAdmin(){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<PersonalTaskInfoDTO> result = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select TaskID, TaskName, TaskDescription, TaskHandlingContent, Status, Creator, " +
                        "CreatorRole, TaskHandler, TaskConfirmation, ConfirmationImage FROM PersonalTaskInfo WHERE CreatorRole != ?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, "Admin");
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("TaskID");
                    String name = rs.getString("TaskName");
                    String desc = rs.getString("TaskDescription");
                    String handlingContent = rs.getString("TaskHandlingContent");
                    String status = rs.getString("Status");
                    String creator = rs.getString("Creator");
                    String sRole = rs.getString("CreatorRole");
                    String handler = rs.getString("TaskHandler");;
                    String confirm = rs.getString("TaskConfirmation");
                    byte[] image = rs.getBytes("ConfirmationImage");
                    PersonalTaskInfoDTO infoDTO = new PersonalTaskInfoDTO(name, desc, handlingContent, status, creator, handler, confirm, image);
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

    public PersonalTaskInfoDTO getTaskById(int id){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        PersonalTaskInfoDTO dto = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select TaskName, TaskDescription, TaskHandlingContent, Status, Creator, " +
                        "CreatorRole, TaskHandler, TaskConfirmation, ConfirmationImage from PersonalTaskInfo where TaskID=?";
                stm = conn.prepareStatement(sql);
                stm.setInt(1, id);
                rs = stm.executeQuery();
                if (rs.next()){
                    String name = rs.getString("TaskName");
                    String desc = rs.getString("TaskDescription");
                    String handlingContent = rs.getString("TaskHandlingContent");
                    String status = rs.getString("Status");
                    String creator = rs.getString("Creator");
                    String sRole = rs.getString("CreatorRole");
                    String handler = rs.getString("TaskHandler");
                    String confirm = rs.getString("TaskConfirmation");
                    byte[] image = rs.getBytes("ConfirmationImage");
                    dto = new PersonalTaskInfoDTO(name, desc, handlingContent, status, creator, handler, confirm, image);
                    dto.setId(id);
                    dto.setCreatorRole(Role.valueOf(sRole));
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn, stm, rs);
        }
        return dto;
    }

    public List<String> getAllTaskIdAndNameByTaskHandler(String username){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<String> result = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select TaskID, TaskName from PersonalTaskInfo where TaskHandler=?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, username);
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("TaskID");
                    String name = rs.getString("TaskName");
                    String str = id + " - " + name;
                    result.add(str);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public List<String> getAllTaskIdAndNameByTaskHandlerExcludeTaskID(String username, int taskId){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<String> result = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select TaskID, TaskName from PersonalTaskInfo where TaskHandler=? and TaskID != ?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, username);
                stm.setInt(2, taskId);
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("TaskID");
                    String name = rs.getString("TaskName");
                    String str = id + " - " + name;
                    result.add(str);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean updateTask(PersonalTaskInfoDTO infoDTO){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Update PersonalTaskInfo set TaskName=?, TaskDescription=?, TaskHandlingContent=?, Status=?," +
                        " Creator=?, TaskHandler=?, TaskConfirmation=?, ConfirmationImage=? where TaskID=?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, infoDTO.getName());
                stm.setString(2, infoDTO.getDescription());
                stm.setString(3, infoDTO.getHandlingContent());
                stm.setString(4, infoDTO.getStatus());
                stm.setString(5, infoDTO.getCreator());
                stm.setString(6, infoDTO.getTaskHandler());
                stm.setString(7, infoDTO.getConfirmation());
                stm.setBytes(8, infoDTO.getConfirmationImage());
                stm.setInt(9, infoDTO.getId());
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
                String sql = "Delete from PersonalTaskInfo where TaskID=?";
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

    public boolean deleteAllTaskByCreatorOrHandler(String creator){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Delete from PersonalTaskInfo where Creator=?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, creator);
                result = stm.executeUpdate() > 0;
                sql = "Update PersonalTaskInfo set TaskHandler=NULL where TaskHandler=?";
                stm = conn.prepareStatement(sql);
                //stm.setNull(1, Types.NVARCHAR);
                stm.setString(1, creator);
                result = stm.executeUpdate() > 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean updateAllTaskByCreatorOrHandlerBefore(String creator, List<Integer> taskIdList){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "update PersonalTaskInfo set Creator = NULL where TaskID = ? AND (Creator=?)\n";
                sql += "update PersonalTaskInfo set TaskHandler = NULL where TaskID = ? AND (TaskHandler=?)\n";
                stm = conn.prepareStatement(sql);
                //stm.setNull(1, Types.NVARCHAR);
                for (Integer id:
                     taskIdList) {
                    stm.setInt(1, id);
                    stm.setString(2, creator);
                    //stm.setNull(4, Types.NVARCHAR);
                    stm.setInt(3, id);
                    stm.setString(4, creator);
                    result = stm.executeUpdate() > 0;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public boolean updateAllTaskByCreatorOrHandlerAfter(String creator, List<Integer> taskIdList){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "update PersonalTaskInfo set Creator = ? where TaskID = ? AND (Creator IS NULL)\n";
                sql += "update PersonalTaskInfo set TaskHandler = ? where TaskID = ? AND (TaskHandler IS NULL)\n";
                stm = conn.prepareStatement(sql);
                for (Integer id: taskIdList)
                {
                    stm.setInt(2, id);
                    stm.setString(1, creator);
                    stm.setInt(4, id);
                    stm.setString(3, creator);
                    result = stm.executeUpdate() > 0;
                }

            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeConnection(conn, stm, rs);
        }
        return result;
    }

    public List<Integer> getAllTaskIDByCreatorOrHandler(String creator){
        Connection conn = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        List<Integer> result = null;
        try {
            conn = JDBCUtils.getMyConnection();
            if (conn != null){
                String sql = "Select TaskID from PersonalTaskInfo where TaskHandler=? OR Creator=?";
                stm = conn.prepareStatement(sql);
                stm.setString(1, creator);
                stm.setString(2, creator);
                rs = stm.executeQuery();
                while(rs.next()){
                    if (result == null){
                        result = new ArrayList<>();
                    }
                    int id = rs.getInt("TaskID");
                    result.add(id);
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
