package com.example.biumi_iot_project;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.biumi_iot_project.databinding.FragmentHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private ArrayList<My_History> myHistories;
    private HistoryViewAdapter adapter;
    private ListView history;
    private final Handler handler = new Handler();
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference reference = database.getReference();
    private Calendar calendar = Calendar.getInstance();
    private Calendar spinner_calendar = Calendar.getInstance();
    private String year = String.valueOf(calendar.get(Calendar.YEAR));
    private String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
    private String date = String.valueOf(calendar.get(Calendar.DATE));
    private ArrayAdapter<CharSequence> data_adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        history = binding.history;

        Spinner year_spinner = binding.yearList;
        ArrayAdapter<CharSequence> year_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.year_list, android.R.layout.simple_spinner_item);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(year_adapter);
        year_spinner.setSelection(2);

        Spinner month_spinner = binding.monthList;
        ArrayAdapter<CharSequence> month_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.month_list, android.R.layout.simple_spinner_item);
        month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        month_spinner.setAdapter(month_adapter);
        month_spinner.setSelection(spinner_calendar.get(Calendar.MONTH));

        Spinner date_spinner = binding.dateList;

        if(spinner_calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 31)
            data_adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.date_list_31, android.R.layout.simple_spinner_item);
        else if(spinner_calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 30)
            data_adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.date_list_30, android.R.layout.simple_spinner_item);
        else if(spinner_calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 29)
            data_adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.date_list_29, android.R.layout.simple_spinner_item);
        else
            data_adapter = ArrayAdapter.createFromResource(getContext(),
                    R.array.date_list_28, android.R.layout.simple_spinner_item);
        data_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        date_spinner.setAdapter(data_adapter);
        date_spinner.setSelection(spinner_calendar.get(Calendar.DATE)-1);

        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_calendar.set(Calendar.YEAR, Integer.parseInt(year_spinner.getSelectedItem().toString()));

                if(spinner_calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 31)
                    data_adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.date_list_31, android.R.layout.simple_spinner_item);
                else if(spinner_calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 30)
                    data_adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.date_list_30, android.R.layout.simple_spinner_item);
                else if(spinner_calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 29)
                    data_adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.date_list_29, android.R.layout.simple_spinner_item);
                else
                    data_adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.date_list_28, android.R.layout.simple_spinner_item);
                data_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                date_spinner.setAdapter(data_adapter);
                date_spinner.setSelection(spinner_calendar.get(Calendar.DATE)-1);
            };
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        month_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_calendar.set(Calendar.MONTH, Integer.parseInt(month_spinner.getSelectedItem().toString()) - 1);

                if(spinner_calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 31)
                    data_adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.date_list_31, android.R.layout.simple_spinner_item);
                else if(spinner_calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 30)
                    data_adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.date_list_30, android.R.layout.simple_spinner_item);
                else if(spinner_calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == 29)
                    data_adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.date_list_29, android.R.layout.simple_spinner_item);
                else
                    data_adapter = ArrayAdapter.createFromResource(getContext(),
                            R.array.date_list_28, android.R.layout.simple_spinner_item);
                data_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                date_spinner.setAdapter(data_adapter);
                date_spinner.setSelection(spinner_calendar.get(Calendar.DATE)-1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_calendar.set(Calendar.DATE, Integer.parseInt(date_spinner.getSelectedItem().toString()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.dateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year = String.valueOf(spinner_calendar.get(Calendar.YEAR));
                month = String.valueOf(spinner_calendar.get(Calendar.MONTH) + 1);
                date = String.valueOf(spinner_calendar.get(Calendar.DATE));

                showList();
            }
        });

        showList();

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void showList() {
        myHistories = new ArrayList<>();
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
                    boolean check = !b.equals("");
                    String[] c = b.split("\\{|,|\\s");
                    for(String d : c) {
                        String[]l = d.split("=");
                        if(l.length == 1) {
                            String[]m = l[0].split("-");
                            String[]n = l[0].split(":");
                            if(m.length == 3) {
                                if(!year.equals(m[0]) || !month.equals(m[1]) || !date.equals(m[2]))
                                    check = false;
                            }
                            else if(m.length == 2) {
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
                    if(check)
                        myHistories.add(new My_History(name,alarm_h,alarm_m,history_h,history_m,building,floor,state));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });

        handler.postDelayed(() -> {
            adapter = new HistoryViewAdapter(getActivity(), R.layout.my_history_item, myHistories, year+"-"+month+"-"+date);
            history.setAdapter(adapter);
        }, 1000);
    }
}