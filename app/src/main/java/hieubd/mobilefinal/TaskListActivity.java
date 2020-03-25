package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.mobilefinal.ui.TaskAdapter;

public class TaskListActivity extends AppCompatActivity {
    private final int REQUEST_EDIT_CODE = 1111;
    private ListView taskListView;
    private TaskAdapter taskAdapter;
    private String username;
    List<PersonalTaskInfoDTO> infoDTOList = null;
    List<PersonalTaskTimeDTO> timeDTOList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Intent intent = this.getIntent();
        username = intent.getStringExtra("USERNAME");
        taskListView = findViewById(R.id.listTask);
        createListView();
    }

    private void createListView(){
        PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        infoDTOList = infoDAO.getAllTasksHandlerByUsername(username);
        timeDTOList = timeDAO.getMultipleTaskById(infoDTOList);
        taskAdapter = new TaskAdapter(infoDTOList, timeDTOList);
        TextView v = new TextView(this);
        v.setText("Your Tasks");
        taskListView.addHeaderView(v);
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
        }
    }
}
