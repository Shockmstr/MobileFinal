package hieubd.mobilefinal.userUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import hieubd.dao.UserDAO;
import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.R;
import hieubd.mobilefinal.ui.UserAdapter;

public class UserListActivity extends AppCompatActivity {
    private final int REQUEST_EDIT_CODE = 1111;
    private GridView gridView;
    private UserAdapter userAdapter;
    List<UserDTO> userDTOList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        createGridView();
    }

    private void createGridView(){
        try{
            UserDAO userDAO = new UserDAO();
            List<UserDTO> tmp1 = userDAO.getAllUserByRole(Role.User);
            List<UserDTO> tmp2 = userDAO.getAllUserByRole(Role.Manager);
            userDTOList = new ArrayList<>();
            userDTOList.addAll(tmp1);
            userDTOList.addAll(tmp2);
            userAdapter = new UserAdapter(userDTOList);
            gridView = findViewById(R.id.gridUser);
            gridView.setAdapter(userAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(UserListActivity.this, UserListDetailActivity.class);
                    UserDTO userDTO = (UserDTO) userAdapter.getItem(i);
                    intent.putExtra("DTO", userDTO);
                    startActivityForResult(intent, REQUEST_EDIT_CODE);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_CODE){
            if (resultCode == RESULT_OK){
                UserDAO userDAO = new UserDAO();
                List<UserDTO> tmp1 = userDAO.getAllUserByRole(Role.User);
                List<UserDTO> tmp2 = userDAO.getAllUserByRole(Role.Manager);
                userDTOList.clear();
                userDTOList.addAll(tmp1);
                userDTOList.addAll(tmp2);
                userAdapter.setUserDTOList(userDTOList);
                gridView.setAdapter(userAdapter);
            }else if (resultCode == RESULT_CANCELED){
                // do nothing
            }
        }
    }
}
