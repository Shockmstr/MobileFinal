package hieubd.mobilefinal.userUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import hieubd.dao.UserDAO;
import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.R;

public class NewUserActivity extends AppCompatActivity {
    private String selectedRole;
    private TextInputLayout til;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        til = findViewById(R.id.tilUsername);
        createSpinnerRole();
        createFocusListenerForUsername();
    }

    public void onClickCreateNewUser(View view) {
        String error = "";
        TextInputEditText edtNewUsername = findViewById(R.id.edtNewUsername);
        TextInputEditText edtNewPassword = findViewById(R.id.edtNewPassword);
        TextInputEditText edtNewFullname = findViewById(R.id.edtNewFullname);

       // TextView edtErrorUsername = findViewById(R.id.txtErrorUsername);
        UserDAO userDAO = new UserDAO();
        //validate
        if (edtNewUsername.getText().toString().isEmpty())error += "Username is empty\n";
        if (edtNewPassword.getText().toString().isEmpty())error += "Password is empty\n";
        if (edtNewFullname.getText().toString().isEmpty())error += "Fullname is empty\n";

        if (/*edtErrorUsername.getVisibility() == View.GONE &&*/ error.equals("")){
            String username = edtNewUsername.getText().toString();
            String password = edtNewPassword.getText().toString();
            String fullname = edtNewFullname.getText().toString();
            Role role = Role.valueOf(selectedRole);

            UserDTO newUserDto = new UserDTO(username, password, fullname, role);
            try {
                if (userDAO.createNewUser(newUserDto)){
                    Toast.makeText(getBaseContext(), "New user created", Toast.LENGTH_SHORT).show();
                    Intent intent = this.getIntent();
                    this.setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(this, "Failed to create user!", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "Error creating new user\n" + error, Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCancel(View view) {
        Intent intent = this.getIntent();
        this.setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void createFocusListenerForUsername(){
        TextInputEditText edtUsername = findViewById(R.id.edtNewUsername);
        edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus){
                    String username = ((EditText)view).getText().toString();
                    UserDAO userDAO = new UserDAO();
                    if (userDAO.checkUsernameIsExisted(username)){
                        /*TextView txtErrorUsername = findViewById(R.id.txtErrorUsername);
                        txtErrorUsername.setVisibility(View.VISIBLE);*/
                        til.setError("Username is already existed");
                    }else{
                        /*TextView txtErrorUsername = findViewById(R.id.txtErrorUsername);
                        txtErrorUsername.setVisibility(View.GONE);*/
                        til.setError(null);
                    }
                }
            }
        });
    }

    private void createSpinnerRole(){
        Spinner spnRole = findViewById(R.id.spnNewRole);
        List<String> roles = new ArrayList<>();
        roles.add("User");
        roles.add("Manager");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRole.setAdapter(adapter);
        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRole = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
