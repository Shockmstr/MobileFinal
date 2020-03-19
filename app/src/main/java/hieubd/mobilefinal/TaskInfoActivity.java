package hieubd.mobilefinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskInfoActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private TextView txtTimeBegin;
    private TextView txtTimeFinish;
    private TextView txtTimeCreated;
    private Button btnTimeBegin;
    private Button btnTimeFinish;
    private Button btnTimeCreated;
    private Calendar calTimeBegin;
    private Calendar calTimeFinish;
    private Calendar calTimeCreated;

    static final int DATE_DIALOG_ID = 0;

    private TextView txtActiveTime;
    private Calendar activeCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);
        createSpinnerStatus();
        txtTimeBegin = findViewById(R.id.txtTimeBegin);
        btnTimeBegin = findViewById(R.id.btnTimeBegin);
        calTimeBegin = Calendar.getInstance();

    }

    private void updateDisplay(TextView text, Calendar date){
        String s = date.get(Calendar.DAY_OF_MONTH) + "/"
                    + (date.get(Calendar.MONTH)+1) + "/"
                    + date.get(Calendar.YEAR);
        text.setText(s);
    }
    private void createSpinnerStatus(){
        Spinner spnStatus = findViewById(R.id.spnStatus);
        List<String> statuses = new ArrayList<>();
        statuses.add("Not Begin");
        statuses.add("Ongoing");
        statuses.add("Finished");
        statuses.add("Late");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapter);
    }

    public void onClickDoneCreate(View view) {
        Intent intent = this.getIntent();
        EditText edtTaskName = findViewById(R.id.edtTaskName);
        EditText edtDescription = findViewById(R.id.edtDescription);
        intent.putExtra("taskname", edtTaskName.getText().toString());
        intent.putExtra("taskdesc", edtDescription.getText().toString());
        this.setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + (month+1) + "/" + year;
        TextView txtTimeBegin = findViewById(R.id.txtTimeBegin);
        txtTimeBegin.setText(date);
    }

    public void clickGetTimeBegin(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "Time Begin");
    }

    public void clickGetTimeFinish(View view) {
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(getFragmentManager(), "Time Finish");
    }
}
