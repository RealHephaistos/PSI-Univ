package com.example.psi_univ;

import androidx.annotation.NonNull;

import java.util.Date;

public class EventPSI {
    private Date startTime;
    private Date endTime;
    private String subject;

    public EventPSI(){}

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n   startTime = " + startTime +
                "\n    endTime =   " + endTime +
                "\n    subject =   " + subject + "\n";
    }
}
