package hieubd.mobilefinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import hieubd.dao.UserDAO;
import hieubd.dto.UserDTO;

public class UserActivity extends AppCompatActivity {
    private static int REQUEST_CODE = 6789;
    private String taskName, taskDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getWelcome();
    }

    private void getWelcome(){
        Intent intent = this.getIntent();
        String username = intent.getStringExtra("USERNAME");
        UserDAO dao = new UserDAO();
        UserDTO dto = dao.getUserByUsername(username);
        TextView txtWelcome = findViewById(R.id.txtWelcome);
        String welcome = txtWelcome.getText().toString();
        welcome += (" " + dto.getFullName());
        txtWelcome.setText(welcome);
    }

    public void onClickCreateTask(View view) {
        Intent intent = new Intent(this, TaskInfoActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                taskName = data.getStringExtra("taskname");
                taskDesc = data.getStringExtra("taskdesc");
            }
        }
    }

    public void onClickViewTask(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("taskname", taskName);
        intent.putExtra("taskdesc", taskDesc);
        startActivity(intent);
    }
}
