package com.example.biumi_iot_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.biumi_iot_project.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final Handler handler = new Handler();
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final LocalTime now = LocalTime.now();
    private final Calendar calendar = Calendar.getInstance();
    private final String year = String.valueOf(calendar.get(Calendar.YEAR));
    private final String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
    private final String date = String.valueOf(calendar.get(Calendar.DATE));

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        Spinner building_spinner = (Spinner) binding.buildingList;
        ArrayAdapter<CharSequence> buiding_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.building_list, android.R.layout.simple_spinner_item);
        buiding_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        building_spinner.setAdapter(buiding_adapter);
        building_spinner.setSelection(0);

        Spinner floor_spinner = (Spinner) binding.floorList;
        ArrayAdapter<CharSequence> floor_adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.floor_list, android.R.layout.simple_spinner_item);
        floor_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floor_spinner.setAdapter(floor_adapter);
        floor_spinner.setSelection(0);

        building_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RequestThread thread = new RequestThread();
                thread.start();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.refresh.setOnClickListener(view -> {
            RequestThread thread = new RequestThread();
            thread.start();
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class RequestThread extends Thread {
        public void run() {
            try {
                String urlStr = "http://172.20.10.3";
                StringBuilder outputBuilder = new StringBuilder();
                URL url = new URL(urlStr);
                HttpURLConnection urconn = (HttpURLConnection) url.openConnection();
                urconn.setDoInput(true);
                urconn.setDoOutput(true);
                urconn.setConnectTimeout(15000); // 15초
                int resCode = urconn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urconn.getInputStream(), StandardCharsets.UTF_8));
                    String line;
                    while (true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        outputBuilder.append(line).append("\n");
                    }
                    reader.close();
                    urconn.disconnect();
                }
                String output = outputBuilder.toString();
                println(output, binding.buildingList.getSelectedItemPosition());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void println(final String data, int position) {
        handler.post(() -> {
            try{
                JSONArray trashArray = new JSONArray(data);

                int res = 0;
                for(int i=0; i<trashArray.length(); i++)
                {
                    JSONObject trashObject = trashArray.getJSONObject(i);
                    TextView textView = (TextView) binding.percent;

                    if(position == 0 && trashObject.getString("deviceName").equals("A")) {
                        res = (int) (40 - Double.parseDouble(trashObject.getString("distance"))) * 100 / 40;
                        textView.setText(res + "%");
                    }
                    else if(position == 1 && trashObject.getString("deviceName").equals("B")) {
                        res = (int) (40 - Double.parseDouble(trashObject.getString("distance"))) * 100 / 40;
                        textView.setText(res + "%");
                    }
                }
                percent(res);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void percent(int i) {
        int[] check = {0};

        View view = (View) binding.trash;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int trash = 611;
        params.height = trash / 100 * i;
        view.setLayoutParams(params);

        String building = binding.buildingList.getSelectedItem().toString();
        String floor = binding.floorList.getSelectedItem().toString();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = "", check_building = "", check_floor = "";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    value = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                }
                String[] v = value.split("\\{|,|\\s|\\}");
                for (String l : v) {
                    String[] m = l.split("-");
                    String[] n = l.split("=");
                    if(n.length == 1 && m.length == 2) {
                        check_building = m[0];
                        check_floor = m[1];
                    }
                    if(n.length == 2 && n[0].equals("state") && !n[1].equals("1")
                            && check_building.equals(building) && check_floor.equals(floor + "=")) {
                        check[0] += 1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        handler.postDelayed(() -> {
            if(check[0] == 0 && i >= 60)
                showDialog9();
        }, 1000);
    }

    public void showDialog9()
    {
        Alarm_Dialog oDialog = new Alarm_Dialog(getContext());
        oDialog.setCancelable(false);
        oDialog.show();
        TextView textbuilding = (TextView) oDialog.findViewById(R.id.building);
        textbuilding.setText(binding.buildingList.getSelectedItem().toString());

        TextView textfloor = (TextView) oDialog.findViewById(R.id.floor);
        textfloor.setText(binding.floorList.getSelectedItem().toString());

        String alarm = now.getHour() + ":" + now.getMinute();

        oDialog.findViewById(R.id.reserved).setOnClickListener(view -> {
            String history = now.getHour() + ":" + now.getMinute();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            String name = auth.getUid();
            MyHistory myHistory = new MyHistory(history,name, "3");

            reference.child("biumi").child(year + "-" + month + "-" + date)
                    .child(binding.buildingList.getSelectedItem().toString() + "-" + binding.floorList.getSelectedItem().toString())
                    .child(alarm).setValue(myHistory);

            oDialog.dismiss();
        });

        oDialog.findViewById(R.id.close).setOnClickListener(view -> {
            MyHistory myHistory = new MyHistory("0:0","", "2");

            reference.child("biumi").child(year + "-" + month + "-" + date)
                    .child(binding.buildingList.getSelectedItem().toString() + "-" + binding.floorList.getSelectedItem().toString())
                    .child(alarm).setValue(myHistory);

            oDialog.dismiss();
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = "", building = "", floor = "", state = "";
                int alarm_h = 0, alarm_m = 0;
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
                            if(l[0].equals("state"))
                                state = l[1];
                        }
                    }
                    if(check &&
                            building.equals(binding.buildingList.getSelectedItem().toString()) &&
                            floor.equals(binding.floorList.getSelectedItem().toString()) &&
                            alarm.equals(alarm_h + ":" + alarm_m) &&
                            state.equals("3"))
                        oDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}