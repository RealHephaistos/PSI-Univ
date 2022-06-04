package com.example.psi_univ.models;

import java.util.Calendar;

public class Event {
    private final Calendar start;
    private final Calendar end;
    private final String subject;

    public Event(Calendar start, Calendar end, String subject) {
        this.start = start;
        this.end = end;
        assert start.before(end);
        this.subject = subject;
    }

    public boolean isOverlapping(Calendar d) {
        return d.after(start) && d.before(end);
    }

    public Calendar getStart() {
        return start;
    }

    public Calendar getEnd() {
        return end;
    }

    public String getSubject() {
        return subject;
    }
}
