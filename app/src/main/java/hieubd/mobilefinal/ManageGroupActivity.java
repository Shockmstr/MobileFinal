package hieubd.mobilefinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import hieubd.dao.GroupDAO;
import hieubd.dto.GroupDTO;
import hieubd.mobilefinal.ui.GroupAdapter;

public class ManageGroupActivity extends AppCompatActivity {
    private ListView groupListView;
    private GroupAdapter adapter;
    private final int REQUEST_LIST_CODE = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);
        groupListView = findViewById(R.id.listGroup);
        createListView();
    }



    private void createListView(){
        GroupDAO dao = new GroupDAO();
        List<GroupDTO> groupList = dao.getAllGroup();
        if (groupList != null){
            adapter = new GroupAdapter(groupList);
            groupListView.setAdapter(adapter);
            groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ManageGroupActivity.this, GroupDetailActivity.class);
                    GroupDTO dto = (GroupDTO) groupListView.getItemAtPosition(i);
                    intent.putExtra("DTO", dto);
                    startActivityForResult(intent, REQUEST_LIST_CODE);
                }
            });
        }
    }

    private void createNewGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create New Group");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GroupDAO groupDAO = new GroupDAO();
                GroupDTO groupDTO = new GroupDTO(input.getText().toString(), -1);
                if (groupDAO.createGroup(groupDTO)){
                    Toast.makeText(getBaseContext(), "Group created", Toast.LENGTH_SHORT).show();
                    GroupDAO dao = new GroupDAO();
                    List<GroupDTO> groupList = dao.getAllGroup();
                    if (groupList != null){
                        adapter.setDtoList(groupList);
                        groupListView.setAdapter(adapter);
                }else{
                    Toast.makeText(getBaseContext(), "Failed to create!", Toast.LENGTH_SHORT).show();
                }
            }
        }});

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do nothing
            }
        });
        builder.show();
    }

    public void onClickNewGroup(View view) {
        createNewGroup();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LIST_CODE){
            if (resultCode == RESULT_OK){

            }
            GroupDAO dao = new GroupDAO();
            List<GroupDTO> groupList = dao.getAllGroup();
            if (groupList != null) {
                adapter.setDtoList(groupList);
                groupListView.setAdapter(adapter);

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GroupDAO dao = new GroupDAO();
        List<GroupDTO> groupList = dao.getAllGroup();
        if (groupList != null) {
            adapter.setDtoList(groupList);
            groupListView.setAdapter(adapter);
        }
    }
}
