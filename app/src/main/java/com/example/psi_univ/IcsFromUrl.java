package com.example.psi_univ;
import java.io.IOException;
import java.nio.channels.*;
import java.io.FileOutputStream;
import java.net.URL;

public class IcsFromUrl {
    //URL TEST https://www.google.com/calendar/ical/hdpka717lurrk1qu3pds5q7u40%40group.calendar.google.com/public/basic.ics

    public static void getICS(String url, String file_name) throws IOException { //file_name string without .ics
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(file_name+".ics");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
