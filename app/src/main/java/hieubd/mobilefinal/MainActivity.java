package hieubd.mobilefinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import hieubd.dao.UserDAO;
import hieubd.dto.UserDTO;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getInfo();
       // debugGetInfo();

    }


    private void getInfo(){
        EditText edtVTN = findViewById(R.id.edtViewTaskName);
        EditText edtVTD = findViewById(R.id.edtViewTaskDesc);
        Intent intent = this.getIntent();
        edtVTN.setText(intent.getStringExtra("taskname"));
        edtVTD.setText(intent.getStringExtra("taskdesc"));
    }

    private void debugGetInfo(){
        EditText edtFName = findViewById(R.id.edtViewTaskName);
        EditText edtRole = findViewById(R.id.edtViewTaskDesc);
        edtFName.setText("Ajax");
        edtRole.setText("user");
    }
}
