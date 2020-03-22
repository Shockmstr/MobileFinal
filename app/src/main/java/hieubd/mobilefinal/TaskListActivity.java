package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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
    List<PersonalTaskInfoDTO> infoDTOList = null;
    List<PersonalTaskTimeDTO> timeDTOList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        createListView();
    }

    private void createListView(){
        PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        infoDTOList = infoDAO.getAllTasks();
        timeDTOList = timeDAO.getAllTasks();
        taskAdapter = new TaskAdapter(infoDTOList, timeDTOList);
        taskListView = findViewById(R.id.listTask);
        taskListView.setAdapter(taskAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TaskListActivity.this, TaskViewDetailActivity.class);

                startActivityForResult(intent, REQUEST_EDIT_CODE);
            }
        });
    }
}
