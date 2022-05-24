package com.example.psi_univ.ui.models;

import java.util.Calendar;
import java.util.Date;

public class Event {
    private final Calendar start;
    private final Calendar end;

    public Event(Calendar start, Calendar end) {
        this.start = start;
        this.end = end;
        assert start.before(end);
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
}
