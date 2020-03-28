package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import hieubd.dao.UserDAO;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.statistics.SearchTaskActivity;
import hieubd.mobilefinal.userUI.NewUserActivity;
import hieubd.mobilefinal.userUI.UserListActivity;

public class AdminActivity extends AppCompatActivity {
    private static int REQUEST_CREATE_CODE = 6789;
    private static int REQUEST_CREATE_USER_CODE = 1234;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getWelcome();
    }

    private void getWelcome(){
        Intent intent = this.getIntent();
        username = intent.getStringExtra("USERNAME");
        UserDAO dao = new UserDAO();
        UserDTO dto = dao.getUserByUsername(username);
        TextView txtWelcome = findViewById(R.id.txtAdminWelcome);
        String welcome = txtWelcome.getText().toString();
        welcome += dto.getFullName();
        txtWelcome.setText(welcome);
    }

    public void onClickCreateTask(View view) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        intent.putExtra("USERNAME", username);
        UserDAO dao = new UserDAO();
        UserDTO dto = dao.getUserByUsername(username);
        intent.putExtra("ROLE", (Serializable)dto.getRole());
        startActivityForResult(intent, REQUEST_CREATE_CODE);
    }

    public void onClickViewYourTask(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    public void onClickViewUserTask(View view) {
        Intent intent = new Intent(this, TaskAdminListActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    public void onClickCreateNewUser(View view) {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivityForResult(intent, REQUEST_CREATE_USER_CODE);
    }

    public void onClickEditUser(View view) {
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_USER_CODE){
            if (resultCode == RESULT_CANCELED);
            if (resultCode == RESULT_OK);
        }
    }

    public void onClickManageGroup(View view) {
        Intent intent = new Intent(this, ManageGroupActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        this.setResult(RESULT_OK);
        finish();
    }

    public void onClickSearchTask(View view) {
        Intent intent = new Intent(this, SearchTaskActivity.class);
        UserDAO dao = new UserDAO();
        UserDTO dto = dao.getUserByUsername(username);
        intent.putExtra("ROLE", (Serializable)dto.getRole());
        startActivity(intent);
    }
}
