package hieubd.mobilefinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

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
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
                case Admin:
                    break;
                case Manager:
                    break;
                default:
                    break;
            }
        }else{
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show();
        }
    }

}
