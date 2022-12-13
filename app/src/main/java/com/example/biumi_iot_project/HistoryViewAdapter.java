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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HistoryViewAdapter extends BaseAdapter {

    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private final Context mContext;
    private final ArrayList<My_History> myHistories;
    private final int mResource;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private Calendar calendar = Calendar.getInstance();
    private String year = String.valueOf(calendar.get(Calendar.YEAR));
    private String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
    private String date = String.valueOf(calendar.get(Calendar.DATE));
    private String NAME;
    private String mDate;

    LocalTime now = LocalTime.now();
    private HashMap<String, Object> childUpdates = new HashMap<>();
    private Map<String, Object> userValue = new HashMap<>();

    public HistoryViewAdapter(Context context, int Resource, ArrayList<My_History> Histories, String Date) {
        mContext = context;
        mResource = Resource;
        myHistories = Histories;
        mDate = Date;
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
        NAME = auth.getUid();
        int hour = now.getHour();
        int minute = now.getMinute();

        TextView tv_alarm = convertView.findViewById(R.id.alarm);
        TextView tv_history = convertView.findViewById(R.id.history_text);
        TextView tv_building = convertView.findViewById(R.id.building);
        TextView tv_floor = convertView.findViewById(R.id.floor);
        Button btn_history = convertView.findViewById(R.id.history_btn);

        String[] dates = mDate.split("-");

        if(!year.equals(dates[0]) || !month.equals(dates[1]) || !date.equals(dates[2]))
            tv_alarm.setText(mDate);
        else tv_alarm.setText(
                ((hour - myHistories.get(position).alarm_h) * 60 +
                        (minute - myHistories.get(position).alarm_m)) + "분전 알림");
        tv_building.setText(myHistories.get(position).building);
        tv_floor.setText(myHistories.get(position).floor);

        Button.OnClickListener onClickListener = view -> {
            if(myHistories.get(position).state == 2) {
                    tv_history.setText(hour + ":" + minute + "예약");
                    myHistories.get(position).setHistory_h(hour);
                    myHistories.get(position).setHistory_m(minute);
                    myHistories.get(position).setState(3);
                    btn_history.setText("예약됨");

                    userValue.put("history", hour + ":" + minute);
                    userValue.put("name", NAME);
                    userValue.put("state", myHistories.get(position).state);

                    childUpdates.put("/biumi/"
                            + myHistories.get(position).building + "-" + myHistories.get(position).floor +
                            "/" + myHistories.get(position).alarm_h + ":" + myHistories.get(position).alarm_m
                            , userValue);
                    reference.updateChildren(childUpdates);
                    btn_history.setBackgroundResource(R.drawable.btn_reserved);
                }
        };

        switch (myHistories.get(position).state) {
            case 1:
                tv_history.setText(myHistories.get(position).history_h + ":" + myHistories.get(position).history_m + "완료");
                btn_history.setText("완료");
                btn_history.setClickable(false);
                btn_history.setTextColor(Color.parseColor("#6f6f6f"));
                btn_history.setBackgroundResource(R.drawable.btn_complete);
                break;
            case 2:
                tv_history.setText("- - - - - - -");
                btn_history.setText("미완료");
                btn_history.setOnClickListener(onClickListener);
                btn_history.setTextColor(Color.parseColor("#ffffff"));
                btn_history.setBackgroundResource(R.drawable.btn_noncomplete);
                break;
            case 3:
                tv_history.setText(myHistories.get(position).history_h + ":" + myHistories.get(position).history_m + "예약");
                btn_history.setText("예약됨");
                btn_history.setClickable(false);
                btn_history.setTextColor(Color.parseColor("#ffffff"));
                btn_history.setBackgroundResource(R.drawable.btn_reserved);
                break;
        }
        return convertView;
    }
}