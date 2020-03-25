package hieubd.mobilefinal.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hieubd.dto.GroupDTO;
import hieubd.mobilefinal.R;

public class GroupAdapter extends BaseAdapter {
    private List<GroupDTO> dtoList = new ArrayList<>();

    public GroupAdapter(List<GroupDTO> dtoList) {
        this.dtoList = dtoList;
    }

    public void setDtoList(List<GroupDTO> dtoList) {
        this.dtoList = dtoList;
    }

    @Override
    public int getCount() {
        return dtoList.size();
    }

    @Override
    public Object getItem(int i) {
        return dtoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return dtoList.indexOf(dtoList.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.group_item, viewGroup, false);
        }
        GroupDTO dto = dtoList.get(i);
        TextView txtName = view.findViewById(R.id.txtGroupItemName);
        txtName.setText(dto.getName());
        return view;
    }
}
