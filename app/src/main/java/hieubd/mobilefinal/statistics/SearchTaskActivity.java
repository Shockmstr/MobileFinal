package hieubd.mobilefinal.statistics;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.dto.Role;
import hieubd.jdbc.JDBCUtils;
import hieubd.mobilefinal.R;
import hieubd.mobilefinal.TaskViewDetailActivity;
import hieubd.mobilefinal.ui.TaskAdapter;

public class SearchTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private int getDateNumberFlag = -1;
    private String selectedStatus;
    List<PersonalTaskInfoDTO> taskInfoDTOList;
    List<PersonalTaskTimeDTO> taskTimeDTOList;
    private TaskAdapter taskAdapter;
    private ListView taskListView;
    private Role role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_task);
        taskInfoDTOList = new ArrayList<>();
        taskListView = findViewById(R.id.listSearchResult);
        createSpinnerStatus();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month+1) + "/" + year;
        switch (getDateNumberFlag){
            case 1:
                TextView txtTimeBegin = findViewById(R.id.txtSearchDateFrom);
                txtTimeBegin.setText(date);
                break;
            case 2:
                TextView txtTimeFinish  = findViewById(R.id.txtSearchDateTo);
                txtTimeFinish.setText(date);
                break;
            default:
                break;
        }

    }

    public void clickGetDateFrom(View view) {
        DialogFragment datePickerFragment = new SearchDatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "Time From");
        getDateNumberFlag = 1;
    }

    public void clickGetDateTo(View view) {
        DialogFragment datePickerFragment = new SearchDatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "Time To");
        getDateNumberFlag = 2;
    }

    private void createSpinnerStatus(){
        Spinner spnStatus = findViewById(R.id.spnSearchStatus);
        List<String> statuses = new ArrayList<>();
        statuses.add("All");
        statuses.add("Not started");
        statuses.add("In progress");
        statuses.add("Finished");
        statuses.add("Overdue");
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

    private void createListView(List<PersonalTaskInfoDTO> taskInfoDTOS, List<PersonalTaskTimeDTO> taskTimeDTOS){
        Intent intent = this.getIntent();
        role = (Role) intent.getSerializableExtra("ROLE");
        taskAdapter = new TaskAdapter(taskInfoDTOS, taskTimeDTOS);
        taskListView.setAdapter(taskAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchTaskActivity.this, TaskViewDetailActivity.class);
                PersonalTaskInfoDTO infoDTO = (PersonalTaskInfoDTO) taskAdapter.getItem(i);
                intent.putExtra("DTO", infoDTO);
                intent.putExtra("ROLE", (Serializable) role);
                startActivityForResult(intent, 5000);
            }
        });
    }

    public void clickSearch(View view) {
        TextView txtDateFrom = findViewById(R.id.txtSearchDateFrom);
        TextView txtDateTo = findViewById(R.id.txtSearchDateTo);
        TextInputEditText edtSearchHandler = findViewById(R.id.edtSearchHandler);

        Timestamp dateFrom = null;
        Timestamp dateTo = null;
        String status = selectedStatus;
        String searchHandler = null;

        try{
            if (txtDateFrom.getText().toString().isEmpty()){
                dateFrom = null;
            }else{
                dateFrom = JDBCUtils.fromStringToTime(txtDateFrom.getText().toString());
            }

            if (txtDateTo.getText().toString().isEmpty()){
                dateTo = null;
            }else{
                dateTo = JDBCUtils.fromStringToTime(txtDateTo.getText().toString());
            }

            if (edtSearchHandler.getText().toString().isEmpty()){
                searchHandler = null;
            }else{
                searchHandler = edtSearchHandler.getText().toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
        List<PersonalTaskInfoDTO> infoDTOList = null;
        List<PersonalTaskTimeDTO> timeDTOList = timeDAO.getAllTaskBetweenDate(dateFrom, dateTo);
        if (timeDTOList != null){
            //taskTimeDTOList = timeDTOList;
            for (PersonalTaskTimeDTO timeDTO: timeDTOList) {
                int id = timeDTO.getId();
                PersonalTaskInfoDTO infoDTO = infoDAO.getTaskByIdWithConditions(id, status, searchHandler);
                if (infoDTO != null){
                    if (infoDTOList == null) infoDTOList = new ArrayList<>();
                    infoDTOList.add(infoDTO);
                }
            }
            createListView(infoDTOList, timeDTOList);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5000){
            if (resultCode == RESULT_OK){
                // do nothing
                taskListView.setAdapter(null);
            }
        }
    }
}
