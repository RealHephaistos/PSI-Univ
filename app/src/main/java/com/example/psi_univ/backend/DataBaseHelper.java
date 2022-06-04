package com.example.psi_univ.backend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.psi_univ.models.Building;
import com.example.psi_univ.models.Event;
import com.example.psi_univ.models.Level;
import com.example.psi_univ.models.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_BUILDING = "building";
    public static final String COLUMN_FLOOR = "floor";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_INFOS = "infos";
    public static final String COLUMN_START_EVENT = "startEvent";
    public static final String COLUMN_END_EVENT = "endEvent";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_NAME = "name";
    public static final String ROOMS_TABLE = "rooms";
    public static final String EVENTS_TABLE = "events";

    private final Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "psi-univ.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String createTableStatement = "CREATE TABLE " + EVENT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_BUILDING + " TEXT, " + COLUMN_FLOOR + " INT, " + COLUMN_ROOM + " TEXT, " + COLUMN_START_EVENT + " DATE, " + COLUMN_END_EVENT + " DATE, " + COLUMN_SUBJECT + " TEXT)";
        String createTableStatementROOMS_TABLE = "CREATE TABLE IF NOT EXISTS " + ROOMS_TABLE + " (" +
                COLUMN_BUILDING + " varchar(64) NOT NULL," +
                COLUMN_FLOOR + " varchar(64) NOT NULL," +
                COLUMN_NUMBER + " varchar(64) NOT NULL," +
                COLUMN_TYPE + " int(11) NOT NULL," +
                COLUMN_SIZE + " int(11) NOT NULL," +
                COLUMN_INFOS + " text," +
                "  PRIMARY KEY (" + COLUMN_BUILDING + "," + COLUMN_NUMBER + ")" +
                ")";
        String createTableStatementEVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + EVENTS_TABLE + " (" +
                COLUMN_START_EVENT + " /*datetime*/text NOT NULL," +
                COLUMN_END_EVENT + " /*datetime*/text NOT NULL," +
                COLUMN_SUBJECT + " varchar(64) DEFAULT NULL," +
                COLUMN_BUILDING + " varchar(64) NOT NULL," +
                COLUMN_NAME + " varchar(64) NOT NULL," +
                " FOREIGN KEY (building)" + "REFERENCES " + ROOMS_TABLE + "(building)" +
                ")";

        db.execSQL(createTableStatementROOMS_TABLE);
        db.execSQL(createTableStatementEVENTS_TABLE);
        intitBuildingTest(db);
        intitEventTest(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * @return List<Building> : A list with all building, building's level, image and room's level
     */
    public List<Building> getBuildings() {
        List<Building> returnList = new ArrayList<>();

        String queryStringBuilding = "SELECT DISTINCT " + COLUMN_BUILDING + " FROM " + ROOMS_TABLE
                + " ORDER BY " + COLUMN_BUILDING + " ASC";


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorBuilding = db.rawQuery(queryStringBuilding, null);

        if (cursorBuilding.moveToFirst()) {
            do { //fetch building
                String res = cursorBuilding.getString(0);
                Building building = new Building(res, getLevels(res));
                returnList.add(building);

            } while (cursorBuilding.moveToNext());


        }

        cursorBuilding.close();
        db.close();


        return returnList;
    }

    /**
     * @param buildingName : The name of the building you search
     * @return List<Level> : A list of all the level with their room
     */
    private List<Level> getLevels(String buildingName) {
        List<Level> returnLevels = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStringFloor = "SELECT DISTINCT " + COLUMN_FLOOR + " FROM " + ROOMS_TABLE
                + " WHERE " + COLUMN_BUILDING + " = '" + buildingName + "'"
                + " ORDER BY " + COLUMN_BUILDING + " ASC, "
                + COLUMN_FLOOR + " ASC";
        Cursor cursorFloor = db.rawQuery(queryStringFloor, null);

        if (cursorFloor.moveToFirst()) {
            do { //fetch levels
                Level level = new Level(buildingName, cursorFloor.getString(0), new ArrayList<>(), context); //TODO: mets pas null c'est caca meme new ArrayList() c'est caca

                level.setRooms(getRooms(buildingName, level.getLevelName()));

                returnLevels.add(level);

            } while (cursorFloor.moveToNext());
        }

        cursorFloor.close();
        return returnLevels;
    }

    /**
     * @param buildingName :  Name of the building you search
     * @param levelName    : Name of the level's building you search
     * @return List<Room> : A list of all the rooms
     */
    private List<Room> getRooms(String buildingName, String levelName) {
        List<Room> returnRooms = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStringRoom = "SELECT DISTINCT " + COLUMN_NUMBER + " FROM " + ROOMS_TABLE
                + " WHERE " + COLUMN_BUILDING + " = '" + buildingName + "'"
                + " AND " + COLUMN_FLOOR + " = '" + levelName + "'"
                + " ORDER BY " + COLUMN_NUMBER + " ASC;";

        Cursor cursorRoom = db.rawQuery(queryStringRoom, null);

        if (cursorRoom.moveToFirst()) {
            do { //fetch rooms
                String res = cursorRoom.getString(0);
                Room room = new Room(res, buildingName, levelName, new ArrayList<>()); //TODO: pareil pas null toute les rooms doivent avoir des listEvevnts
                returnRooms.add(room);
            } while (cursorRoom.moveToNext());
        }
        cursorRoom.close();
        return returnRooms;
    }

    /**
     * @param buildingName : The name of the searched building
     * @return building : Building whith all levels and rooms
     */
    public Building getBuilding(String buildingName) {
        return new Building(buildingName, getLevels(buildingName));
    }

    public void intitBuildingTest(SQLiteDatabase db) {
        String query = "INSERT INTO `" + ROOMS_TABLE + "` (`" + COLUMN_BUILDING + "`, `" + COLUMN_NUMBER + "`, `" + COLUMN_FLOOR + "`, `" + COLUMN_TYPE + "`, `" + COLUMN_SIZE + "`) VALUES\n" +
                "('B12D', 'Amphi P', 0, 2, 200),\n" +
                "('B12D', 'i-202 Prioritaire M2 Salle multimédia', 2, 5, 40),\n" +
                "('B12D', 'i-203 TP Spéciaux', 2, 3, 24),\n" +
                "('B12D', 'i-204 TP Spéciaux', 2, 3, 22),\n" +
                "('B12D', 'i-205 TP Réseaux', 2, 4, 18),\n" +
                "('B12D', 'i-206 TP Spéciaux', 2, 3, 22),\n" +
                "('B12D', 'i-207 TP Réseaux', 2, 4, 24),\n" +
                "('B12D', 'i-210 Prioritaire M2', 2, 5, 24),\n" +
                "('B12D', 'i-214 Prioritaire M2', 2, 5, 24),\n" +
                "('B12D', 'i-50', 0, 0, 64),\n" +
                "('B12D', 'i-51', 0, 0, 64),\n" +
                "('B12D', 'i-52', 0, 0, 44),\n" +
                "('B12D', 'i-53', 0, 0, 44),\n" +
                "('B12D', 'i-54 Anglais', 0, 0, 20),\n" +
                "('B12D', 'i-55 Anglais', 0, 0, 26),\n" +
                "('B12D', 'i-56', 0, 0, 44),\n" +
                "('B12D', 'i-57', 0, 0, 44),\n" +
                "('B12D', 'i-58', 0, 0, 44),\n" +
                "('B12D', 'i-59', 0, 0, 44),\n" +
                "('B12D', 'i-60', 0, 0, 44),\n" +
                "('B12D', 'Salle Guernesey', -1, 6, 50),\n" +
                "('B12D', 'Salle Jersey', -1, 6, 56);\n";

        db.execSQL(query);
    }

    public void intitEventTest(SQLiteDatabase db) {
        String query = "INSERT INTO " + EVENTS_TABLE + " ('" + COLUMN_START_EVENT + "', `" + COLUMN_END_EVENT + "`, `" + COLUMN_SUBJECT + "`, `" + COLUMN_BUILDING + "`, `" + COLUMN_NAME + "`) VALUES" +
                "('2021-09-10 04:00:00', '2021-09-10 06:00:00', 'NCG', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-10 04:30:00', '2021-09-10 07:30:00', 'FOM-CM/TD G3', 'B12D', 'i-52'),\n" +
                "('2021-09-10 04:30:00', '2021-09-10 07:30:00', 'FOM-CM/TD G5', 'B12D', 'i-53'),\n" +
                "('2021-09-10 04:30:00', '2021-09-10 07:30:00', 'FOM-CM/TD G6', 'B12D', 'i-59'),\n" +
                "('2021-09-10 05:00:00', '2021-09-10 06:00:00', 'Soutenance L3 pro SETA', 'B12D', 'i-58'),\n" +
                "('2021-09-10 05:00:00', '2021-09-10 06:30:00', 'ANG Gr A', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-10 05:00:00', '2021-09-10 06:30:00', 'ANG Gr B', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-10 06:00:00', '2021-09-10 08:00:00', 'A Maths Refresher', 'B12D', 'i-58'),\n" +
                "('2021-09-10 06:00:00', '2021-09-10 08:00:00', 'CM Propa', 'B12D', 'Amphi P'),\n" +
                "('2021-09-10 06:00:00', '2021-09-10 08:00:00', 'PCP + STAGE1', 'B12D', 'i-60'),\n" +
                "('2021-09-10 06:15:00', '2021-09-10 08:15:00', 'A RSP CM', 'B12D', 'i-50'),\n" +
                "('2021-09-10 06:15:00', '2021-09-10 08:15:00', 'EVL CM', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-10 06:15:00', '2021-09-10 08:15:00', 'FSY CM', 'B12D', 'i-51'),\n" +
                "('2021-09-10 06:15:00', '2021-09-10 08:15:00', 'IDM CM (DLIS) Gr 3', 'B12D', 'i-57'),\n" +
                "('2021-09-10 06:15:00', '2021-09-10 08:15:00', 'MOB CM', 'B12D', 'i-56'),\n" +
                "('2021-09-10 06:15:00', '2021-09-10 08:15:00', 'NINF-BD', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-10 06:45:00', '2021-09-10 08:15:00', 'ANG Gr C', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-10 06:45:00', '2021-09-10 08:15:00', 'ANG Gr D', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-10 08:30:00', '2021-09-10 09:00:00', 'Réunion Projet EDUC \\\"Escape Game\\\"', 'B12D', 'i-50'),\n" +
                "('2021-09-10 09:30:00', '2021-09-10 11:00:00', 'ANG TD gr1-1', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-10 09:30:00', '2021-09-10 11:00:00', 'ANG TD gr2-1', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 10:30:00', 'Présentations équipes', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'adaptation : electronique', 'B12D', 'i-60'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'ArcSys1 Maths - CoursTD', 'B12D', 'i-59'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'CM Canal de propagation', 'B12D', 'i-52'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'CM Electronique', 'B12D', 'i-51'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'LF CM', 'B12D', 'Amphi P'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'Robotique asservissement visuel', 'B12D', 'i-50'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'TAA TP IL CCN gr.1', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'TAA TP ILA/CCNA non encadrés', 'B12D', 'i-204 TP Spéciaux'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'TAA TP ILA/CCNA non encadrés', 'B12D', 'i-206 TP Spéciaux'),\n" +
                "('2021-09-10 10:00:00', '2021-09-10 12:00:00', 'TAA TP ILA/CCNA non encadrés', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-10 10:30:00', '2021-09-10 11:00:00', 'Présentations équipes', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-10 11:15:00', '2021-09-10 12:45:00', 'ANG TD gr1-2', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-10 11:15:00', '2021-09-10 12:45:00', 'ANG TD gr2-2', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-10 12:15:00', '2021-09-10 14:15:00', 'TAA TP IL CCN gr.1', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-10 12:15:00', '2021-09-10 14:15:00', 'TAA TP ILA/CCNA non encadrés', 'B12D', 'i-204 TP Spéciaux'),\n" +
                "('2021-09-10 12:15:00', '2021-09-10 14:15:00', 'TAA TP ILA/CCNA non encadrés', 'B12D', 'i-206 TP Spéciaux'),\n" +
                "('2021-09-10 12:15:00', '2021-09-10 14:15:00', 'TAA TP ILA/CCNA non encadrés', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-10 12:15:00', '2021-09-10 14:15:00', 'Unix/C CM', 'B12D', 'Amphi P'),\n" +
                "('2021-09-10 13:00:00', '2021-09-10 14:30:00', 'ANG TD gr1-2', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-10 13:00:00', '2021-09-10 14:30:00', 'ANG TD gr2-2', 'B12D', 'i-53'),\n" +
                "('2021-09-10 13:00:00', '2021-09-10 14:30:00', 'ANG TD gr2-2', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-13 04:00:00', '2021-09-13 06:00:00', 'CM Canal de propagation', 'B12D', 'i-52'),\n" +
                "('2021-09-13 04:00:00', '2021-09-13 06:00:00', 'CM Electronique des systèmes', 'B12D', 'i-51'),\n" +
                "('2021-09-13 04:00:00', '2021-09-13 06:00:00', 'GLAR  CM', 'B12D', 'i-58'),\n" +
                "('2021-09-13 04:00:00', '2021-09-13 06:00:00', 'MN.ARC1 CM/TD (étudiant.e.s pour mise à niveau)', 'B12D', 'i-50'),\n" +
                "('2021-09-13 04:00:00', '2021-09-13 06:00:00', 'NCG', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-13 06:15:00', '2021-09-13 08:15:00', 'CM CF Maths 1', 'B12D', 'i-52'),\n" +
                "('2021-09-13 06:15:00', '2021-09-13 08:15:00', 'CM Circuits RF & Antennes', 'B12D', 'i-57'),\n" +
                "('2021-09-13 06:15:00', '2021-09-13 08:15:00', 'EVL CM', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-13 06:15:00', '2021-09-13 08:15:00', 'MAG gr2 TD', 'B12D', 'i-51'),\n" +
                "('2021-09-13 06:15:00', '2021-09-13 08:15:00', 'MAT3-TD6', 'B12D', 'i-58'),\n" +
                "('2021-09-13 06:15:00', '2021-09-13 08:15:00', 'PRG1 gr3 TD', 'B12D', 'i-59'),\n" +
                "('2021-09-13 06:15:00', '2021-09-13 08:15:00', 'Rappels Probabilités CM/TD', 'B12D', 'i-50'),\n" +
                "('2021-09-13 06:15:00', '2021-09-13 08:15:00', 'SIN CM', 'B12D', 'Amphi P'),\n" +
                "('2021-09-13 06:15:00', '2021-09-13 08:15:00', 'TD Conception numérique G1', 'B12D', 'i-53'),\n" +
                "('2021-09-13 09:00:00', '2021-09-13 12:00:00', 'FOM-CM/TD G2', 'B12D', 'i-56'),\n" +
                "('2021-09-13 09:00:00', '2021-09-13 12:00:00', 'FOM-CM/TD G5', 'B12D', 'i-60'),\n" +
                "('2021-09-13 09:30:00', '2021-09-13 10:00:00', 'réunion du lundi signatures', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-13 09:30:00', '2021-09-13 11:30:00', 'soutenance stage M2 SE', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-13 09:45:00', '2021-09-13 11:45:00', 'CM CF Maths 2', 'B12D', 'i-50'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'GLA CM', 'B12D', 'i-58'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'GTA', 'B12D', 'i-53'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'MAG gr3 TD', 'B12D', 'i-59'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'Maths for Security CM/TD', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'MOB CM', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'NMAT', 'B12D', 'Amphi P'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'PRG1 gr1 TD', 'B12D', 'i-52'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'PRG1 gr2 TD', 'B12D', 'i-51'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'RO CM', 'B12D', 'i-57'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'TAA TP IL CCN gr.2', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-13 10:00:00', '2021-09-13 12:00:00', 'TAA TP ILA/CCNA', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-13 12:00:00', '2021-09-13 14:00:00', 'FST1 CM EIT', 'B12D', 'i-59'),\n" +
                "('2021-09-13 12:15:00', '2021-09-13 13:45:00', 'PRR-2', 'B12D', 'Amphi P'),\n" +
                "('2021-09-13 12:15:00', '2021-09-13 14:15:00', 'A Méthodologie présentations scientifiques', 'B12D', 'i-57'),\n" +
                "('2021-09-13 12:15:00', '2021-09-13 14:15:00', 'ACO TD - Cloud et Réseaux-CNI', 'B12D', 'i-50'),\n" +
                "('2021-09-13 12:15:00', '2021-09-13 14:15:00', 'adaptation : electronique', 'B12D', 'i-60'),\n" +
                "('2021-09-13 12:15:00', '2021-09-13 14:15:00', 'CM Systèmes Dynamiques', 'B12D', 'i-51'),\n" +
                "('2021-09-13 12:15:00', '2021-09-13 14:15:00', 'GLA CM', 'B12D', 'i-58'),\n" +
                "('2021-09-13 12:15:00', '2021-09-13 14:15:00', 'TAA TP IL CCN gr.2', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-13 12:15:00', '2021-09-13 14:15:00', 'TAA TP ILA/CCNA', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-13 12:15:00', '2021-09-13 14:15:00', 'TD Conception numérique G2', 'B12D', 'i-53'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'ADMI CM', 'B12D', 'i-57'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'AOC CM (DLIS) Gr 3', 'B12D', 'i-59'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'FSY CM', 'B12D', 'i-50'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'GLA CM', 'B12D', 'i-58'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'OML TD (M1 CCN)', 'B12D', 'i-52'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'PRG1 gr1 TD', 'B12D', 'i-51'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'PRG1 gr3 TD', 'B12D', 'i-56'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'TAA TP IL CCN gr.1', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'TAA TP ILA/CCNA non encadrés', 'B12D', 'i-206 TP Spéciaux'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'TAA TP ILA/CCNA non encadrés', 'B12D', 'i-210 Prioritaire M2'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 06:00:00', 'Unix C gr2 TD', 'B12D', 'i-53'),\n" +
                "('2021-09-14 04:00:00', '2021-09-14 08:00:00', 'CG CM', 'B12D', 'Amphi P'),\n" +
                "('2021-09-14 04:30:00', '2021-09-14 06:00:00', 'TD Anglais  G I', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 07:45:00', 'Réunion SOIE G2', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 07:45:00', 'TD Anglais  G II', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'Algo - Agreg', 'B12D', 'i-60'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'Basic Cryptography CM/TD', 'B12D', 'i-204 TP Spéciaux'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'Basic Cryptography CM/TD', 'B12D', 'i-51'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'CG', 'B12D', 'i-57'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'INV 1 CM (BDDA).', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'IPRG CM (M1 CCN)', 'B12D', 'i-58'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'L2IE_AD-TD', 'B12D', 'i-50'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'MAG gr1 TD', 'B12D', 'i-52'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'option ARR CM', 'B12D', 'i-59'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'PRG1 gr2 TD', 'B12D', 'i-53'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'TAA TP IL CCN gr.1', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'TAA TP ILA/CCNA non encadrés', 'B12D', 'i-210 Prioritaire M2'),\n" +
                "('2021-09-14 06:15:00', '2021-09-14 08:15:00', 'Unix C gr3 TD', 'B12D', 'i-56'),\n" +
                "('2021-09-14 10:00:00', '2021-09-14 12:00:00', 'Algo - Agreg', 'B12D', 'i-60'),\n" +
                "('2021-09-14 10:00:00', '2021-09-14 12:00:00', 'CG TD gr3', 'B12D', 'i-56'),\n" +
                "('2021-09-14 10:00:00', '2021-09-14 12:00:00', 'CM Outil informatique', 'B12D', 'Amphi P'),\n" +
                "('2021-09-14 10:00:00', '2021-09-14 12:00:00', 'EX TP grB', 'B12D', 'i-53'),\n" +
                "('2021-09-14 10:00:00', '2021-09-14 12:00:00', 'PRG1 TD gr2', 'B12D', 'i-57'),\n" +
                "('2021-09-14 12:15:00', '2021-09-14 14:15:00', 'EX TP grA', 'B12D', 'i-53'),\n" +
                "('2021-09-14 12:15:00', '2021-09-14 14:15:00', 'MN.ARC1 CM/TD (étudiant.e.s pour mise à niveau)', 'B12D', 'i-50'),\n" +
                "('2021-09-14 12:15:00', '2021-09-14 14:15:00', 'PRG1 TD gr2', 'B12D', 'i-57'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'A ST CM', 'B12D', 'i-59'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'CG TD 2', 'B12D', 'i-52'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'CM Network Security', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'EVL CM', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'EX TP grD', 'B12D', 'i-53'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'M12 - Anglais P1', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'OML TD (M1 CCN)', 'B12D', 'i-60'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'PRG1 TD gr1', 'B12D', 'i-51'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'PRG1 TD gr3', 'B12D', 'i-56'),\n" +
                "('2021-09-15 04:00:00', '2021-09-15 06:00:00', 'PRM 2 CM', 'B12D', 'Amphi P'),\n" +
                "('2021-09-15 04:30:00', '2021-09-15 07:30:00', 'FOM-CM/TD G1', 'B12D', 'i-57'),\n" +
                "('2021-09-15 04:30:00', '2021-09-15 07:30:00', 'FOM-CM/TD G4', 'B12D', 'i-58'),\n" +
                "('2021-09-15 04:30:00', '2021-09-15 07:30:00', 'FOM-CM/TD G6', 'B12D', 'i-50'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'A CM  Com. Num', 'B12D', 'Amphi P'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'ADMI TP', 'B12D', 'i-207 TP Réseaux'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'ANG M2 Miage Gr3', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'ArcSys1 Info - CM', 'B12D', 'i-60'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'BCS CM', 'B12D', 'i-52'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'CM Circuits RF & Antennes', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'EDD 1 TP (Gr 2)', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'EX TP grC', 'B12D', 'i-53'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'IPRG CM (M1 CCN)', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'Low Level Programming CM', 'B12D', 'i-59'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'PRG1 TD gr1', 'B12D', 'i-51'),\n" +
                "('2021-09-15 06:15:00', '2021-09-15 08:15:00', 'PRG1 TD gr3', 'B12D', 'i-56'),\n" +
                "('2021-09-15 09:00:00', '2021-09-15 12:00:00', 'FOM-CM/TD G3', 'B12D', 'i-57'),\n" +
                "('2021-09-15 09:00:00', '2021-09-15 12:00:00', 'FOM-CM/TD G7', 'B12D', 'i-51'),\n" +
                "('2021-09-15 09:00:00', '2021-09-15 12:00:00', 'FOM-CM/TD G8', 'B12D', 'i-60'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'ANG M2 Miage Gr2', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'CG TD gr2', 'B12D', 'i-56'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'CM Mod Analog', 'B12D', 'i-50'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'EDD 1 TP (Gr 1)', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'EX TP grF', 'B12D', 'i-53'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'FST1 CM EIT', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'GLA CM', 'B12D', 'i-52'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'GLAR  CM', 'B12D', 'i-58'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'IDM CM (DLIS) Gr 3', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'PDL CM', 'B12D', 'i-59'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 12:00:00', 'Unix/C CM', 'B12D', 'Amphi P'),\n" +
                "('2021-09-15 10:00:00', '2021-09-15 13:00:00', 'GIT', 'B12D', 'i-210 Prioritaire M2'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 13:30:00', 'Séminaire', 'B12D', 'i-51'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 13:45:00', 'CM Phy. Composants', 'B12D', 'i-50'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 13:45:00', 'PRR-4', 'B12D', 'Amphi P'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 14:15:00', 'ACO TD 2', 'B12D', 'i-60'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 14:15:00', 'ANG M2 Miage Gr1', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 14:15:00', 'AOC TD (DLIS) Gr 3', 'B12D', 'i-56'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 14:15:00', 'CG TD gr1', 'B12D', 'i-57'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 14:15:00', 'EX TP grE', 'B12D', 'i-53'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 14:15:00', 'GLA CM', 'B12D', 'i-58'),\n" +
                "('2021-09-15 12:15:00', '2021-09-15 14:15:00', 'Unix C gr1 TD', 'B12D', 'i-52'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'A TD Network Security Cyber', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'ANG TD', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'ANG TD', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'CM Signal', 'B12D', 'i-60'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'IDE  1 CM', 'B12D', 'Amphi P'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'MAT3-TD3', 'B12D', 'i-57'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'NINF-BD', 'B12D', 'i-50'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'OFI-TD1', 'B12D', 'i-56'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'TAA TP IL CCN non encadrés', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'TAA TP IL CCN non encadrés', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-16 04:00:00', '2021-09-16 06:00:00', 'TAA TP ILA/CCNA', 'B12D', 'i-210 Prioritaire M2'),\n" +
                "('2021-09-16 04:30:00', '2021-09-16 07:30:00', 'FOM-CM/TD G2', 'B12D', 'i-52'),\n" +
                "('2021-09-16 04:30:00', '2021-09-16 07:30:00', 'FOM-CM/TD G4', 'B12D', 'i-53'),\n" +
                "('2021-09-16 04:30:00', '2021-09-16 07:30:00', 'FOM-CM/TD G7', 'B12D', 'i-58'),\n" +
                "('2021-09-16 04:45:00', '2021-09-16 06:00:00', 'Présentations équipes', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'A PDS CM', 'B12D', 'Amphi P'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'Algo Distribuée (Réseau)', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'ANG gr1', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'ANG gr2', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'BCS CM', 'B12D', 'i-59'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'CM System Security', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'ISYS Codage CM (M1 CCN)', 'B12D', 'i-57'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'MAT3-TD1', 'B12D', 'i-56'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'NINF-BD', 'B12D', 'i-50'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'OFI-TD3', 'B12D', 'i-51'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'option ARR CM', 'B12D', 'i-60'),\n" +
                "('2021-09-16 06:15:00', '2021-09-16 08:15:00', 'TAA TP ILA/CCNA', 'B12D', 'i-210 Prioritaire M2'),\n" +
                "('2021-09-16 09:00:00', '2021-09-16 12:00:00', 'FOM-CM/TD G1', 'B12D', 'i-58'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'A CM TS', 'B12D', 'i-59'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'A SEL CM', 'B12D', 'i-50'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'BDD1 TD 1', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'BDD1 TD 2', 'B12D', 'i-53'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'CG', 'B12D', 'i-56'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'CM Circuits RF & Antennes', 'B12D', 'i-51'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'FSY TD EIT', 'B12D', 'i-57'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'MMM TP ILA/CCNA', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'option ARR TD', 'B12D', 'i-60'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'Prog1 - CM', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 12:00:00', 'TAA TP IL CCN gr.1', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-16 10:00:00', '2021-09-16 13:00:00', 'FOM-CM/TD G8', 'B12D', 'i-52'),\n" +
                "('2021-09-16 12:15:00', '2021-09-16 13:45:00', 'PRR-1', 'B12D', 'Amphi P'),\n" +
                "('2021-09-16 12:15:00', '2021-09-16 14:15:00', 'ACO TD - Cloud et Réseaux-CNI', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-16 12:15:00', '2021-09-16 14:15:00', 'FSY CM', 'B12D', 'i-50'),\n" +
                "('2021-09-16 12:15:00', '2021-09-16 14:15:00', 'GTA', 'B12D', 'i-56'),\n" +
                "('2021-09-16 12:15:00', '2021-09-16 14:15:00', 'Invité 1 - Sécurité CM', 'B12D', 'i-51'),\n" +
                "('2021-09-16 12:15:00', '2021-09-16 14:15:00', 'MMM TP ILA/CCNA', 'B12D', 'i-210 Prioritaire M2'),\n" +
                "('2021-09-16 12:15:00', '2021-09-16 14:15:00', 'TAA TP IL CCN gr.1', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-17 04:00:00', '2021-09-17 06:00:00', '1TD CF Math 1', 'B12D', 'i-51'),\n" +
                "('2021-09-17 04:00:00', '2021-09-17 06:00:00', 'INV 1 CM (BDDA).', 'B12D', 'Amphi P'),\n" +
                "('2021-09-17 04:00:00', '2021-09-17 06:00:00', 'IPRG CM (M1 CCN)', 'B12D', 'i-60'),\n" +
                "('2021-09-17 04:00:00', '2021-09-17 06:00:00', 'Math/Comp Algèbre', 'B12D', 'i-58'),\n" +
                "('2021-09-17 04:00:00', '2021-09-17 06:00:00', 'NCG', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-17 04:00:00', '2021-09-17 06:00:00', 'TAA TP IL CCN non encadrés', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-17 04:00:00', '2021-09-17 06:00:00', 'TAA TP IL CCN non encadrés', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-17 04:00:00', '2021-09-17 06:00:00', 'TAA TP ILA/CCNA', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-17 04:30:00', '2021-09-17 07:30:00', 'FOM-CM/TD G3', 'B12D', 'i-52'),\n" +
                "('2021-09-17 04:30:00', '2021-09-17 07:30:00', 'FOM-CM/TD G5', 'B12D', 'i-53'),\n" +
                "('2021-09-17 04:30:00', '2021-09-17 07:30:00', 'FOM-CM/TD G6', 'B12D', 'i-59'),\n" +
                "('2021-09-17 05:00:00', '2021-09-17 06:30:00', 'ANG Gr A', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-17 05:00:00', '2021-09-17 06:30:00', 'ANG Gr B', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-17 06:00:00', '2021-09-17 08:00:00', 'CM Propa', 'B12D', 'Amphi P'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'A CM MVFA', 'B12D', 'i-57'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'A RSP CM', 'B12D', 'i-50'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'AOC  TD ILA CCNA', 'B12D', 'i-58'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'CM Canal de propagation', 'B12D', 'i-56'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'EDD 1 TP (Gr 3)', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'EVL TP gr.1', 'B12D', 'i-206 TP Spéciaux'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'EVL TP gr.2', 'B12D', 'i-204 TP Spéciaux'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'FST1 CM  (BDDA)', 'B12D', 'i-51'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'ISYS Codage CM (M1 CCN)', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'MATH 1 - TD (Beaulieu)', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'MMM TP IL/CCN', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-17 06:15:00', '2021-09-17 08:15:00', 'Rappels Probabilités CM/TD', 'B12D', 'i-60'),\n" +
                "('2021-09-17 06:45:00', '2021-09-17 08:15:00', 'ANG Gr C', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-17 06:45:00', '2021-09-17 08:15:00', 'ANG Gr D', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-17 09:30:00', '2021-09-17 10:00:00', 'réunion du vendredi Marc B.', 'B12D', 'i-210 Prioritaire M2'),\n" +
                "('2021-09-17 09:30:00', '2021-09-17 11:00:00', 'ANG TD gr1-1', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-17 09:30:00', '2021-09-17 11:00:00', 'ANG TD gr2-1', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'A PDS TD M1 SIF', 'B12D', 'i-59'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'ArcSys1 Maths - CoursTD', 'B12D', 'i-51'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'BDD1 TD EIT', 'B12D', 'i-60'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'CM Electronique', 'B12D', 'Amphi P'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'EDD 1 TP (Gr EIT)', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'Fond1 - CM', 'B12D', 'i-56'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'GLA CM', 'B12D', 'i-57'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'GP1 TD 1', 'B12D', 'Salle Jersey'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'Rappels Calcul Matriciel', 'B12D', 'i-50'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'RAS', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'TAA TP IL CCN gr.2', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'TAA TP ILA/CCNA', 'B12D', 'i-204 TP Spéciaux'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 12:00:00', 'TAA TP ILA/CCNA', 'B12D', 'i-206 TP Spéciaux'),\n" +
                "('2021-09-17 10:00:00', '2021-09-17 14:00:00', 'TP RESA-S7', 'B12D', 'i-207 TP Réseaux'),\n" +
                "('2021-09-17 11:15:00', '2021-09-17 12:45:00', 'ANG TD gr1-2', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-17 11:15:00', '2021-09-17 12:45:00', 'ANG TD gr2-2', 'B12D', 'i-54 Anglais'),\n" +
                "('2021-09-17 12:15:00', '2021-09-17 14:15:00', 'DMV', 'B12D', 'Salle Guernesey'),\n" +
                "('2021-09-17 12:15:00', '2021-09-17 14:15:00', 'GLA TP', 'B12D', 'i-57'),\n" +
                "('2021-09-17 12:15:00', '2021-09-17 14:15:00', 'GP1 TD 2', 'B12D', 'i-51'),\n" +
                "('2021-09-17 12:15:00', '2021-09-17 14:15:00', 'SEP', 'B12D', 'i-50'),\n" +
                "('2021-09-17 12:15:00', '2021-09-17 14:15:00', 'TAA TP IL CCN gr.2', 'B12D', 'i-202 Prioritaire M2 Salle multimédia'),\n" +
                "('2021-09-17 12:15:00', '2021-09-17 14:15:00', 'TAA TP ILA/CCNA', 'B12D', 'i-214 Prioritaire M2'),\n" +
                "('2021-09-17 13:00:00', '2021-09-17 14:30:00', 'ANG TD gr1-2', 'B12D', 'i-55 Anglais'),\n" +
                "('2021-09-17 13:00:00', '2021-09-17 14:30:00', 'ANG TD gr2-2', 'B12D', 'i-53'),\n" +
                "('2021-09-17 13:00:00', '2021-09-17 14:30:00', 'ANG TD gr2-2', 'B12D', 'i-56')";

        db.execSQL(query);

    }

    /**
     @param buildingName : The name of the building you search
     @param roomName : The name of the room you search
     @param startTime : Date of first event (String format yyyy-MM-dd)
     @param endTime : Date of last event (String format yyyy-MM-dd)
     @return List<Event> : A list of all event in a room
     */
    public List<Event> getEvents (String buildingName, String roomName, String startTime, String endTime) throws ParseException {
        List<Event> returnEvents = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStringEvent = "SELECT "+COLUMN_START_EVENT+", "+COLUMN_END_EVENT+", "+COLUMN_SUBJECT+" FROM " + EVENTS_TABLE
                + " WHERE " + COLUMN_BUILDING + " = '" + buildingName + "'"
                + " AND " + COLUMN_NAME + " = '" + roomName + "'"
                + " AND " + COLUMN_START_EVENT + " >= '" + startTime + " 00:00:00'"
                + " AND " + COLUMN_END_EVENT + " <= '" + endTime + " 24:00:00'"
                + " ORDER BY " + COLUMN_BUILDING + " ASC, "
                + COLUMN_NAME + " ASC";
        Cursor cursorEvent = db.rawQuery(queryStringEvent,null);

        Log.i("test1","true");
        if (cursorEvent.moveToFirst()) {
            Log.i("test2","true");
            do {
                String eventStart = cursorEvent.getString(0);
                String eventEnd = cursorEvent.getString(1);
                String eventSubject = cursorEvent.getString(2);

                Calendar calendarStart = Calendar.getInstance();
                Calendar calendarEnd = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.FRANCE);
                Log.i("Temps",eventStart);
                calendarStart.setTime(dateFormat.parse(eventStart));
                calendarEnd.setTime(dateFormat.parse(eventEnd));
                Event event = new Event(calendarStart,calendarEnd,eventSubject); //TODO : revoir ça
                returnEvents.add(event);

            } while (cursorEvent.moveToNext());
        }

        cursorEvent.close();
        return returnEvents;
    }


    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        String statement1 = "DROP TABLE IF EXISTS " + ROOMS_TABLE;
        db.execSQL(statement1);
        String statement2 = "DROP TABLE IF EXISTS " + EVENTS_TABLE;
        db.execSQL(statement2);
    }
}
