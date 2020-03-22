package hieubd.mobilefinal;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.PersonalTaskManagerDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskManagerDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.dto.Role;

public class TaskInfoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private int getDateNumberFlag = -1;
    private String selectedStatus, selectedSource, selectedConfirm;
    private Role userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        createSpinnerStatus();
        createSpinnerConfirmation();
        autoFillCreatorAndWorkHandler();
        filterRoleForTask();
    }

    private void filterRoleForTask(){
        Intent intent = this.getIntent();
        userRole = (Role) intent.getSerializableExtra("ROLE");
    }

    private void autoFillCreatorAndWorkHandler(){
        Intent intent = this.getIntent();
        String username = intent.getStringExtra("USERNAME");
        EditText edtCreator = findViewById(R.id.edtCreator);
        edtCreator.setText(username);
        EditText edtTaskHandler = findViewById(R.id.edtTaskHandler);
        edtTaskHandler.setText(username);
    }

    private void createSpinnerStatus(){
        Spinner spnStatus = findViewById(R.id.spnStatus);
        List<String> statuses = new ArrayList<>();
        statuses.add("Not Started");
        statuses.add("In progress");
        statuses.add("Finished");
        statuses.add("Overdue");
        statuses.add("Unable to start");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapter);
        spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedStatus = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void createSpinnerConfirmation(){
        Spinner spnConfirm = findViewById(R.id.spnConfirmation);
        List<String> confirms = new ArrayList<>();
        confirms.add("Done");
        confirms.add("Unable to start");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, confirms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnConfirm.setAdapter(adapter);
        spnConfirm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedConfirm = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month+1) + "/" + year;
        switch (getDateNumberFlag){
            case 1:
                TextView txtTimeBegin = findViewById(R.id.txtTimeBegin);
                txtTimeBegin.setText(date);
                break;
            case 2:
                TextView txtTimeFinish  = findViewById(R.id.txtTimeFinish);
                txtTimeFinish.setText(date);
                break;
            case 3:
                TextView txtTimeCreated  = findViewById(R.id.txtTimeCreated);
                txtTimeCreated.setText(date);
                break;
            case 4:
                TextView txtTimeComment  = findViewById(R.id.txtTimeComment);
                txtTimeComment.setText(date);
                break;
            default:
                break;
        }

    }

    public void clickGetTimeBegin(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "Time Begin");
        getDateNumberFlag = 1;
    }

    public void clickGetTimeFinish(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "Time Finish");
        getDateNumberFlag = 2;
    }

    public void clickGetTimeCreated(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "Time Created");
        getDateNumberFlag = 3;
    }

    public void clickGetTimeComment(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "Time Comment");
        getDateNumberFlag = 4;
    }

    private Timestamp changeStringToTime(String time) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse(time);
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }

    public void onClickDoneCreate(View view) {
        try{
            String error = "";
            String name = ((EditText)findViewById(R.id.edtTaskName)).getText().toString();
            String description = ((EditText)findViewById(R.id.edtDescription)).getText().toString();
            String handlingContent = ((EditText)findViewById(R.id.edtHandlingContent)).getText().toString();
            String status = selectedStatus;
            String confirm = selectedConfirm;
            String txtTimeBegin = ((TextView)findViewById(R.id.txtTimeBegin)).getText().toString();
            Timestamp dateBegin = changeStringToTime(txtTimeBegin);
            String txtTimeFinish = ((TextView)findViewById(R.id.txtTimeFinish)).getText().toString();
            Timestamp dateFinish = changeStringToTime(txtTimeFinish);
            String txtTimeCreated = ((TextView)findViewById(R.id.txtTimeCreated)).getText().toString();
            Timestamp dateCreated = changeStringToTime(txtTimeCreated);
            String creator = ((EditText)findViewById(R.id.edtCreator)).getText().toString();
            String taskHandler = ((EditText)findViewById(R.id.edtTaskHandler)).getText().toString();
            String comment = null;
            String mark = null;
            Timestamp dateComment = null;
            if (userRole == Role.Admin){
                comment = ((EditText)findViewById(R.id.edtComment)).getText().toString();
                mark = ((EditText)findViewById(R.id.edtMark)).getText().toString();
                String txtTimeComment = ((TextView)findViewById(R.id.txtTimeComment)).getText().toString();
                dateComment = changeStringToTime(txtTimeComment);
            }

            String test = "image.png";
            Charset charset = StandardCharsets.UTF_16;
            byte[] confirmationI = test.getBytes(charset);
            System.out.println(confirmationI);

            PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
            PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
            PersonalTaskManagerDAO taskManagerDAO = new PersonalTaskManagerDAO();
            PersonalTaskInfoDTO infoDTO = new PersonalTaskInfoDTO(name, null, description, handlingContent, status, creator, taskHandler, confirm, confirmationI);
            System.out.println(name + description + handlingContent + status + creator + taskHandler + confirm);
            if (infoDAO.createNewTask(infoDTO, userRole)){
                int id = infoDAO.getNewTaskId();
                infoDTO.setId(id);
                PersonalTaskTimeDTO timeDTO = new PersonalTaskTimeDTO(id, dateBegin, dateFinish, dateCreated);
                System.out.println(id + dateBegin.toString() + dateFinish.toString() + dateCreated.toString());
                PersonalTaskManagerDTO managerDTO = null;
                if (timeDAO.createNewTaskTime(timeDTO));
                else error += "\nCannot create task! Please check the date inputs again.";
                if (userRole == Role.Admin) {
                    managerDTO = new PersonalTaskManagerDTO(id, comment, Integer.parseInt(mark), dateComment);
                    if (taskManagerDAO.createNewTaskManager(managerDTO)) ;
                    else error += "\nCannot create task! Please check Manager part again.";
                }
            }else{
                error += "\nCannot create task! Please check the details again.";
            }

            if (error.equals("")){
                Intent intent = this.getIntent();
                this.setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clickUploadImage(View view) {
    }

    public void onClickSubmitEdit(View view) {
    }
}
