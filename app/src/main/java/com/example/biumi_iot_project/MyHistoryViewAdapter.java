package com.example.biumi_iot_project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.time.LocalTime;
import java.util.ArrayList;

public class MyHistoryViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final int mResource;
    private final ArrayList<My_History> my_history;
    private MyHistoryDBHelper myDBHelper;
    LocalTime now = LocalTime.now();
    private int hour, minute;

    public MyHistoryViewAdapter(Context context, int Resource, ArrayList<My_History> dates) {
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
        hour = now.getHour();
        minute = now.getMinute();

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

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(position);
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
                btn_history.setTextColor(Color.parseColor("#ffffff"));
                btn_history.setOnClickListener(onClickListener);
                btn_history.setBackgroundResource(R.drawable.btn_noncomplete);
                break;
            case 3:
                tv_history.setText(my_history.get(position).history_h + ":" + my_history.get(position).history_m + "예약");
                btn_history.setText("예약됨");
                btn_history.setTextColor(Color.parseColor("#ffffff"));
                btn_history.setOnClickListener(onClickListener);
                btn_history.setBackgroundResource(R.drawable.btn_reserved);
                break;
        }
        return convertView;
    }

    public void showDialog(int position) {
        Select_Dialog oDialog = new Select_Dialog(mContext);
        oDialog.setCancelable(false);
        oDialog.show();

        oDialog.findViewById(R.id.complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_history.get(position).setH_case(1);

                myDBHelper.delete(
                        my_history.get(position).name,
                        String.valueOf(my_history.get(position).alarm_h),
                        String.valueOf(my_history.get(position).alarm_m),
                        my_history.get(position).building,
                        String.valueOf(my_history.get(position).floor));

                myDBHelper.insert(
                        my_history.get(position).name,
                        String.valueOf(my_history.get(position).alarm_h),
                        String.valueOf(my_history.get(position).alarm_m),
                        String.valueOf(hour), String.valueOf(minute),
                        my_history.get(position).building,
                        String.valueOf(my_history.get(position).floor),"1");

                oDialog.dismiss();
            }
        });
        oDialog.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                my_history.get(position).setH_case(2);

                myDBHelper.delete(
                        my_history.get(position).name,
                        String.valueOf(my_history.get(position).alarm_h),
                        String.valueOf(my_history.get(position).alarm_m),
                        my_history.get(position).building,
                        String.valueOf(my_history.get(position).floor));

                myDBHelper.insert(
                        "",
                        String.valueOf(my_history.get(position).alarm_h),
                        String.valueOf(my_history.get(position).alarm_m)
                        ,"0", "0",
                        my_history.get(position).building,
                        String.valueOf(my_history.get(position).floor),"2");

                oDialog.dismiss();
            }
        });

        oDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oDialog.dismiss();
            }
        });
    }
}