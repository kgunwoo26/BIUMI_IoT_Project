package com.example.biumi_iot_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.biumi_iot_project.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        Spinner building_spinner = (Spinner) binding.buildingList;
        ArrayAdapter<CharSequence> buiding_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.building_list, android.R.layout.simple_spinner_item);
        buiding_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        building_spinner.setAdapter(buiding_adapter);

        Spinner floor_spinner = (Spinner) binding.floorList;
        ArrayAdapter<CharSequence> floor_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.floor_list, android.R.layout.simple_spinner_item);
        floor_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floor_spinner.setAdapter(floor_adapter);

        ImageView trash = binding.trash;

//        쓰레기 통 현황에 따라 이미지 변경
//        0~20 > R,drawable.trash;
//        20~40 > R,drawable.trash_25;
//        40~60 > R,drawable.trash_50;
//        60~80 > R,drawable.trash_75;
//        80~ > R,drawable.trash_100; 알림이 오는 시점 부터 쓰레기 통이 곽 참을 의미함

        trash.setImageResource(R.drawable.trash_50);

        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}