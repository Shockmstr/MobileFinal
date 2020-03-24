package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dao.UserDAO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.ui.TaskAdapter;

public class TaskAdminListActivity extends AppCompatActivity {
    private final int REQUEST_EDIT_CODE = 1111;
    private ListView taskListView;
    private TaskAdapter taskAdapter;
    private String adminUsername;
    List<PersonalTaskInfoDTO> infoDTOList = null;
    List<PersonalTaskTimeDTO> timeDTOList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_admin_list);
        Intent intent = this.getIntent();
        adminUsername = intent.getStringExtra("USERNAME");
        createListView();
    }

    private void createListView(){
        PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        UserDAO userDAO = new UserDAO();
        final UserDTO userDTO = userDAO.getUserByUsername(adminUsername); // dto is admin
        infoDTOList = infoDAO.getAllTaskNotAdmin();
        timeDTOList = timeDAO.getMultipleTaskById(infoDTOList);
        taskAdapter = new TaskAdapter(infoDTOList, timeDTOList);
        taskListView = findViewById(R.id.listAdminTask);
        taskListView.setAdapter(taskAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TaskAdminListActivity.this, TaskViewDetailActivity.class);
                PersonalTaskInfoDTO infoDTO = (PersonalTaskInfoDTO) taskAdapter.getItem(i);
                intent.putExtra("DTO", infoDTO);
                intent.putExtra("ROLE", userDTO.getRole());
                startActivityForResult(intent, REQUEST_EDIT_CODE);
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
                infoDTOList = infoDAO.getAllTaskNotAdmin();
                timeDTOList = timeDAO.getMultipleTaskById(infoDTOList);
                taskAdapter.setTaskInfoDTOList(infoDTOList);
                taskAdapter.setTaskTimeDTOList(timeDTOList);
                taskListView.setAdapter(taskAdapter);
            }
        }
    }
}
