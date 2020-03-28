package hieubd.mobilefinal.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.mobilefinal.R;
import hieubd.mobilefinal.ui.TaskAdapter;

public class ViewTaskHistoryListActivity extends AppCompatActivity {
    private final int REQUEST_EDIT_CODE = 1111;
    private ListView taskListView;
    private TaskAdapter taskAdapter;
    private String username;
    List<PersonalTaskInfoDTO> infoDTOList = null;
    List<PersonalTaskTimeDTO> timeDTOList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_history_list);
        Intent intent = this.getIntent();
        username = intent.getStringExtra("USERNAME");
        taskListView = findViewById(R.id.listTaskHistory);
        createListView();
    }

    private void createListView(){
        PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        List<PersonalTaskInfoDTO> tmp = infoDAO.getAllTasksStatusFinished(username);
        timeDTOList = timeDAO.getMultipleTaskById(tmp);
        Collections.sort(timeDTOList);
        infoDTOList = new ArrayList<>();
        for (PersonalTaskTimeDTO timeDto : timeDTOList) {
            infoDTOList.add(infoDAO.getTaskById(timeDto.getId()));
        }
        taskAdapter = new TaskAdapter(infoDTOList, timeDTOList);
        taskListView.setAdapter(taskAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewTaskHistoryListActivity.this, ViewTaskHistoryDetailActivity.class);
                PersonalTaskInfoDTO infoDTO = (PersonalTaskInfoDTO) taskAdapter.getItem(i);
                intent.putExtra("DTO", infoDTO);
                intent.putExtra("ROLE", infoDTO.getCreatorRole());
                startActivityForResult(intent, REQUEST_EDIT_CODE);
            }
        });
    }
}
