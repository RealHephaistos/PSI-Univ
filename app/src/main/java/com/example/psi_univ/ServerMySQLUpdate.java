package com.example.psi_univ;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ServerMySQLUpdate {

    //TODO Cette classe ne doit pas être laissé elle devra être déployé sur le serveur
    // Le .ics n'est pas au bon endroit car cette classe ne doit pas être utilisé c'est juste pour qu'elle soit sur le GIT
    public static void main(String[] args) {
        String urlBat12D = "";

        //TODO Ne pas décommenter cette ligne tant que l'on a pas confirmation que l'on peut retelecharger les .ics
        //getICS(urlBat12D, "ADECal");
        try {
            getRooms();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getICS(String url, String file_name) {
        ReadableByteChannel rbc;
        try {
            URL website = new URL(url);
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(file_name + ".ics");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            System.out.println("Fichier ICS récuperer avec succès.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void getRooms() throws Exception {

        Connection connection = null;
        Statement statement = null;
        PreparedStatement stmt = null;
        ResultSet knownRoomsResult = null;
        long timeSpend = System.currentTimeMillis();
        int toCommit = 0;
        int year, month, date, hrs, min;
        int parsingState = 0;
        long exception = 0;
        long success = 0;
        String name = null, location = null;
        String[] lieux;
        Set<String> knowRooms = new HashSet<String>();
        Set<String> unknowRooms = new HashSet<String>();

        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PSI", "root", "");
            //connection = DriverManager.getConnection("jdbc:mysql://psi-server-morvan.mysql.database.azure.com:3306/psi-univ?useSSL=true" ,"psiMorvan","riKLYi4LYySJgzw");
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            System.out.println("Connection Established");
            statement.executeUpdate("TRUNCATE TABLE `events`");
            knownRoomsResult = statement.executeQuery("SELECT `building`,`name` FROM `rooms`; ");
            boolean encore = knownRoomsResult.next();
            while (encore) {
                knowRooms.add(knownRoomsResult.getString(1) + " " + knownRoomsResult.getString(2));
                encore = knownRoomsResult.next();
            }
            stmt = connection.prepareStatement("INSERT INTO `events` values(?,?,?,?,?)");
        } catch (SQLException e) {
            System.err.println(e);
        }


        List<String> line = new ArrayList<>();
        Scanner scanner = new Scanner(new File("./ADECal.ics"), "UTF-8");
        while (scanner.hasNext()) {
            line.add(scanner.nextLine());
        }
        scanner.close();
        for (String i : line) {
            if (i.startsWith("BEGIN:VEVENT")) {
                if (parsingState != 0) parsingError(parsingState);
                parsingState = 1;
            } else if (i.startsWith("DTSTART")) {
                if (parsingState != 1) parsingError(parsingState);
                year = Integer.parseInt(i.substring(8, 12));
                month = Integer.parseInt(i.substring(12, 14));
                date = Integer.parseInt(i.substring(14, 16));
                hrs = Integer.parseInt(i.substring(17, 19));
                min = Integer.parseInt(i.substring(19, 21));
                stmt.setTimestamp(1, java.sql.Timestamp.valueOf(year + "-" + month + "-" + date + " " + hrs + ":" + min + ":00.0"));
                parsingState = 2;
            } else if (i.startsWith("DTEND")) {
                if (parsingState != 2) parsingError(parsingState);
                year = Integer.parseInt(i.substring(6, 10));
                month = Integer.parseInt(i.substring(10, 12));
                date = Integer.parseInt(i.substring(12, 14));
                hrs = Integer.parseInt(i.substring(15, 17));
                min = Integer.parseInt(i.substring(17, 19));
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(year + "-" + month + "-" + date + " " + hrs + ":" + min + ":00.0"));
                parsingState = 3;
            } else if (i.startsWith("SUMMARY")) {
                if (parsingState != 3) parsingError(parsingState);
                stmt.setString(3, i.substring(8));
                parsingState = 4;
            } else if (i.startsWith("LOCATION")) {
                if (parsingState != 4) parsingError(parsingState);
                location = i.substring(9);
                String tmp = line.get(line.indexOf(i) + 1);
                if (!tmp.startsWith("DESCRIPTION")) location = location + tmp;
                lieux = location.split("\\,");
                for (String j : lieux) {
                    String[] separation = j.split(" - ");
                    separation[0] = separation[0].trim();
                    stmt.setString(4, separation[0]);
                    if (separation.length > 1 && separation[1].contains(" ("))
                        name = separation[1].substring(0, separation[1].indexOf("(") - 1).trim();
                    else name = "";
                    stmt.setString(5, name);
                    try {
                        if (knowRooms.contains(separation[0] + " " + name)) {
                            stmt.executeUpdate();
                            toCommit++;
                            success++;
                        } else {
                            exception++;
                            unknowRooms.add(separation[0] + " " + name + "\n");
                        }
                        if (toCommit > 500) {
                            connection.commit();
                            toCommit = 0;
                        }
                    } catch (SQLException e) {
                        System.out.println(e);
                        exception++;
                    }
                }
                parsingState = 5;
            } else if (i.startsWith("END:VEVENT")) {
                if (parsingState != 5) parsingError(parsingState);
                parsingState = 0;
            }
        }
        if (toCommit != 0) connection.commit();
        System.out.println("*** Remplissage de la table réussi en " + (System.currentTimeMillis() - timeSpend) / 1000 + " secondes ***");
        System.out.println("*** " + exception + " requêtes ont échouées ! ***");
        System.out.println("*** " + success + " requêtes ont réussies ! ***");
        System.out.println(unknowRooms);
        connection.close();

        return;
    }

    private static void parsingError(int nbError) throws Exception {
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
