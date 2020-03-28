package hieubd.mobilefinal.userUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.R;

public class QRScanDetailActivity extends AppCompatActivity {
    private UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan_detail);
        Intent intent = this.getIntent();
        userDTO = (UserDTO) intent.getSerializableExtra("DTO");
        autoFillDetails();
    }

    private void autoFillDetails() {
        String username = userDTO.getUsername();
        String fullname = userDTO.getFullName();
        Role role = userDTO.getRole();

        TextInputEditText txtUsername = findViewById(R.id.edtQRUsername);
        TextInputEditText txtFullname = findViewById(R.id.edtQRFullname);
        TextInputEditText txtRole = findViewById(R.id.edtQRRole);

        txtUsername.setText(username);
        txtFullname.setText(fullname);
        txtRole.setText(role.toString());

        txtUsername.setFocusable(false);
        txtFullname.setFocusable(false);
        txtRole.setFocusable(false);
    }
    public void onClickBack(View view) {
        Intent intent = this.getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
