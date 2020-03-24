package hieubd.mobilefinal.userUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import hieubd.dao.UserDAO;
import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.R;

public class NewUserActivity extends AppCompatActivity {
    private String selectedManagerUsername, selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        createSpinnerRole();
        createFocusListenerForUsername();
    }

    public void onClickCreateNewUser(View view) {
        EditText edtNewUsername = findViewById(R.id.edtNewUsername);
        EditText edtNewPassword = findViewById(R.id.edtNewPassword);
        EditText edtNewFullname = findViewById(R.id.edtNewFullname);
        TextView edtErrorUsername = findViewById(R.id.txtErrorUsername);
        UserDAO userDAO = new UserDAO();
        UserDTO managerDTO = userDAO.getUserByUsername(selectedManagerUsername);

        if (edtErrorUsername.getVisibility() == View.GONE){
            String username = edtNewUsername.getText().toString();
            String password = edtNewPassword.getText().toString();
            String fullname = edtNewFullname.getText().toString();
            Role role = Role.valueOf(selectedRole);
            int managerId = managerDTO.getId();

            UserDTO newUserDto = new UserDTO(username, password, fullname, role, managerId);
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
            Toast.makeText(this, "Error creating new user", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickCancel(View view) {
        Intent intent = this.getIntent();
        this.setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void createFocusListenerForUsername(){
        EditText edtUsername = findViewById(R.id.edtNewUsername);
        edtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus){
                    String username = ((EditText)view).getText().toString();
                    UserDAO userDAO = new UserDAO();
                    if (userDAO.checkUsernameIsExisted(username)){
                        TextView txtErrorUsername = findViewById(R.id.txtErrorUsername);
                        txtErrorUsername.setVisibility(View.VISIBLE);
                    }else{
                        TextView txtErrorUsername = findViewById(R.id.txtErrorUsername);
                        txtErrorUsername.setVisibility(View.GONE);
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
                if (selectedRole == "Manager"){
                    LinearLayout groupLayout = findViewById(R.id.groupLayout);
                    groupLayout.setVisibility(View.GONE);
                }else{
                    LinearLayout groupLayout = findViewById(R.id.groupLayout);
                    groupLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
