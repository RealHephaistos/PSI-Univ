package com.example.psi_univ.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class Event {
    private final Calendar start;
    private final Calendar end;
    private final String subject;
    private Event next;

    public Event(String start, String end, String subject) {
        this.start = Calendar.getInstance();
        this.end = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            this.start.setTime(Objects.requireNonNull(sdf.parse(start)));
            this.end.setTime(Objects.requireNonNull(sdf.parse(end)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert this.start.before(this.end);
        this.subject = subject;
        this.next = null;
    }

    public Event() {
        this.start = null;
        this.end = null;
        this.subject = null;
        this.next = null;
    }

    /**
     * @return true if the event is empty
     */
    public boolean isEmpty() {
        return this.start == null && this.end == null && this.subject == null;
    }

    /**
     * @param d the date to compare to
     * @return true if d is between start and end
     */
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

    public Event getNext() {
        return next;
    }

    public void setNext(Event next) {
        this.next = next;
    }
}
