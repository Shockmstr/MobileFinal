package hieubd.mobilefinal;


import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import hieubd.dao.GroupDAO;
import hieubd.dao.UserDAO;
import hieubd.dto.GroupDTO;
import hieubd.dto.Role;
import hieubd.dto.UserDTO;
import hieubd.mobilefinal.ui.UserAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends Fragment {
    private GroupDTO dto;
    private FloatingActionButton fab;
    private ListView listManagerFragment;
    private GridView gridUserFragment;
    private UserAdapter managerAdapter, userAdapter;

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
        gridUserFragment = getActivity().findViewById(R.id.gridUserFragment);
        listManagerFragment = getActivity().findViewById(R.id.listManagerFragment);
        createListView();
        createGridView();
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
                                addUser();
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
            builder.setIcon(R.drawable.add_plus);
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
                        if (manager.getRole() == Role.Manager){
                            dto.setManagerId(manager.getId());
                            dao.updateGroup(dto);
                            List<UserDTO> userDTOList2 = new ArrayList<>();
                            userDTOList2.add(manager);
                            if (managerAdapter == null) managerAdapter = new UserAdapter(userDTOList2);
                            managerAdapter.setUserDTOList(userDTOList2);
                            listManagerFragment.setAdapter(managerAdapter);
                        }else{
                            Toast.makeText(getContext(), "This user is not a manager!", Toast.LENGTH_LONG).show();
                        }
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

    private void addUser(){
        GroupDAO dao = new GroupDAO();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add user");
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setIcon(R.drawable.add_plus);
            builder.setView(input);
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    UserDAO userDAO = new UserDAO();
                    String username = input.getText().toString();
                    if (!userDAO.checkUsernameIsExisted(username)){
                        Toast.makeText(getContext(), "Username not existed", Toast.LENGTH_SHORT).show();
                    }else{
                        UserDTO user = userDAO.getUserByUsername(username);
                        if (dao.isUserExistedInGroup(dto, user.getId())){
                            Toast.makeText(getContext(), "This group already has that user", Toast.LENGTH_LONG).show();
                        }else{
                            if (user.getRole() == Role.User){
                                dao.createUserInGroup(dto, user);
                                List<UserDTO> userDTOList2 = userDAO.getAllUserByGroupID(dto.getId());
                                if (userDTOList2 != null) {
                                    if (userAdapter == null) userAdapter = new UserAdapter(userDTOList2);
                                    userAdapter.setUserDTOList(userDTOList2);
                                    gridUserFragment.setAdapter(userAdapter);
                                }
                            }else{
                                    Toast.makeText(getContext(), "Cannot put the manager in here!", Toast.LENGTH_LONG).show();
                                }
                        }
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

    private void updateTheGroupDto(){
        GroupDAO dao = new GroupDAO();
        dto = dao.getGroupById(dto.getId());
    }

    private void createListView(){
        UserDAO userDAO = new UserDAO();
        UserDTO manager = userDAO.getUserById(dto.getManagerId());
        List<UserDTO> userDTOList = new ArrayList<>();
        if (manager != null) userDTOList.add(manager);
        if (userDTOList != null){
            managerAdapter = new UserAdapter(userDTOList);
            listManagerFragment.setAdapter(managerAdapter);
            listManagerFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                    alertDialog.setTitle("Confirmation");
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    alertDialog.setMessage("Delete this manager from group?");
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GroupDAO groupDAO = new GroupDAO();
                            updateTheGroupDto();
                            if (groupDAO.deleteManagerFromGroup(dto)){
                                Toast.makeText(view.getContext(), "Manager deleted", Toast.LENGTH_SHORT).show();
                                updateTheGroupDto();
                                UserDTO manager2 = userDAO.getUserById(dto.getManagerId());
                                if (manager2 == null){
                                    List<UserDTO> userDTOList2 = new ArrayList<>();
                                    managerAdapter.setUserDTOList(userDTOList2);
                                    listManagerFragment.setAdapter(managerAdapter);
                                }
                            }else{
                                Toast.makeText(view.getContext(), "Failed to delete!", Toast.LENGTH_SHORT).show();
                            }
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
            });
        }
    }

    private void createGridView(){
        try{
            UserDAO userDAO = new UserDAO();
            List<UserDTO> userDTOList = userDAO.getAllUserByGroupID(dto.getId());
            if (userDTOList != null){
                userAdapter = new UserAdapter(userDTOList);

                gridUserFragment.setAdapter(userAdapter);
                gridUserFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        UserDTO user = (UserDTO) userAdapter.getItem(i);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                        alertDialog.setTitle("Confirmation");
                        alertDialog.setIcon(R.mipmap.ic_launcher);
                        alertDialog.setMessage("Delete this user from group?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GroupDAO groupDAO = new GroupDAO();
                                //updateTheGroupDto();
                                if (groupDAO.deleteUserFromGroup(user.getId(), dto.getId())){
                                    Toast.makeText(view.getContext(), "User deleted", Toast.LENGTH_SHORT).show();
                                    updateTheGroupDto();
                                    List<UserDTO> userDTOList2 = userDAO.getAllUserByGroupID(dto.getId());
                                    if (userDTOList2 != null) {
                                        userAdapter.setUserDTOList(userDTOList2);
                                        gridUserFragment.setAdapter(userAdapter);
                                    }
                                }else{
                                    Toast.makeText(view.getContext(), "Failed to delete!", Toast.LENGTH_SHORT).show();
                                }
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
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
