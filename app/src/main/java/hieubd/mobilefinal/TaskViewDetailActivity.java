package hieubd.mobilefinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import hieubd.dao.PersonalTaskInfoDAO;
import hieubd.dao.PersonalTaskManagerDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dao.UserDAO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskManagerDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.dto.Role;
import hieubd.jdbc.JDBCUtils;

public class TaskViewDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private int getDateNumberFlag = -1;
    private String selectedStatus, selectedConfirm;
    private int thisTaskId;
    private Role userRole;
    private String username;
    private PersonalTaskInfoDTO infoDTO;
    private Uri selectedImage;
    private String oldImage;
    private boolean imageIsEdited = false;
    private FirebaseStorage storage; // used to create a FirebaseStorage instance
    private StorageReference storageReference; // point to the uploaded file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view_detail);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getInfoFromIntent();
        filterRoleForTask();
        createSpinnerStatus();
        autoFillAllDetails();
        managerMarkValidate();
    }

    private void getInfoFromIntent(){
        Intent intent = this.getIntent();
        infoDTO = (PersonalTaskInfoDTO) intent.getSerializableExtra("DTO");
        username = infoDTO.getTaskHandler();
        thisTaskId = infoDTO.getId();
        userRole = (Role) intent.getSerializableExtra("ROLE");
    }

    private void filterRoleForTask(){
        Intent intent = this.getIntent();
        int managerIsViewingHisEdit = intent.getIntExtra("MANAGERFLAG", 0);
        if (userRole == Role.User){
            //LinearLayout managerLayout = findViewById(R.id.managerLayout);
            //managerLayout.setVisibility(View.GONE);
            TextInputEditText edtEditComment = findViewById(R.id.edtEditComment);
            edtEditComment.setFocusable(false);
            EditText edtEditMark = findViewById(R.id.edtEditMark);
            edtEditMark.setFocusable(false);
            Button btnConfirm = findViewById(R.id.btnConfirm);
            btnConfirm.setClickable(false);
            Button btnGetConfirm = findViewById(R.id.btnPingConfirm);
            btnGetConfirm.setVisibility(View.VISIBLE);
        }
        if (userRole == Role.Manager && managerIsViewingHisEdit == 1){
            TextInputEditText edtEditComment = findViewById(R.id.edtEditComment);
            edtEditComment.setFocusable(false);
            EditText edtEditMark = findViewById(R.id.edtEditMark);
            edtEditMark.setFocusable(false);
            Button btnConfirm = findViewById(R.id.btnConfirm);
            btnConfirm.setClickable(false);
        }
        if (userRole == Role.Admin && managerIsViewingHisEdit == 1){
            LinearLayout managerLayout = findViewById(R.id.managerLayout);
            managerLayout.setVisibility(View.GONE);
        }
    }

    private void autoFillAllDetails(){
        TextInputEditText edtEditName = findViewById(R.id.edtEditTaskName);
        TextInputEditText edtEditDesc = findViewById(R.id.edtEditDescription);
        TextInputEditText edtEditHandlingContent = findViewById(R.id.edtEditHandlingContent);
        TextInputEditText edtEditCreator = findViewById(R.id.edtEditCreator);
        TextInputEditText edtEditHandler = findViewById(R.id.edtEditTaskHandler);
        TextView txtTimeBegin = findViewById(R.id.txtEditTimeBegin);
        TextView txtTimeFinish = findViewById(R.id.txtEditTimeFinish);
        TextView txtTimeCreated = findViewById(R.id.txtEditTimeCreated);
        Spinner spnStatus = findViewById(R.id.spnEditStatus);
        Button btnConfirm = findViewById(R.id.btnConfirm);
        TextInputEditText edtComment = findViewById(R.id.edtEditComment);
        EditText edtMark = findViewById(R.id.edtEditMark);
        TextView txtTimeComment = findViewById(R.id.txtEditTimeComment);
        //ImageView imgEdit = findViewById(R.id.imgDetail);

        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        PersonalTaskManagerDAO managerDAO = new PersonalTaskManagerDAO();
        //PersonalTaskInfoDTO infoDTO = (PersonalTaskInfoDTO) intent.getSerializableExtra("DTO");

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
        btnConfirm.setText(infoDTO.getConfirmation());
        formatBtnConfirm(btnConfirm);
        spnStatus.setOnItemSelectedListener(null);
        spnStatus.setSelection(getIndexOfValueFromSpinner(spnStatus, infoDTO.getStatus()));
        spnStatus.setOnItemSelectedListener(spnStatusListener);
        String imageName = infoDTO.getConfirmationImage();
        oldImage = imageName;
        downloadFromFireBase(imageName);
        if (managerDTO != null){
            edtComment.setText(managerDTO.getManagerComment());
            edtMark.setText(managerDTO.getManagerMark()+"");
            txtTimeComment.setText(JDBCUtils.fromTimestampToString(managerDTO.getManagerCommentBeginTime()));
        }else{
            txtTimeComment.setText(getCurrentDate());
        }
    }

    private void formatBtnConfirm(Button btnConfirm){
        String confirm = btnConfirm.getText().toString();
        switch (confirm){
            case "Not Confirmed":
            case "Waiting":
                btnConfirm.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
                btnConfirm.setTextColor(Color.BLACK);
                break;
            case "Accepted":
                btnConfirm.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                btnConfirm.setTextColor(Color.WHITE);
                break;
            case "Denied":
                btnConfirm.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                btnConfirm.setTextColor(Color.WHITE);
                break;
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

    private void createSpinnerStatus(){
        Spinner spnStatus = findViewById(R.id.spnEditStatus);
        List<String> statuses = new ArrayList<>();
        statuses.add("Not Started");
        statuses.add("In progress");
        statuses.add("Finished");
        statuses.add("Overdue");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnStatus.setAdapter(adapter);
    }

    private void submitEdit(){
        try{
            String error = "";
            String name = ((TextInputEditText)findViewById(R.id.edtEditTaskName)).getText().toString();
            String description = ((TextInputEditText)findViewById(R.id.edtEditDescription)).getText().toString();
            String handlingContent = ((TextInputEditText)findViewById(R.id.edtEditHandlingContent)).getText().toString();
            String status = selectedStatus;
            String confirm = ((Button)findViewById(R.id.btnConfirm)).getText().toString();
            String txtTimeBegin = ((TextView)findViewById(R.id.txtEditTimeBegin)).getText().toString();
            Timestamp dateBegin = JDBCUtils.fromStringToTime(txtTimeBegin);
            String txtTimeFinish = ((TextView)findViewById(R.id.txtEditTimeFinish)).getText().toString();
            Timestamp dateFinish = JDBCUtils.fromStringToTime(txtTimeFinish);
            String txtTimeCreated = ((TextView)findViewById(R.id.txtEditTimeCreated)).getText().toString();
            Timestamp dateCreated = JDBCUtils.fromStringToTime(txtTimeCreated);
            String creator = ((TextInputEditText)findViewById(R.id.edtEditCreator)).getText().toString();
            String taskHandler = ((TextInputEditText)findViewById(R.id.edtEditTaskHandler)).getText().toString();
            String comment = null;
            String mark = null;
            Timestamp dateComment = null;
            if (userRole == Role.Manager || userRole == Role.Admin){
                comment = ((TextInputEditText)findViewById(R.id.edtEditComment)).getText().toString();
                mark = ((EditText)findViewById(R.id.edtEditMark)).getText().toString();
                //if (mark == null || mark.isEmpty()) mark = "-1";
                String txtTimeComment = ((TextView)findViewById(R.id.txtEditTimeComment)).getText().toString();
                dateComment = JDBCUtils.fromStringToTime(txtTimeComment);
            }

            String imageName = null;
            if (imageIsEdited == true){
                imageName = UUID.randomUUID().toString();
            }else{
                imageName = oldImage;
            }

            //Validate
            UserDAO userDAO = new UserDAO();
            if (name.isEmpty()) error += "Name is empty\n";
            if (description.isEmpty()) error += "Description is empty\n";
            if (handlingContent.isEmpty()) error += "Handling content is empty\n";
            if (!userDAO.checkUsernameIsExisted(creator)) error += "Creator not existed\n";
            if (!userDAO.checkUsernameIsExisted(taskHandler)) error += "Task Handler not existed\n";
            if (userRole == Role.Manager || userRole == Role.Admin){
                if (comment.isEmpty()) error += "Comment is empty\n";
                if (mark == null || mark.isEmpty()) error += "Please add mark\n";

            }

            if (error.equals("")){
                PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
                PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
                PersonalTaskManagerDAO managerDAO = new PersonalTaskManagerDAO();
                PersonalTaskInfoDTO infoDTO = new PersonalTaskInfoDTO(name, description, handlingContent, status, creator, taskHandler, confirm, imageName);
                infoDTO.setId(thisTaskId);
                //System.out.println(name + description + handlingContent + status + creator + taskHandler + confirm);
                if (infoDAO.updateTask(infoDTO)){
                    if (imageIsEdited == true){
                        deleteFromFireBase(oldImage);
                        uploadToFireBase(selectedImage, imageName);
                    }
                    PersonalTaskTimeDTO timeDTO = new PersonalTaskTimeDTO(thisTaskId, dateBegin, dateFinish, dateCreated);
                    //System.out.println(thisTaskId + dateBegin.toString() + dateFinish.toString() + dateCreated.toString());
                    if (userRole == Role.Manager || userRole == Role.Admin){
                        PersonalTaskManagerDTO managerDTO = new PersonalTaskManagerDTO(thisTaskId, comment, Integer.parseInt(mark), dateComment);
                        if (managerDAO.updateTask(managerDTO));
                        else{
                            managerDAO.createNewTaskManager(managerDTO);
                        }
                    }
                    if (timeDAO.updateTask(timeDTO));
                    else error += "\nCannot update task! Please check the date inputs again.";
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
            }else{
                new AlertDialog.Builder(this).setTitle("Error").setMessage(error)
                        .setPositiveButton("Confirm", (dialogInterface, i) -> {
                            // do nothing
                        }).show();
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
        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> submitEdit());
        alertDialog.setNegativeButton("No", (dialogInterface, i) -> {
            // Do Nothing
        });
        alertDialog.show();
    }

    private void deleteThisTask(){
        try {
            PersonalTaskInfoDAO infoDAO = new PersonalTaskInfoDAO();
            boolean result = infoDAO.deleteTask(thisTaskId) ;
            if (result){
                Toast.makeText(this, "Delete task successfully", Toast.LENGTH_SHORT ).show();
                deleteFromFireBase(oldImage);
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
        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> deleteThisTask());
        alertDialog.setNegativeButton("No", (dialogInterface, i) -> {
            // Do Nothing
        });
        alertDialog.show();
    }

    private final int GALLERY_REQUEST_CODE = 14785;
    public void onClickEditImage(View view) {
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri image = data.getData();
                    selectedImage = image;
                    imageIsEdited = true;
                    ImageView imageView = findViewById(R.id.imgDetail);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                        imageView.setImageBitmap(bitmap);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    break;
            }
    }

    private void uploadToFireBase(Uri file, String fileName){
        if(file != null)
        {
            StorageReference ref = storageReference.child("images/"+ fileName);
            ref.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

    private void downloadFromFireBase(String filename){
        if (filename != null){
            try {
                StorageReference ref = storageReference.child("images/" + filename);
                final long ONE_MEGABYTE = 1024 * 1024;
                ref.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ImageView imageView = findViewById(R.id.imgDetail);
                        imageView.setImageBitmap(bm);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void deleteFromFireBase(String filename){
        if (filename != null){
            try {
                StorageReference ref = storageReference.child("images/" + filename);
                ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("SUCCESS");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void onclickChangeConfirm(View view) {
        Button btnConfirm = findViewById(R.id.btnConfirm);
        String confirm = btnConfirm.getText().toString();
        switch (confirm){
            case "Not Confirmed":
                btnConfirm.setText("Waiting");
                formatBtnConfirm(btnConfirm);
                break;
            case "Waiting":
                btnConfirm.setText("Accepted");
                formatBtnConfirm(btnConfirm);
                break;
            case "Accepted":
                btnConfirm.setText("Denied");
                formatBtnConfirm(btnConfirm);
                break;
            case "Denied":
                btnConfirm.setText("Not Confirmed");
                formatBtnConfirm(btnConfirm);
                break;
        }
    }
}
