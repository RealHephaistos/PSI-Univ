package com.example.psi_univ;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class IcsFromUrl {
    //URL TEST https://www.google.com/calendar/ical/hdpka717lurrk1qu3pds5q7u40%40group.calendar.google.com/public/basic.ics

    public static void getICS(String url, String file_name) throws IOException { //file_name string without .ics
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream("./src/main/java/com/example/psi_univ/ics/" + file_name + ".ics"); //ICS file download in specific repertory
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
