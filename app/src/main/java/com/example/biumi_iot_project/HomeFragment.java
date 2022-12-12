package com.example.biumi_iot_project;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.biumi_iot_project.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private final String name = "1";
    private final int trash = 605;

    private FragmentHomeBinding binding;
    private Handler handler = new Handler();
    MyHistoryDBHelper myDBHelper;
    LocalTime now = LocalTime.now();
    int p = 10;

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

        binding.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p += 5;
                p %= 100;
                binding.percent.setText(p +"%");
                percent(p);
//                RequestThread thread = new RequestThread();
//                thread.start();
            }
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
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urconn.getInputStream(), "UTF-8"));
                    String line = null;
                    while (true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        outputBuilder.append(line + "\n");
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

    private void println(final String data, int position) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONArray trashArray = new JSONArray(data);

                    int res = 0;
                    for(int i=0; i<trashArray.length(); i++)
                    {
                        JSONObject trashObject = trashArray.getJSONObject(i);
                        TextView textView = (TextView) getActivity().findViewById(R.id.percent);

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
            }
        });
    }

    private void percent(int i) {
        View view = (View) binding.trash;
        String building = binding.buildingList.getSelectedItem().toString();
        String floor = binding.floorList.getSelectedItem().toString();
        myDBHelper = new MyHistoryDBHelper(getContext());
        Cursor cursor;
        int check = 0;

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = trash / 100 * i;
        view.setLayoutParams(params);

//        cursor = myDBHelper.getAllUsersByMethod();
//        while (cursor.moveToNext()) {
//            if(cursor.getString(6).equals(building) &&
//                    cursor.getString(7).equals(floor) &&
//                    !cursor.getString(8).equals("1"))
//                check ++;
//        }
//        if(check == 0 && i >= 60)
//            showDialog9();
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

        int alarm_h = now.getHour();
        int alarm_m = now.getMinute();

        oDialog.findViewById(R.id.reserved).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDBHelper = new MyHistoryDBHelper(getContext());
                myDBHelper.insert(
                        name,
                        alarm_h + "",
                        alarm_m + "",
                        now.getHour() + "",
                        now.getMinute() + "",
                        binding.buildingList.getSelectedItem().toString(),
                        binding.floorList.getSelectedItem().toString(),
                        "3");

                oDialog.dismiss();
            }
        });

        oDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDBHelper = new MyHistoryDBHelper(getContext());
                myDBHelper.insert(
                        "",
                        alarm_h+ "",
                        alarm_m + "",
                        "0",
                        "0",
                        binding.buildingList.getSelectedItem().toString(),
                        binding.floorList.getSelectedItem().toString(),
                        "2");

                oDialog.dismiss();
            }
        });
    }
}