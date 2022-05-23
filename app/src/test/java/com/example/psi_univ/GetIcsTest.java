package com.example.psi_univ;

import org.junit.Test;

public class GetIcsTest {
    @Test
    public void getARealIcs() throws Exception {
        IcsFromUrl ics = new IcsFromUrl();
        IcsFromUrl.getICS("https://www.google.com/calendar/ical/hdpka717lurrk1qu3pds5q7u40%40group.calendar.google.com/public/basic.ics", "test");
    }


    @Test
    public void notAnIcs() throws Exception {
        IcsFromUrl ics = new IcsFromUrl();
        IcsFromUrl.getICS("https://www.google.com", "falseTest");
    }
}
