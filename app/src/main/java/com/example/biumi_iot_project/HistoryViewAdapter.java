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

import java.time.LocalTime;
import java.util.ArrayList;

public class HistoryViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final int mResource;
    private final ArrayList<My_History> my_history;
    private MyHistoryDBHelper myDBHelper;
    LocalTime now = LocalTime.now();

    public HistoryViewAdapter(Context context, int Resource, ArrayList<My_History> dates) {
        mContext = context;
        mResource = Resource;
        my_history = dates;
    }

    @Override
    public int getCount() {
        return my_history.size();
    }

    @Override
    public Object getItem(int position) {
        return my_history.get(position);
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
        int hour = now.getHour();
        int minute = now.getMinute();

        myDBHelper = new MyHistoryDBHelper(parent.getContext());

        TextView tv_alarm = convertView.findViewById(R.id.alarm);
        TextView tv_history = convertView.findViewById(R.id.history_text);
        TextView tv_building = convertView.findViewById(R.id.building);
        TextView tv_floor = convertView.findViewById(R.id.floor);
        Button btn_history = convertView.findViewById(R.id.history_btn);

        tv_alarm.setText(
                ((hour - my_history.get(position).alarm_h) * 60 +
                        (minute - my_history.get(position).alarm_m)) + "분전 알림");
        tv_building.setText(my_history.get(position).building);
        tv_floor.setText(my_history.get(position).floor + "F");

        Button.OnClickListener onClickListener = view -> {
            switch (my_history.get(position).h_case) {
                case 2:
                    tv_history.setText(hour + ":" + minute + "예약");
                    my_history.get(position).setHistory_h(hour);
                    my_history.get(position).setHistory_m(minute);
                    my_history.get(position).setH_case(3);
                    btn_history.setText("예약됨");

                    myDBHelper.delete(
                            String.valueOf(my_history.get(position).alarm_h),
                            String.valueOf(my_history.get(position).alarm_m),
                            my_history.get(position).building,
                            String.valueOf(my_history.get(position).floor));

                    myDBHelper.insert(
                            String.valueOf(my_history.get(position).alarm_h),
                            String.valueOf(my_history.get(position).alarm_m),
                            String.valueOf(hour),
                            String.valueOf(minute),my_history.get(position).building,
                            String.valueOf(my_history.get(position).floor),"3");

                    btn_history.setBackgroundResource(R.drawable.btn_reserved);
                    break;
                case 3:
                    tv_history.setText("- - - - - - -");
                    my_history.get(position).setH_case(2);
                    btn_history.setText("미완료");

                    myDBHelper.delete(
                            String.valueOf(my_history.get(position).alarm_h),
                            String.valueOf(my_history.get(position).alarm_m),
                            my_history.get(position).building,
                            String.valueOf(my_history.get(position).floor));

                    myDBHelper.insert(
                            String.valueOf(my_history.get(position).alarm_h),
                            String.valueOf(my_history.get(position).alarm_m)
                            ,"0", "0",
                            my_history.get(position).building,
                            String.valueOf(my_history.get(position).floor),"2");

                    btn_history.setBackgroundResource(R.drawable.btn_noncomplete);
                    break;
            }
        };

        switch (my_history.get(position).h_case) {
            case 1:
                tv_history.setText(my_history.get(position).history_h + ":" + my_history.get(position).history_m + "완료");
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
                tv_history.setText(my_history.get(position).history_h + ":" + my_history.get(position).history_m + "예약");
                btn_history.setText("예약됨");
                btn_history.setOnClickListener(onClickListener);
                btn_history.setTextColor(Color.parseColor("#ffffff"));
                btn_history.setBackgroundResource(R.drawable.btn_reserved);
                break;
        }


        return convertView;
    }
}