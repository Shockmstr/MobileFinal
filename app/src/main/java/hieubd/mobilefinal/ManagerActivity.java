package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import hieubd.dao.UserDAO;
import hieubd.dto.UserDTO;

public class ManagerActivity extends AppCompatActivity {
    private static int REQUEST_CREATE_CODE = 6789;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        getWelcome();
    }

    private void getWelcome(){
        Intent intent = this.getIntent();
        username = intent.getStringExtra("USERNAME");
        UserDAO dao = new UserDAO();
        UserDTO dto = dao.getUserByUsername(username);
        TextView txtWelcome = findViewById(R.id.txtManagerWelcome);
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
        Intent intent = new Intent(this, TaskManagerListActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }
}
