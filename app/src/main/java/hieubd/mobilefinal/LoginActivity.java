package hieubd.mobilefinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hieubd.dao.UserDAO;
import hieubd.dto.Role;

public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private final int REQ_LOGIN_CODE = 5050;

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
                    startActivityForResult(intent, REQ_LOGIN_CODE);
                    break;
                case Manager:
                    Intent intent2 = new Intent(this, ManagerActivity.class);
                    intent2.putExtra("USERNAME", edtUsername.getText().toString());
                    startActivityForResult(intent2, REQ_LOGIN_CODE);
                    break;
                case Admin:
                    Intent intent3 = new Intent(this, AdminActivity.class);
                    intent3.putExtra("USERNAME", edtUsername.getText().toString());
                    startActivityForResult(intent3, REQ_LOGIN_CODE);
                    break;
                default:
                    break;
            }
        }else{
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOGIN_CODE){
            if (resultCode == RESULT_OK){
                // Do nothing
            }
        }
    }
}
