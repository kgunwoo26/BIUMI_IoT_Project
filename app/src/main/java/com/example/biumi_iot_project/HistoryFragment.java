package com.example.biumi_iot_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.biumi_iot_project.databinding.FragmentSecondBinding;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FragmentSecondBinding binding;
    ArrayList<My_History> My_history;
    HistoryViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        binding = FragmentSecondBinding.inflate(inflater, container, false);

        ListView history = binding.history;

        My_history = new ArrayList<>();

        My_history.add(new My_History(5, 22,10, "상상", 1));
        My_history.add(new My_History(5, 23,20, "상상", 3));

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