package hieubd.mobilefinal;

import androidx.appcompat.app.AppCompatActivity;

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
        EditText edtFName = findViewById(R.id.edtFName);
        EditText edtRole = findViewById(R.id.edtRole);
        UserDAO dao = new UserDAO();
        UserDTO dto = dao.getUserById(1);
        edtFName.setText(dto.getFullName());
        edtRole.setText(dto.getRole().name());
    }

    private void debugGetInfo(){
        EditText edtFName = findViewById(R.id.edtFName);
        EditText edtRole = findViewById(R.id.edtRole);
        edtFName.setText("Ajax");
        edtRole.setText("user");
    }
}
