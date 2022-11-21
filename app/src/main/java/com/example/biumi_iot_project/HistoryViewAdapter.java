package com.example.biumi_iot_project;

import android.app.Activity;
import android.content.Context;
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
    private final Activity mActivity;
    private int check = 0;
    LocalTime now = LocalTime.now();

    public HistoryViewAdapter(Context context, int Resource, ArrayList<My_History> dates, Activity activity) {
        mContext = context;
        mResource = Resource;
        my_history = dates;
        mActivity = activity;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView tv_alarm = convertView.findViewById(R.id.alarm);
        TextView tv_history = convertView.findViewById(R.id.history_text);
        TextView tv_building = convertView.findViewById(R.id.building);
        Button btn_history = convertView.findViewById(R.id.history_btn);


        tv_alarm.setText(my_history.get(position).alarm + "분전 알림");
        tv_building.setText(my_history.get(position).building + "관");

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (my_history.get(position).h_case) {
                    case 2:
                        tv_history.setText(now.getHour() + ":" + now.getMinute() + "예약");
                        my_history.get(position).setHistory_h(now.getHour());
                        my_history.get(position).setHistory_m(now.getMinute());
                        my_history.get(position).setH_case(3);
                        btn_history.setText("예약됨");
                        btn_history.setBackgroundResource(R.drawable.btn_reserved);
                        break;
                    case 3:
                        tv_history.setText("- - - - - - -");
                        my_history.get(position).setH_case(2);
                        btn_history.setText("미완료");
                        btn_history.setBackgroundResource(R.drawable.btn_noncomplete);
                        break;
                }
            }
        };

        switch (my_history.get(position).h_case) {
            case 1:
                tv_history.setText(my_history.get(position).history_h + ":" + my_history.get(position).history_m + "완료");
                btn_history.setText("완료");
                btn_history.setClickable(false);
                break;
            case 2:
                tv_history.setText("- - - - - - -");
                btn_history.setText("미완료");
                btn_history.setOnClickListener(onClickListener);
                break;
            case 3:
                tv_history.setText(my_history.get(position).history_h + ":" + my_history.get(position).history_m + "예약");
                btn_history.setText("예약됨");
                btn_history.setOnClickListener(onClickListener);
                break;
        }


        return convertView;
    }
}