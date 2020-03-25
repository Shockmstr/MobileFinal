package hieubd.mobilefinal;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import hieubd.dao.GroupDAO;
import hieubd.dto.GroupDTO;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupInfoFragment extends Fragment {
    private GroupDTO dto;
    private Button btnChangeName, btnDeleteGroup;

    public GroupInfoFragment() {
        // Required empty public constructor
    }

    public GroupInfoFragment(GroupDTO dto) {
        // Required empty public constructor
        this.dto = dto;
    }

    public void setDto(GroupDTO dto) {
        this.dto = dto;
    }

    @Override
    public void onStart() {
        super.onStart();
        btnChangeName = getActivity().findViewById(R.id.btnChangeName);
        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Edit Name");
                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GroupDAO groupDAO = new GroupDAO();
                        dto.setName(input.getText().toString());
                        if (groupDAO.updateGroup(dto)){
                            Toast.makeText(view.getContext(), "Name changed", Toast.LENGTH_SHORT).show();
                            TextView txtGroupName = getView().findViewById(R.id.txtGroupInfoName);
                            txtGroupName.setText(dto.getName());
                        }else{
                            Toast.makeText(view.getContext(), "Failed to change!", Toast.LENGTH_SHORT).show();
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
        });

        btnDeleteGroup = getActivity().findViewById(R.id.btnDeleteGroup);
        btnDeleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
                alertDialog.setTitle("Confirmation");
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.setMessage("Are you sure you want to delete?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GroupDAO groupDAO = new GroupDAO();
                        if (groupDAO.deleteGroup(dto.getId())){
                            Toast.makeText(view.getContext(), "Group deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), ManageGroupActivity.class);

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_group_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtGroupName = getView().findViewById(R.id.txtGroupInfoName);
        txtGroupName.setText(dto.getName());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
