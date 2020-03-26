package hieubd.mobilefinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.Serializable;

import hieubd.dao.UserDAO;
import hieubd.dto.UserDTO;
import hieubd.util.QRCodeHelper;

public class UserActivity extends AppCompatActivity {
    private static int REQUEST_CREATE_CODE = 6789;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getWelcome();
    }

    private void getWelcome(){
        Intent intent = this.getIntent();
        username = intent.getStringExtra("USERNAME");
        UserDAO dao = new UserDAO();
        UserDTO dto = dao.getUserByUsername(username);
        TextView txtWelcome = findViewById(R.id.txtWelcome);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_CODE){
            if (resultCode == RESULT_OK){

            }
        }
    }

    public void onClickViewTask(View view) {
        Intent intent = new Intent(this, ViewGroupTaskListActivity.class);
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    public void logout(View view) {
        this.setResult(RESULT_OK);
        finish();
    }

    public void onClickNewQRCode(View view) {
        try {
            UserDAO userDAO = new UserDAO();
            UserDTO user = userDAO.getUserByUsername(username);
            String userSerialized = new Gson().toJson(user);
            Bitmap bitmap = QRCodeHelper.newInstance(this)
                            .setContent(userSerialized)
                            .setErrorCorrectionLevel(ErrorCorrectionLevel.Q)
                            .setMargin(2)
                            .getQRCOde();

            ImageView img = findViewById(R.id.imgQR);
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
