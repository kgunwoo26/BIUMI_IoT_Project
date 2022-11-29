package com.example.biumi_iot_project;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.biumi_iot_project.databinding.FragmentHistoryBinding;

import java.time.LocalTime;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    ArrayList<My_History> My_history;
    HistoryViewAdapter adapter;
    MyHistoryDBHelper myDBHelper;
    LocalTime now = LocalTime.now();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        ListView history = binding.history;

        myDBHelper = new MyHistoryDBHelper(getContext());

        My_history = new ArrayList<>();

        Cursor cursor = myDBHelper.getAllUsersByMethod();

        for(int i = 0; i < cursor.getCount(); i ++) {
            cursor.moveToPosition(i);
            int alarm_h = Integer.parseInt(cursor.getString(1));
            int alarm_m = Integer.parseInt(cursor.getString(2));
            int history_h = Integer.parseInt(cursor.getString(3));
            int history_m = Integer.parseInt(cursor.getString(4));
            int floor = Integer.parseInt(cursor.getString(6));
            int h_case = Integer.parseInt(cursor.getString(7));
            My_history.add(new My_History(alarm_h, alarm_m, history_h, history_m, cursor.getString(5), floor,h_case));
        }

        adapter = new HistoryViewAdapter(getActivity(), R.layout.my_history_item, My_history);

        history.setAdapter(adapter);

        return binding.getRoot();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}