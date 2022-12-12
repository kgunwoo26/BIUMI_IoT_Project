package com.example.biumi_iot_project;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.biumi_iot_project.databinding.FragmentHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MyHistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private ArrayList<My_History> myHistories;
    private MyHistoryViewAdapter adapter;
    private final Handler handler = new Handler();
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private String NAME;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        ListView history = binding.history;

        myHistories = new ArrayList<>();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        NAME = auth.getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = "", building = "", floor = "", name = "";
                int alarm_h = 0, alarm_m = 0, history_h = 0, history_m = 0, state = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    value = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                }
                String[] a = value.split("\\}");
                for (String b : a) {
                    String[] c = b.split("\\{|,|\\s");
                    for(String d : c) {
                        String[]l = d.split("=");
                        if(l.length == 1) {
                            String[]m = l[0].split("-");
                            String[]n = l[0].split(":");
                            if(m.length == 2) {
                                building = m[0];
                                floor = m[1];
                            }
                            else if(n.length == 2) {
                                alarm_h = Integer.parseInt(n[0]);
                                alarm_m = Integer.parseInt(n[1]);
                            }
                        }
                        else if(l.length == 2) {
                            if(l[0].equals("history")) {
                                String[] h = l[1].split(":");
                                history_h = Integer.parseInt(h[0]);
                                history_m = Integer.parseInt(h[1]);
                            }
                            else if(l[0].equals("state"))
                                state = Integer.parseInt(l[1]);
                            else
                                name = l[1];
                        }
                    }
                    if(name.equals(NAME))
                        myHistories.add(new My_History(name,alarm_h,alarm_m,history_h,history_m,building,floor,state));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

        handler.postDelayed(() -> {
            adapter = new MyHistoryViewAdapter(getActivity(), R.layout.my_history_item, myHistories);

            history.setAdapter(adapter);
        }, 1000);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}