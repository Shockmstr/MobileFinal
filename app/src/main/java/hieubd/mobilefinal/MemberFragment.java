package hieubd.mobilefinal;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import hieubd.dao.GroupDAO;
import hieubd.dao.UserDAO;
import hieubd.dto.GroupDTO;
import hieubd.dto.UserDTO;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends Fragment {
    private GroupDTO dto;
    private FloatingActionButton fab;

    public MemberFragment() {
        // Required empty public constructor
    }

    public MemberFragment(GroupDTO dto) {
        this.dto = dto;
    }

    public void setDto(GroupDTO dto) {
        this.dto = dto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        fab = getActivity().findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), fab);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.miAddManager:
                                addManager();
                                break;
                            case R.id.miAddUser:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private void addManager(){
        GroupDAO dao = new GroupDAO();
        if (dao.isManagerExistedInGroup(dto)){
            Toast.makeText(getContext(), "This group already has a manager", Toast.LENGTH_LONG).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add manager");
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    UserDAO userDAO = new UserDAO();
                    String username = input.getText().toString();
                    if (!userDAO.checkUsernameIsExisted(username)){
                        Toast.makeText(getContext(), "Username not existed", Toast.LENGTH_SHORT).show();
                    }else{
                        UserDTO manager = userDAO.getUserByUsername(username);
                        dto.setManagerId(manager.getId());
                        dao.updateGroup(dto);
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do nothing
                }
            });
            builder.show();
        }
    }
}
