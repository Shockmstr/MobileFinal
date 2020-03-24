package hieubd.mobilefinal.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hieubd.dto.UserDTO;
import hieubd.mobilefinal.R;

public class UserAdapter extends BaseAdapter {
    private List<UserDTO> userDTOList = new ArrayList<>();

    public UserAdapter(List<UserDTO> userDTOList) {
        this.userDTOList = userDTOList;
    }

    public void setUserDTOList(List<UserDTO> userDTOList) {
        this.userDTOList = userDTOList;
    }

    @Override
    public int getCount() {
        return userDTOList.size();
    }

    @Override
    public Object getItem(int i) {
        return userDTOList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return userDTOList.indexOf(userDTOList.get(i));
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            view = inflater.inflate(R.layout.user_item, viewGroup, false);
        }
        UserDTO userDTO = userDTOList.get(i);
        TextView txtName = view.findViewById(R.id.txtUserItemUsername);
        TextView txtRole = view.findViewById(R.id.txtUserItemRole);
        txtName.setText(userDTO.getUsername());
        txtRole.setText(userDTO.getRole().toString());
        changeRoleColor(userDTO.getRole().toString(), txtRole);
        return view;
    }

    private void changeRoleColor(String role, TextView txtView){
        switch (role){
            case "User":
                txtView.setTextColor(Color.rgb(31, 104, 56));
                break;
            case "Manager":
                txtView.setTextColor(Color.BLUE);
                break;
            default:
                break;
        }
    }
}
