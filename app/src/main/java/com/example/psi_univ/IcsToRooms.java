package com.example.psi_univ;

import com.example.psi_univ.ui.models.Room;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class IcsToRooms {
    public IcsToRooms() {
    }

    public List<Room> getRooms() throws Exception {
        List<Room> rooms = new ArrayList<>();
        EventPSI tmpEvent = new EventPSI();
        int year, month, date, hrs, min;
        int parsingState = 0;

        List<String> line = new ArrayList<>();
        Scanner scanner = new Scanner(new File("./src/main/java/com/example/psi_univ/ics/ADECal.ics"));
        while (scanner.hasNext()) {
            line.add(scanner.nextLine());
        }
        for (String i : line) {
            if (i.contains("BEGIN:VEVENT")) {
                if (parsingState != 0) parsingError(parsingState);
                tmpEvent = new EventPSI();
                parsingState = 1;
            } else if (i.contains("DTSTART")) {
                if (parsingState != 1) parsingError(parsingState);
                year = Integer.parseInt(i.substring(8, 12));
                month = Integer.parseInt(i.substring(12, 14));
                date = Integer.parseInt(i.substring(14, 16));
                hrs = Integer.parseInt(i.substring(17, 19));
                min = Integer.parseInt(i.substring(19, 21));
                tmpEvent.setStartTime(new Date(year, month, date, hrs, min));
                parsingState = 2;
            } else if (i.contains("DTEND")) {
                if (parsingState != 2) parsingError(parsingState);
                year = Integer.parseInt(i.substring(6, 10));
                month = Integer.parseInt(i.substring(10, 12));
                date = Integer.parseInt(i.substring(12, 14));
                hrs = Integer.parseInt(i.substring(15, 17));
                min = Integer.parseInt(i.substring(17, 19));
                tmpEvent.setEndTime(new Date(year, month, date, hrs, min));
                parsingState = 3;
            } else if (i.contains("SUMMARY")) {
                if (parsingState != 3) parsingError(parsingState);
                tmpEvent.setSubject(i.substring(8));
                parsingState = 4;
            } else if (i.contains("LOCATION")) {
                if (parsingState != 4) parsingError(parsingState);
                String name = i.substring(9);
                int indiceRoom = -1;
                for (int j = 0; j < rooms.size(); j++) {
                    if (rooms.get(j).getRoomName().compareTo(name) == 0) indiceRoom = j;
                }
                if (indiceRoom == -1) {
                    List<EventPSI> events = new ArrayList<>();
                    events.add(tmpEvent);
                    rooms.add(new Room(name, events, false));
                    //TODO: Faudrait qu'on discute de la mise en commun
                } else {
                    Room actualRoom = rooms.get(indiceRoom);
                    List<EventPSI> events = actualRoom.getEvents();
                    events.add(tmpEvent);
                    rooms.set(indiceRoom, actualRoom);
                }
                parsingState = 5;
            } else if (i.contains("END:VEVENT")) {
                if (parsingState != 5) parsingError(parsingState);
                parsingState = 0;
            }
        }

        return rooms;
    }

    private void parsingError(int nbError) throws Exception {
        switch (nbError) {
            case 0:
                throw new Exception("BEGIN is expected");
            case 1:
                throw new Exception("DTSTART is expected");
            case 2:
                throw new Exception("DTEND is expected");
            case 3:
                throw new Exception("SUMMARY is expected");
            case 4:
                throw new Exception("LOCATION is expected");
            case 5:
                throw new Exception("END is expected");
            default:
                throw new Exception();
        }
    }
}
