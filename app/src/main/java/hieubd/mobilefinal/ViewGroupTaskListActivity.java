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
import hieubd.dao.UserDAO;
import hieubd.dto.GroupDTO;
import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.ui.GroupAdapter;

public class ViewGroupTaskListActivity extends AppCompatActivity {

    private ListView groupListView;
    private GroupAdapter adapter;
    private final int REQUEST_LIST_CODE = 9999;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group_task_list);
        groupListView = findViewById(R.id.listTaskGroup);
        Intent intent = this.getIntent();
        username = intent.getStringExtra("USERNAME");
        createListView();
    }

    private void createListView(){
        GroupDAO dao = new GroupDAO();
        UserDAO userDAO = new UserDAO();
        List<GroupDTO> groupList = null;
        UserDTO userDTO = userDAO.getUserByUsername(username);
        if (userDTO.getRole() == Role.Manager){
            groupList = dao.getAllGroupByManagerId(userDTO.getId());
        }else{
            List<Integer> tmp = dao.getAllGroupIdByUserId(userDTO.getId());
            if (tmp != null) {
                for (Integer id : tmp) {
                    GroupDTO tmpDto = dao.getGroupById(id);
                    groupList = new ArrayList<>();
                    groupList.add(tmpDto);
                }
            }
        }
        if (groupList != null){
            adapter = new GroupAdapter(groupList);
            groupListView.setAdapter(adapter);
            groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ViewGroupTaskListActivity.this, TaskListActivity.class);
                    GroupDTO dto = (GroupDTO) groupListView.getItemAtPosition(i);
                    intent.putExtra("DTO", dto);
                    intent.putExtra("USERNAME", username);
                    startActivityForResult(intent, REQUEST_LIST_CODE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE){
            if (resultCode == RESULT_OK){

            }
            GroupDAO dao = new GroupDAO();
            UserDAO userDAO = new UserDAO();
            List<GroupDTO> groupList = null;
            UserDTO userDTO = userDAO.getUserByUsername(username);
            if (userDTO.getRole() == Role.Manager){
                groupList = dao.getAllGroupByManagerId(userDTO.getId());
            }else{
                List<Integer> tmp = dao.getAllGroupIdByUserId(userDTO.getId());
                if (tmp != null) {
                    for (Integer id : tmp) {
                        GroupDTO tmpDto = dao.getGroupById(id);
                        groupList = new ArrayList<>();
                        groupList.add(tmpDto);
                    }
                }
            }
            if (groupList != null) {
                adapter.setDtoList(groupList);
                groupListView.setAdapter(adapter);

            }
        }
    }
}
