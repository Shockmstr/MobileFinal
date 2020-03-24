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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.dto.Role;

public class TaskInfoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private int getDateNumberFlag = -1;
    private String selectedStatus;
    private Role userRole;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        Intent intent = this.getIntent();
        userRole = (Role) intent.getSerializableExtra("ROLE");
        autoFillCreatorAndWorkHandler();
        createSpinnerStatus();
    }

    private void autoFillCreatorAndWorkHandler(){
        Intent intent = this.getIntent();
        username = intent.getStringExtra("USERNAME");
        EditText edtTaskHandler = findViewById(R.id.edtTaskHandler);
        edtTaskHandler.setText(username);
    }

    private void createSpinnerStatus(){
        Spinner spnStatus = findViewById(R.id.spnStatus);
        List<String> statuses = new ArrayList<>();
        statuses.add("Not started");
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
            default:
                break;
        }

    }

    public void clickGetTimeBegin(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment(1);
        datePickerFragment.show(getFragmentManager(), "Time Begin");
        getDateNumberFlag = 1;
    }

    public void clickGetTimeFinish(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment(1);
        datePickerFragment.show(getFragmentManager(), "Time Finish");
        getDateNumberFlag = 2;
    }

    private String getTimeCreated() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        String timeCreated = sdf.format(currentDate);
        return timeCreated;
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
            String confirm = "Not Confirmed";
            String txtTimeBegin = ((TextView)findViewById(R.id.txtTimeBegin)).getText().toString();
            Timestamp dateBegin = changeStringToTime(txtTimeBegin);
            String txtTimeFinish = ((TextView)findViewById(R.id.txtTimeFinish)).getText().toString();
            Timestamp dateFinish = changeStringToTime(txtTimeFinish);
            String txtTimeCreated = getTimeCreated();
            Timestamp dateCreated = changeStringToTime(txtTimeCreated);
            String creator = username;
            String taskHandler = ((EditText)findViewById(R.id.edtTaskHandler)).getText().toString();

            String test = "image.png";
            Charset charset = StandardCharsets.UTF_16;
            byte[] confirmationI = test.getBytes(charset);
            System.out.println(confirmationI);

            PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
            PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
            PersonalTaskInfoDTO infoDTO = new PersonalTaskInfoDTO(name, description, handlingContent, status, creator, taskHandler, confirm, confirmationI);
            System.out.println(name + description + handlingContent + status + creator + taskHandler + confirm);
            if (infoDAO.createNewTask(infoDTO, userRole)){
                int id = infoDAO.getNewTaskId();
                infoDTO.setId(id);
                PersonalTaskTimeDTO timeDTO = new PersonalTaskTimeDTO(id, dateBegin, dateFinish, dateCreated);
                System.out.println(id + dateBegin.toString() + dateFinish.toString() + dateCreated.toString());
                if (timeDAO.createNewTaskTime(timeDTO));
                else error += "\nCannot create task! Please check the date inputs again.";
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

}
