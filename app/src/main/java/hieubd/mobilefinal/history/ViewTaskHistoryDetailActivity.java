package hieubd.mobilefinal.history;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import hieubd.dao.PersonalTaskManagerDAO;
import hieubd.dao.PersonalTaskTimeDAO;
import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskManagerDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.dto.Role;
import hieubd.jdbc.JDBCUtils;
import hieubd.mobilefinal.R;

public class ViewTaskHistoryDetailActivity extends AppCompatActivity {
    private int thisTaskId;
    private Role userRole;
    private String username;
    private PersonalTaskInfoDTO infoDTO;
    private FirebaseStorage storage; // used to create a FirebaseStorage instance
    private StorageReference storageReference; // point to the uploaded file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_history_detail);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getInfoFromIntent();
        autoFillAllDetails();
    }

    private void getInfoFromIntent(){
        Intent intent = this.getIntent();
        infoDTO = (PersonalTaskInfoDTO) intent.getSerializableExtra("DTO");
        username = infoDTO.getTaskHandler();
        thisTaskId = infoDTO.getId();
        userRole = (Role) intent.getSerializableExtra("ROLE");
    }

    private void autoFillAllDetails(){
        TextInputEditText edtHistoryName = findViewById(R.id.edtHistoryTaskName);
        TextInputEditText edtHistoryDesc = findViewById(R.id.edtHistoryDescription);
        TextInputEditText edtHistoryHandlingContent = findViewById(R.id.edtHistoryHandlingContent);
        TextInputEditText edtHistoryCreator = findViewById(R.id.edtHistoryCreator);
        TextInputEditText edtHistoryHandler = findViewById(R.id.edtHistoryTaskHandler);
        TextView txtTimeBegin = findViewById(R.id.txtHistoryTimeBegin);
        TextView txtTimeFinish = findViewById(R.id.txtHistoryTimeFinish);
        TextView txtTimeCreated = findViewById(R.id.txtHistoryTimeCreated);
        TextView txtStatus = findViewById(R.id.txtHistoryStatus);
        Button btnConfirm = findViewById(R.id.btnHistoryConfirm);
        TextInputEditText edtComment = findViewById(R.id.edtHistoryComment);
        EditText edtMark = findViewById(R.id.edtHistoryMark);
        TextView txtTimeComment = findViewById(R.id.txtHistoryTimeComment);

        PersonalTaskTimeDAO timeDAO = new PersonalTaskTimeDAO();
        PersonalTaskManagerDAO managerDAO = new PersonalTaskManagerDAO();
        //PersonalTaskInfoDTO infoDTO = (PersonalTaskInfoDTO) intent.getSerializableExtra("DTO");

        PersonalTaskTimeDTO timeDTO = timeDAO.getTaskById(thisTaskId);
        PersonalTaskManagerDTO managerDTO = managerDAO.getTaskById(thisTaskId);


        edtHistoryName.setText(infoDTO.getName());
        edtHistoryDesc.setText(infoDTO.getDescription());
        edtHistoryHandlingContent.setText(infoDTO.getHandlingContent());
        edtHistoryCreator.setText(infoDTO.getCreator());
        edtHistoryHandler.setText(infoDTO.getTaskHandler());
        txtTimeBegin.setText(JDBCUtils.fromTimestampToString(timeDTO.getTimeBegin()));
        txtTimeFinish.setText(JDBCUtils.fromTimestampToString(timeDTO.getTimeFinish()));
        txtTimeCreated.setText(JDBCUtils.fromTimestampToString(timeDTO.getTimeCreated()));
        txtStatus.setText(infoDTO.getStatus());
        btnConfirm.setText(infoDTO.getConfirmation());
        formatBtnConfirm(btnConfirm);
        String imageName = infoDTO.getConfirmationImage();
        downloadFromFireBase(imageName);
        if (managerDTO != null){
            edtComment.setText(managerDTO.getManagerComment());
            edtMark.setText(managerDTO.getManagerMark()+"");
            txtTimeComment.setText(JDBCUtils.fromTimestampToString(managerDTO.getManagerCommentBeginTime()));
        }
    }

    private void formatBtnConfirm(Button btnConfirm){
        String confirm = btnConfirm.getText().toString();
        switch (confirm){
            case "Not Confirmed":
            case "Waiting":
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

    public void onClickReturn(View view) {
        setResult(RESULT_OK);
        finish();
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
                        ImageView imageView = findViewById(R.id.imgHistory);
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
}
