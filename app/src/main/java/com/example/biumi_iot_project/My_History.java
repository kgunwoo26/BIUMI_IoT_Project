package com.example.biumi_iot_project;

public class My_History {
    String name;
    int alarm_h;
    int alarm_m;
    int history_h;
    int history_m;
    String building;
    int floor;
    int h_case;

//    1 : 완료
//    2 : 미완료
//    3 : 예약됨

    My_History(String name, int alarm_h, int alarm_m, int history_h, int history_m, String building,int floor, int h_case) {
        this.name = name;
        this.alarm_h = alarm_h;
        this.alarm_m = alarm_m;
        this.history_h = history_h;
        this.history_m = history_m;
        this.building = building;
        this.floor = floor;
        this.h_case = h_case;
    }

    public void setHistory_h(int history_h) {
        this.history_h = history_h;
    }

    public void setHistory_m(int history_m) {
        this.history_m = history_m;
    }

    public void setH_case(int h_case) {
        this.h_case = h_case;
    }
}
