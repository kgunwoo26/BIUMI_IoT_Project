package com.example.biumi_iot_project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyHistoryViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final int mResource;
    private final ArrayList<My_History> myHistories;
    LocalTime now = LocalTime.now();
    private int hour, minute;

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private HashMap<String, Object> childUpdates = new HashMap<>();
    private Map<String, Object> userValue = new HashMap<>();

    public MyHistoryViewAdapter(Context context, int Resource, ArrayList<My_History> Histories) {
        mContext = context;
        mResource = Resource;
        myHistories = Histories;
    }

    @Override
    public int getCount() {
        return myHistories.size();
    }

    @Override
    public Object getItem(int position) {
        return myHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        hour = now.getHour();
        minute = now.getMinute();

        TextView tv_alarm = convertView.findViewById(R.id.alarm);
        TextView tv_history = convertView.findViewById(R.id.history_text);
        TextView tv_building = convertView.findViewById(R.id.building);
        TextView tv_floor = convertView.findViewById(R.id.floor);
        Button btn_history = convertView.findViewById(R.id.history_btn);

        tv_alarm.setText(
                ((hour - myHistories.get(position).alarm_h) * 60 +
                        (minute - myHistories.get(position).alarm_m)) + "분전 알림");
        tv_building.setText(myHistories.get(position).building);
        tv_floor.setText(myHistories.get(position).floor);

        switch (myHistories.get(position).state) {
            case 1:
                tv_history.setText(myHistories.get(position).history_h + ":" + myHistories.get(position).history_m + "완료");
                btn_history.setText("완료");
                btn_history.setClickable(false);
                btn_history.setTextColor(Color.parseColor("#6f6f6f"));
                btn_history.setBackgroundResource(R.drawable.btn_complete);
                break;
            case 3:
                tv_history.setText(myHistories.get(position).history_h + ":" + myHistories.get(position).history_m + "예약");
                btn_history.setText("예약됨");
                btn_history.setTextColor(Color.parseColor("#ffffff"));
                btn_history.setOnClickListener(view -> showDialog(position));
                btn_history.setBackgroundResource(R.drawable.btn_reserved);
                break;
        }
        return convertView;
    }

    public void showDialog(int position) {
        Select_Dialog oDialog = new Select_Dialog(mContext);
        oDialog.setCancelable(false);
        oDialog.show();

        oDialog.findViewById(R.id.complete).setOnClickListener(view -> {
            myHistories.get(position).setState(1);

            userValue.put("history", hour + ":" + minute);
            userValue.put("name", myHistories.get(position).name);
            userValue.put("state", myHistories.get(position).state);

            childUpdates.put("/biumi/"
                            + myHistories.get(position).building + "-" + myHistories.get(position).floor +
                            "/" + myHistories.get(position).alarm_h + ":" + myHistories.get(position).alarm_m
                    , userValue);
            reference.updateChildren(childUpdates);

            oDialog.dismiss();
        });
        oDialog.findViewById(R.id.cancle).setOnClickListener(view -> {
            myHistories.get(position).setState(2);

            userValue.put("history", myHistories.get(position).history_h + ":" + myHistories.get(position).history_h);
            userValue.put("name", myHistories.get(position).name);
            userValue.put("state", myHistories.get(position).state);

            childUpdates.put("/biumi/"
                            + myHistories.get(position).building + "-" + myHistories.get(position).floor +
                            "/" + myHistories.get(position).alarm_h + ":" + myHistories.get(position).alarm_m
                    , userValue);
            reference.updateChildren(childUpdates);

            oDialog.dismiss();
        });

        oDialog.findViewById(R.id.close).setOnClickListener(view -> oDialog.dismiss());
    }
}