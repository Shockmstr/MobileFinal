package hieubd.mobilefinal;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment {
    private int flag;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    /**
     * 1: TaskInfoActivity
     * 2: TaskViewDetailActivity
     */
    @SuppressLint("ValidFragment")
    public DatePickerFragment(int flag){
        this.flag = flag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        return textView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);// + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = null;
        switch (flag){
            case 1:
                datePickerDialog = new DatePickerDialog(getActivity(), (TaskInfoActivity)getActivity(), year, month, day);
                break;
            case 2:
                datePickerDialog = new DatePickerDialog(getActivity(), (TaskViewDetailActivity)getActivity(), year, month, day);
                break;
            default:
                break;
        }
        return datePickerDialog;
    }
}
