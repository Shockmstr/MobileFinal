package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hieubd.dao.UserDAO;
import hieubd.dto.Role;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void clickLogin(View view) {
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        UserDAO dao = new UserDAO();
        Role result = dao.checkLogin(username, password);
        if (result != Role.None){
            switch (result){
                case User:
                    Intent intent = new Intent(this, UserActivity.class);
                    intent.putExtra("USERNAME", edtUsername.getText().toString());
                    startActivity(intent);
                    break;
                case Manager:
                    Intent intent2 = new Intent(this, ManagerActivity.class);
                    intent2.putExtra("USERNAME", edtUsername.getText().toString());
                    startActivity(intent2);
                    break;
                case Admin:
                    Intent intent3 = new Intent(this, AdminActivity.class);
                    intent3.putExtra("USERNAME", edtUsername.getText().toString());
                    startActivity(intent3);
                    break;
                default:
                    break;
            }
        }else{
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show();
        }
    }

}
