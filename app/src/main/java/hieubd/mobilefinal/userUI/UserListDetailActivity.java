package hieubd.mobilefinal.userUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.UserDAO;
import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.R;

public class UserListDetailActivity extends AppCompatActivity {
    private UserDTO userDTO;
    private String selectedRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_detail);
        Intent intent = this.getIntent();
        userDTO = (UserDTO) intent.getSerializableExtra("DTO");
        createSpinnerRole();
        autoFillDetails();
    }

    private void autoFillDetails() {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        String fullname = userDTO.getFullName();
        Role role = userDTO.getRole();

        TextInputEditText txtUsername = findViewById(R.id.edtEditUsername);
        TextInputEditText txtPassword = findViewById(R.id.edtEditPassword);
        TextInputEditText txtFullname = findViewById(R.id.edtEditFullname);
        Spinner spnRole = findViewById(R.id.spnEditRole);

        txtUsername.setText(username);
        txtPassword.setText(password);
        txtFullname.setText(fullname);
        spnRole.setSelection(getIndexOfValueFromSpinner(spnRole, role.toString()));
    }

    private int getIndexOfValueFromSpinner(Spinner spn, String str){
        for (int i = 0; i < spn.getCount(); i++) {
            if (spn.getItemAtPosition(i).toString().equalsIgnoreCase(str)){
                return i;
            }
        }
        return 0;
    }

    private void createSpinnerRole(){
        Spinner spnRole = findViewById(R.id.spnEditRole);
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

    public void onClickSubmitEditUser(View view) {
        TextInputEditText txtUsername = findViewById(R.id.edtEditUsername);
        TextInputEditText txtPassword = findViewById(R.id.edtEditPassword);
        TextInputEditText txtFullname = findViewById(R.id.edtEditFullname);
        Spinner spnRole = findViewById(R.id.spnEditRole);
        UserDAO userDAO = new UserDAO();

        String error = "";
        if (userDAO.checkUsernameIsExisted(txtUsername.getText().toString())) error += "Username is existed!\n";
        if (txtUsername.getText().toString().isEmpty()) error += "Username is empty\n";
        if (txtPassword.getText().toString().isEmpty()) error += "Password is empty\n";
        if (txtFullname.getText().toString().isEmpty()) error += "Fullname is empty\n";

        if (error.equals("")){
            int id = userDTO.getId();
            String username = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            String fullname = txtFullname.getText().toString();
            String role = selectedRole;

            UserDTO newUser = new UserDTO(id, username, password, fullname, Role.valueOf(role));

            PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
            try{
                List<Integer> taskIdList = infoDAO.getAllTaskIDByCreatorOrHandler(userDTO.getUsername());
                infoDAO.updateAllTaskByCreatorOrHandlerBefore(userDTO.getUsername(), taskIdList);
                if (userDAO.updateUser(newUser)){
                    Toast.makeText(getBaseContext(), "Update user success", Toast.LENGTH_SHORT).show();
                    infoDAO.updateAllTaskByCreatorOrHandlerAfter(newUser.getUsername(), taskIdList);
                    Intent intent = this.getIntent();
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    Toast.makeText(this, "Update error!", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, error, Toast.LENGTH_SHORT ).show();
        }
    }

    public void onClickDeleteUser(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirmation");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Are you sure you want to delete this user?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try{
                    UserDAO userDAO = new UserDAO();
                    int id = userDTO.getId();
                    PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
                    infoDAO.deleteAllTaskByCreatorOrHandler(userDTO.getUsername());
                    if (userDAO.deleteUser(id)){
                        Toast.makeText(getBaseContext(), "User is deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = UserListDetailActivity.this.getIntent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do Nothing
            }
        });
        alertDialog.show();
    }

    public void onClickCancel(View view) {
        Intent intent = this.getIntent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
