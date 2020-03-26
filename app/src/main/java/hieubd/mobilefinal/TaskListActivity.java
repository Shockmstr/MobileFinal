package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import hieubd.dao.GroupDAO;
import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dao.UserDAO;
import hieubd.dto.GroupDTO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.ui.TaskAdapter;

public class TaskListActivity extends AppCompatActivity {
    private final int REQUEST_EDIT_CODE = 1111;
    private final int REQUEST_EDIT_CODE_2 = 1235;
    private ListView taskListView;
    private TaskAdapter taskAdapter;
    private String username;
    private GroupDTO groupDTO;
    List<PersonalTaskInfoDTO> infoDTOList = null;
    List<PersonalTaskTimeDTO> timeDTOList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Intent intent = this.getIntent();
        username = intent.getStringExtra("USERNAME");
        groupDTO = (GroupDTO) intent.getSerializableExtra("DTO");
        taskListView = findViewById(R.id.listTask);
        if (groupDTO != null){
            createListViewWithGroup();
        }else{
            createListView();
        }
    }

    private void createListView(){
        PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        infoDTOList = infoDAO.getAllTasksHandlerByUsername(username);
        timeDTOList = timeDAO.getMultipleTaskById(infoDTOList);
        taskAdapter = new TaskAdapter(infoDTOList, timeDTOList);
        taskListView.setAdapter(taskAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TaskListActivity.this, TaskViewDetailActivity.class);
                PersonalTaskInfoDTO infoDTO = (PersonalTaskInfoDTO) taskAdapter.getItem(i);
                intent.putExtra("MANAGERFLAG", true);
                intent.putExtra("DTO", infoDTO);
                intent.putExtra("ROLE", infoDTO.getCreatorRole());
                startActivityForResult(intent, REQUEST_EDIT_CODE);
            }
        });
    }

    private void createListViewWithGroup(){
        PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        GroupDAO groupDAO = new GroupDAO();
        UserDAO userDAO = new UserDAO();
        UserDTO manager = userDAO.getUserById(groupDTO.getManagerId());
        UserDTO thisUser = userDAO.getUserByUsername(username);
        if (manager == null) manager = thisUser;
        if (thisUser.getRole() == Role.User){
            infoDTOList = infoDAO.getAllTasksHandlerByUsernameAndCreatorByManagerId(username, manager.getUsername());
        }else if (thisUser.getRole() == Role.Manager){
            List<Integer> tmp = groupDAO.getAllUserIdByGroupId(groupDTO.getId());
            infoDTOList = new ArrayList<>();
            if (tmp != null){
                for (Integer id: tmp
                     ) {
                    UserDTO userTMP = userDAO.getUserById(id);
                    List<PersonalTaskInfoDTO> groupTMP = infoDAO.getAllTasksHandlerByUsernameAndCreatorByManagerId(userTMP.getUsername(), manager.getUsername());
                    if (groupTMP != null){
                        infoDTOList.addAll(groupTMP);
                    }
                }
            }
            List<PersonalTaskInfoDTO> groupTMP2 = infoDAO.getAllTasksHandlerByUsernameAndCreatorByManagerId(username, manager.getUsername());
            if (groupTMP2 != null){
                infoDTOList.addAll(groupTMP2);
            }
        }
        timeDTOList = timeDAO.getMultipleTaskById(infoDTOList);
        taskAdapter = new TaskAdapter(infoDTOList, timeDTOList);
        taskListView.setAdapter(taskAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TaskListActivity.this, TaskViewDetailActivity.class);
                PersonalTaskInfoDTO infoDTO = (PersonalTaskInfoDTO) taskAdapter.getItem(i);
                intent.putExtra("MANAGERFLAG", true);
                intent.putExtra("DTO", infoDTO);
                intent.putExtra("ROLE", infoDTO.getCreatorRole());
                startActivityForResult(intent, REQUEST_EDIT_CODE_2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_CODE) {
            if (resultCode == RESULT_OK) {
                PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
                PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
                infoDTOList = infoDAO.getAllTasksHandlerByUsername(username);
                timeDTOList = timeDAO.getMultipleTaskById(infoDTOList);
                taskAdapter.setTaskInfoDTOList(infoDTOList);
                taskAdapter.setTaskTimeDTOList(timeDTOList);
                taskListView.setAdapter(taskAdapter);
            }
        }else if (requestCode == REQUEST_EDIT_CODE_2){
            if (resultCode == RESULT_OK){
                PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
                PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
                UserDAO userDAO = new UserDAO();
                UserDTO manager = userDAO.getUserById(groupDTO.getManagerId());
                infoDTOList = infoDAO.getAllTasksHandlerByUsernameAndCreatorByManagerId(username, manager.getUsername());
                timeDTOList = timeDAO.getMultipleTaskById(infoDTOList);
                taskAdapter.setTaskInfoDTOList(infoDTOList);
                taskAdapter.setTaskTimeDTOList(timeDTOList);
                taskListView.setAdapter(taskAdapter);
            }
        }
    }
}
