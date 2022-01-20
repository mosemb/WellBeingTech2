package com.hfad.wellbeingtech;

import java.util.Map;

public class DBHeartRate {

    private Integer heartBeat;
    private Map serverTimestamp;
    // private Date date;

    public DBHeartRate(Integer heartBeat, Map serverTimestamp) {
        this.heartBeat = heartBeat;
        this.serverTimestamp = serverTimestamp;
        // this.date = date;
    }

    public DBHeartRate() {
    }


    public Integer getHeartBeat() {
        return heartBeat;
    }

    public void setHeartBeat(Integer heartBeat) {
        this.heartBeat = heartBeat;
    }

    public Map getServerTimestamp() {
        return serverTimestamp;
    }

    public void setServerTimestamp(Map serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
    }




}
