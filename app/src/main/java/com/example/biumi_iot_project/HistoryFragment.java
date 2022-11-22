package com.example.biumi_iot_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.biumi_iot_project.databinding.FragmentHistoryBinding;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    ArrayList<My_History> My_history;
    HistoryViewAdapter adapter;
    MyDBHelper myDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        ListView history = binding.history;

        myDBHelper = new MyDBHelper(getContext());

        My_history = new ArrayList<>();

        Cursor cursor = myDBHelper.getAllUsersByMethod();

        for(int i = 0; i < cursor.getCount(); i ++) {
            cursor.moveToPosition(i);
            int alarm = Integer.valueOf(cursor.getString(1));
            int history_h = Integer.valueOf(cursor.getString(2));
            int history_m = Integer.valueOf(cursor.getString(3));
            int floor = Integer.valueOf(cursor.getString(5));
            int h_case = Integer.valueOf(cursor.getString(6));
            My_history.add(new My_History(alarm, history_h, history_m, cursor.getString(4), floor,h_case));
        }

        adapter = new HistoryViewAdapter(getActivity(), R.layout.my_history_item, My_history, getActivity());

        history.setAdapter(adapter);

        return binding.getRoot();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}