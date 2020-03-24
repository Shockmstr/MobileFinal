package hieubd.mobilefinal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.PersonalTaskManagerDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskManagerDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.dto.Role;
import hieubd.jdbc.JDBCUtils;

public class TaskViewDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private int getDateNumberFlag = -1;
    private int setSelectedProgrammatically = 0;
    private String selectedStatus, selectedConfirm, selectedSource, oldSource;
    private int thisTaskId;
    private Role userRole;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_detail);
        getInfoFromIntent();
        filterRoleForTask();
        createSpinnerStatus();
        createSpinnerConfirmation();
        autoFillAllDetails();
        managerMarkValidate();
    }

    private void getInfoFromIntent(){
        Intent intent = this.getIntent();
        PersonalTaskInfoDTO infoDTO = (PersonalTaskInfoDTO) intent.getSerializableExtra("DTO");
        username = infoDTO.getTaskHandler();
        thisTaskId = infoDTO.getId();
    }

    private void filterRoleForTask(){
        Intent intent = this.getIntent();
        userRole = (Role) intent.getSerializableExtra("ROLE");
        boolean managerIsViewingHisEdit = intent.getBooleanExtra("MANAGERFLAG", false);
        if (userRole == Role.User){
            //LinearLayout managerLayout = findViewById(R.id.managerLayout);
            //managerLayout.setVisibility(View.GONE);
            EditText edtEditComment = findViewById(R.id.edtEditComment);
            edtEditComment.setEnabled(false);
            EditText edtEditMark = findViewById(R.id.edtEditMark);
            edtEditMark.setEnabled(false);
            Spinner spnConfirm = findViewById(R.id.spnEditConfirmation);
            spnConfirm.setEnabled(false);
        }
        if (managerIsViewingHisEdit == true){
            EditText edtEditComment = findViewById(R.id.edtEditComment);
            edtEditComment.setEnabled(false);
            EditText edtEditMark = findViewById(R.id.edtEditMark);
            edtEditMark.setEnabled(false);
            Spinner spnConfirm = findViewById(R.id.spnEditConfirmation);
            spnConfirm.setEnabled(false);
        }
        if (userRole == Role.Admin && managerIsViewingHisEdit){
            LinearLayout managerLayout = findViewById(R.id.managerLayout);
            managerLayout.setVisibility(View.GONE);
        }
    }

    private void autoFillAllDetails(){
        EditText edtEditName = findViewById(R.id.edtEditTaskName);
        EditText edtEditDesc = findViewById(R.id.edtEditDescription);
        EditText edtEditHandlingContent = findViewById(R.id.edtEditHandlingContent);
        EditText edtEditCreator = findViewById(R.id.edtEditCreator);
        EditText edtEditHandler = findViewById(R.id.edtEditTaskHandler);
        TextView txtTimeBegin = findViewById(R.id.txtEditTimeBegin);
        TextView txtTimeFinish = findViewById(R.id.txtEditTimeFinish);
        TextView txtTimeCreated = findViewById(R.id.txtEditTimeCreated);
        Spinner spnStatus = findViewById(R.id.spnEditStatus);
        Spinner spnConfirm = findViewById(R.id.spnEditConfirmation);
        EditText edtComment = findViewById(R.id.edtEditComment);
        EditText edtMark = findViewById(R.id.edtEditMark);
        TextView txtTimeComment = findViewById(R.id.txtEditTimeComment);

        Intent intent = this.getIntent();
        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        PersonalTaskManagerDAO managerDAO = new PersonalTaskManagerDAO();
        PersonalTaskInfoDTO infoDTO = (PersonalTaskInfoDTO) intent.getSerializableExtra("DTO");

        PersonalTaskTimeDTO timeDTO = timeDAO.getTaskById(thisTaskId);
        PersonalTaskManagerDTO managerDTO = managerDAO.getTaskById(thisTaskId);


        edtEditName.setText(infoDTO.getName());
        edtEditDesc.setText(infoDTO.getDescription());
        edtEditHandlingContent.setText(infoDTO.getHandlingContent());
        edtEditCreator.setText(infoDTO.getCreator());
        edtEditHandler.setText(infoDTO.getTaskHandler());
        txtTimeBegin.setText(JDBCUtils.fromTimestampToString(timeDTO.getTimeBegin()));
        txtTimeFinish.setText(JDBCUtils.fromTimestampToString(timeDTO.getTimeFinish()));
        txtTimeCreated.setText(JDBCUtils.fromTimestampToString(timeDTO.getTimeCreated()));
        spnStatus.setOnItemSelectedListener(null);
        spnConfirm.setOnItemSelectedListener(null);
        setSelectedProgrammatically = 0;
        spnStatus.setSelection(getIndexOfValueFromSpinner(spnStatus, infoDTO.getStatus()));
        spnConfirm.setSelection(getIndexOfValueFromSpinner(spnConfirm, infoDTO.getConfirmation()));
        setSelectedProgrammatically = 1;
        spnStatus.setOnItemSelectedListener(spnStatusListener);
        spnConfirm.setOnItemSelectedListener(spnConfirmListener);
        if (managerDTO != null){
            edtComment.setText(managerDTO.getManagerComment());
            edtMark.setText(managerDTO.getManagerMark());
            txtTimeComment.setText(JDBCUtils.fromTimestampToString(managerDTO.getManagerCommentBeginTime()));
        }else{
            txtTimeComment.setText(getCurrentDate());
        }
    }

    private void managerMarkValidate(){
        try {
            final EditText edtMark = findViewById(R.id.edtEditMark);
            edtMark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Nothing
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Nothing
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        int mark = Integer.parseInt(editable.toString());
                        if (mark < 1 || mark > 10){
                            Toast.makeText(TaskViewDetailActivity.this, "Mark can only be from 1 - 10", Toast.LENGTH_LONG).show();
                            editable.clear();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        String current = sdf.format(currentDate);
        return current ;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month+1) + "/" + year;
        switch (getDateNumberFlag){
            case 1:
                TextView txtTimeBegin = findViewById(R.id.txtEditTimeBegin);
                txtTimeBegin.setText(date);
                break;
            case 2:
                TextView txtTimeFinish  = findViewById(R.id.txtEditTimeFinish);
                txtTimeFinish.setText(date);
                break;
            case 3:
                TextView txtTimeComment  = findViewById(R.id.txtEditTimeComment);
                txtTimeComment.setText(date);
                break;
            default:
                break;
        }
    }

    public void clickEditTimeBegin(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment(2);
        datePickerFragment.show(getFragmentManager(), "Time Begin");
        getDateNumberFlag = 1;
    }

    public void clickEditTimeFinish(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment(2);
        datePickerFragment.show(getFragmentManager(), "Time Finish");
        getDateNumberFlag = 2;
    }

    private int getIndexOfValueFromSpinner(Spinner spn, String str){
        for (int i = 0; i < spn.getCount(); i++) {
            if (spn.getItemAtPosition(i).toString().equalsIgnoreCase(str)){
                return i;
            }
        }
        return 0;
    }

    private Spinner.OnItemSelectedListener spnStatusListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectedStatus = adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private Spinner.OnItemSelectedListener spnConfirmListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectedConfirm = adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void createSpinnerStatus(){
        Spinner spnStatus = findViewById(R.id.spnEditStatus);
        List<String> statuses = new ArrayList<>();
        statuses.add("Not Started");
        statuses.add("In progress");
        statuses.add("Finished");
        statuses.add("Overdue");
        statuses.add("Unable to start");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapter);
    }

    private void createSpinnerConfirmation(){
        Spinner spnConfirm = findViewById(R.id.spnEditConfirmation);
        List<String> confirms = new ArrayList<>();
        confirms.add("Not Confirmed");
        confirms.add("Done");
        confirms.add("Unable to start");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, confirms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnConfirm.setAdapter(adapter);
    }

    private void submitEdit(){
        try{
            String error = "";
            String name = ((EditText)findViewById(R.id.edtEditTaskName)).getText().toString();
            String description = ((EditText)findViewById(R.id.edtEditDescription)).getText().toString();
            String handlingContent = ((EditText)findViewById(R.id.edtEditHandlingContent)).getText().toString();
            String source = selectedSource;
            String status = selectedStatus;
            String confirm = selectedConfirm;
            String txtTimeBegin = ((TextView)findViewById(R.id.txtEditTimeBegin)).getText().toString();
            Timestamp dateBegin = JDBCUtils.fromStringToTime(txtTimeBegin);
            String txtTimeFinish = ((TextView)findViewById(R.id.txtEditTimeFinish)).getText().toString();
            Timestamp dateFinish = JDBCUtils.fromStringToTime(txtTimeFinish);
            String txtTimeCreated = ((TextView)findViewById(R.id.txtEditTimeCreated)).getText().toString();
            Timestamp dateCreated = JDBCUtils.fromStringToTime(txtTimeCreated);
            String creator = ((EditText)findViewById(R.id.edtEditCreator)).getText().toString();
            String taskHandler = ((EditText)findViewById(R.id.edtEditTaskHandler)).getText().toString();
            String comment = null;
            String mark = null;
            Timestamp dateComment = null;
            if (userRole == Role.Manager){
                comment = ((EditText)findViewById(R.id.edtEditComment)).getText().toString();
                mark = ((EditText)findViewById(R.id.edtEditMark)).getText().toString();
                if (mark == null || mark.isEmpty()) mark = "-1";
                String txtTimeComment = ((TextView)findViewById(R.id.txtEditTimeComment)).getText().toString();
                dateComment = JDBCUtils.fromStringToTime(txtTimeComment);
            }

            String image = "image.png";
            byte[] confirmationI = JDBCUtils.fromStringToBytes(image);

            PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
            PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
            PersonalTaskManagerDAO managerDAO = new PersonalTaskManagerDAO();
            PersonalTaskInfoDTO infoDTO = new PersonalTaskInfoDTO(name, description, handlingContent, status, creator, taskHandler, confirm, confirmationI);
            infoDTO.setId(thisTaskId);
            System.out.println(name + description + handlingContent + status + creator + taskHandler + confirm);
            if (infoDAO.updateTask(infoDTO)){
                PersonalTaskTimeDTO timeDTO = new PersonalTaskTimeDTO(thisTaskId, dateBegin, dateFinish, dateCreated);
                System.out.println(thisTaskId + dateBegin.toString() + dateFinish.toString() + dateCreated.toString());
                PersonalTaskManagerDTO managerDTO = new PersonalTaskManagerDTO(thisTaskId, comment, Integer.parseInt(mark), dateComment);
                if (timeDAO.updateTask(timeDTO));
                else error += "\nCannot update task! Please check the date inputs again.";
                if (managerDAO.updateTask(managerDTO));
                else{
                    managerDAO.createNewTaskManager(managerDTO);
                }

            }else{
                error += "\nCannot update task! Please check the details again.";
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

    public void onClickSubmitEdit(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirmation");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Are you sure you want to submit the edit?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                submitEdit();
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

    private void deleteThisTask(){
        try {
            PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
            PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
            PersonalTaskManagerDAO managerDAO = new PersonalTaskManagerDAO();
            boolean result = managerDAO.deleteTask(thisTaskId) && timeDAO.deleteTask(thisTaskId) && infoDAO.deleteTask(thisTaskId) ;
            if (result){
                Toast.makeText(this, "Delete task successfully", Toast.LENGTH_SHORT ).show();
                Intent intent = this.getIntent();
                this.setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(this, "Delete task failed", Toast.LENGTH_SHORT ).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onClickDelete(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirmation");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setMessage("Are you sure you want to delete this task?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteThisTask();
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

}
