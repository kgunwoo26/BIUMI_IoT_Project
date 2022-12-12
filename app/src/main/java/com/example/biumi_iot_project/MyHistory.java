package com.example.biumi_iot_project;

public class MyHistory {
    String name, history, state;

    MyHistory(String history, String name, String state) {
        this.name = name;
        this.history = history;
        this.state = state;
    }

    public String getHistory() {
        return history;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setName(String name) {
        this.name = name;
    }
}
