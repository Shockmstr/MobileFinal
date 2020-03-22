package hieubd.mobilefinal.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import hieubd.dto.PersonalTaskInfoDTO;
import hieubd.dto.PersonalTaskTimeDTO;
import hieubd.mobilefinal.R;

public class TaskAdapter extends BaseAdapter {
    private List<PersonalTaskInfoDTO> taskInfoDTOList;
    private List<PersonalTaskTimeDTO> taskTimeDTOList;

    public TaskAdapter(List<PersonalTaskInfoDTO> taskInfoDTOList, List<PersonalTaskTimeDTO> taskTimeDTOList) {
        this.taskInfoDTOList = taskInfoDTOList;
        this.taskTimeDTOList = taskTimeDTOList;
    }

    public void setTaskInfoDTOList(List<PersonalTaskInfoDTO> taskInfoDTOList) {
        this.taskInfoDTOList = taskInfoDTOList;
    }

    public void setTaskTimeDTOList(List<PersonalTaskTimeDTO> taskTimeDTOList) {
        this.taskTimeDTOList = taskTimeDTOList;
    }

    @Override
    public int getCount() {
        return taskInfoDTOList.size();
    }

    @Override
    public Object getItem(int i) {
        return taskInfoDTOList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return taskInfoDTOList.indexOf(taskInfoDTOList.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.item, viewGroup, false);
        }
        PersonalTaskInfoDTO infoDTO = taskInfoDTOList.get(i);
        PersonalTaskTimeDTO timeDTO = taskTimeDTOList.get(i);
        TextView txtName = view.findViewById(R.id.txtItemName);
        TextView txtStatus = view.findViewById(R.id.txtItemStatus);
        TextView txtTimeFrom = view.findViewById(R.id.txtItemDateBegin);
        TextView txtTimeTo = view.findViewById(R.id.txtItemDateFinish);
        txtName.setText(infoDTO.getName());
        txtStatus.setText(infoDTO.getStatus());
        txtTimeFrom.setText(convertTimestampToString(timeDTO.getTimeBegin()));
        txtTimeTo.setText(convertTimestampToString(timeDTO.getTimeFinish()));
        changeStatusColor(infoDTO.getStatus(), txtStatus);
        return view;
    }

    private String convertTimestampToString(Timestamp time){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String sTime = sdf.format(time);
        return sTime;
    }

    private void changeStatusColor(String status, TextView txtView){
        switch (status){
            case "Not Started":
                txtView.setTextColor(Color.GRAY);
                break;
            case "In progress":
                txtView.setTextColor(Color.BLUE);
                break;
            case "Finished":
                txtView.setTextColor(Color.GREEN);
                break;
            case "Overdue":
            case "Unable to start":
                txtView.setTextColor(Color.RED);
                break;
        }
    }
}
