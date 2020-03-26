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
        Intent intent = this.getIntent();
        username = intent.getStringExtra("USERNAME");
        getWelcome();
    }

    private void getWelcome(){
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

    public void onClickViewGroupTask(View view) {
        Intent intent = new Intent(this, ViewGroupTaskListActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    public void logout(View view) {
        this.setResult(RESULT_OK);
        finish();
    }

    public void onClickScanQR(View view) {
        Intent intent = new Intent(this, ScannerViewActivity.class);
        startActivity(intent);
    }
}
