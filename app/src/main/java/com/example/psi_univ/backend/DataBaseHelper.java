package com.example.psi_univ.backend;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String COLUMN_BUILDING = "building";
    public static final String COLUMN_FLOOR = "floor";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_START_EVENT = "startEvent";
    public static final String COLUMN_END_EVENT = "endEvent";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_NAME = "name";
    public static final String ROOMS_TABLE = "rooms";
    public static final String EVENTS_TABLE = "events";
    public static final String COLUMN_TIME = "time";
    public static final String TIME_TABLE = COLUMN_TIME;
    public static final String COLUMN_ID = "id";

    private final Context context;

    private static final SimpleDateFormat dataBaseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DataBaseHelper(@Nullable Context context) {
        super(context, "psi-univ.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatementROOMS_TABLE = "CREATE TABLE IF NOT EXISTS " + ROOMS_TABLE + " (" +
                COLUMN_BUILDING + " varchar(64) NOT NULL," +
                COLUMN_FLOOR + " Interger NOT NULL," +
                COLUMN_NUMBER + " varchar(64) NOT NULL," +
                COLUMN_TYPE + " int(11) NOT NULL," +
                COLUMN_SIZE + " int(11) NOT NULL," +
                "  PRIMARY KEY (" + COLUMN_BUILDING + "," + COLUMN_NUMBER + ")" +
                ")";
        String createTableStatementEVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + EVENTS_TABLE + " (" +
                COLUMN_START_EVENT + " text NOT NULL," +
                COLUMN_END_EVENT + " text NOT NULL," +
                COLUMN_SUBJECT + " varchar(64) DEFAULT NULL," +
                COLUMN_BUILDING + " varchar(64) NOT NULL," +
                COLUMN_NAME + " varchar(64) NOT NULL," +
                " UNIQUE ("+COLUMN_START_EVENT+","+ COLUMN_END_EVENT +","+COLUMN_SUBJECT+","+ COLUMN_BUILDING +","+ COLUMN_NAME + ")," +
                " FOREIGN KEY ("+COLUMN_BUILDING+")" + "REFERENCES " + ROOMS_TABLE + "("+COLUMN_BUILDING+")" +
                ")";

        String createTableStatementTIME_TABLE = "CREATE TABLE IF NOT EXISTS " + TIME_TABLE  +
                "( " + COLUMN_ID + " INTEGER PRIMARY KEY CHECK (" + COLUMN_ID + " = 0)," +
                " " + COLUMN_TIME + " text)";


        db.execSQL(createTableStatementROOMS_TABLE);
        db.execSQL(createTableStatementEVENTS_TABLE);
        db.execSQL(createTableStatementTIME_TABLE);

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = dateFormat.format(new Date());
        addOneTime(db,newDate);

        Log.i("testDebut", "onCreate: ");
        Background background = new Background(this.context);
        background.execute(ROOMS_TABLE,EVENTS_TABLE);
        Log.i("testFin", "onCreate: ");
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
                Level level = new Level(buildingName, cursorFloor.getString(0), getRooms(buildingName, cursorFloor.getString(0)), context);
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
                Room room = new Room(res, buildingName, levelName);
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

    /**
     * @return List<Room> : List of all rooms
     */
    public List<Room> getAllRooms() {
        List<Building> b = getBuildings();
        List<Room> result = new ArrayList<>();
        for (Building i : b) {
            for (Level j : i.getLevelList()) {
                result.addAll(getRooms(i.getName(), j.getLevelName()));
            }
        }
        return result;
    }

    /**
     * @param buildingName : The name of the building you search
     * @param roomName     : The name of the room you search
     * @param time    : Date of the event
     * @return The event if it exists, null otherwise
     */
    public Event getEventAt(String buildingName, String roomName, Calendar time) {
        String timeString = "'" +dataBaseFormat.format(time.getTime())+ "'";
        SQLiteDatabase db = this.getReadableDatabase();

        String queryStringEvent = "SELECT " + COLUMN_START_EVENT + ", " + COLUMN_END_EVENT + ", " + COLUMN_SUBJECT + " FROM " + EVENTS_TABLE
                + " WHERE " + COLUMN_BUILDING + " = '" + buildingName + "'"
                + " AND " + COLUMN_NAME + " = '" + roomName + "'"
                + " AND " + timeString  + " >= " + COLUMN_START_EVENT
                + " AND " + timeString  + " <= "+ COLUMN_END_EVENT;

        Cursor cursorEvent = db.rawQuery(queryStringEvent, null);
        Event result;

        if (cursorEvent.moveToFirst()) {
            result = new Event(cursorEvent.getString(0), cursorEvent.getString(1), cursorEvent.getString(2));
        }else {
            result = new Event();
        }
        cursorEvent.close();

        String nextDate;
        if(!result.isEmpty()){
            nextDate = "'" + dataBaseFormat.format(result.getEnd().getTime()) + "'";
        }
        else {
            nextDate = timeString;
        }

        queryStringEvent = "SELECT " + COLUMN_START_EVENT + ", " + COLUMN_END_EVENT + ", " + COLUMN_SUBJECT + " FROM " + EVENTS_TABLE
                + " WHERE " + COLUMN_BUILDING + " = '" + buildingName + "'"
                + " AND " + COLUMN_NAME + " = '" + roomName + "'"
                + " AND " + nextDate  + " >= " + COLUMN_END_EVENT
                + " ORDER BY " + COLUMN_END_EVENT  + " ASC LIMIT 1";
        cursorEvent = db.rawQuery(queryStringEvent, null);

        if (cursorEvent.moveToFirst()) {
            result.setNext(new Event(cursorEvent.getString(0), cursorEvent.getString(1), cursorEvent.getString(2)));
        }

        cursorEvent.close();
        return result;
    }

    //passer tableau de string en paramètre
    /**
        * @param building : List of all the data for the database
     */
    public void addOneBuilding(List<String[]> building)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO `"+ROOMS_TABLE+"` (`"+COLUMN_BUILDING+"`, `"+COLUMN_NUMBER+"`, `"+COLUMN_FLOOR+"`, `"+COLUMN_TYPE+"`, `"+COLUMN_SIZE+"`) VALUES\n";

        for(int i = 0; i < building.size(); i++)
        {
            String[] buildingInfo = building.get(i);

            if(i == building.size() - 1)
            {
                query += "('"+buildingInfo[0]+"', '"+buildingInfo[1]+"', '"+buildingInfo[2]+"', "+buildingInfo[3]+", "+buildingInfo[4]+")";
            }
            else
            {
                query += "('"+buildingInfo[0]+"', '"+buildingInfo[1]+"', '"+buildingInfo[2]+"', "+buildingInfo[3]+", "+buildingInfo[4]+"),";
            }

        }
        db.execSQL(query);
    }

    //passer tableau de string en paramètre
    /**
     * @param event : List of all the data for the database
     */
    public void addOneEvent (List<String[]> event) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String query = "INSERT INTO `" + EVENTS_TABLE + "` (`" + COLUMN_START_EVENT + "`, `" + COLUMN_END_EVENT + "`, `" + COLUMN_SUBJECT + "`, `" + COLUMN_BUILDING + "`, `" + COLUMN_NAME + "`) VALUES\n";
        for(int i = 0; i < event.size(); i++) {
            String[] eventInfo = event.get(i);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date start = dateFormat.parse(eventInfo[0]);
            Date end = dateFormat.parse(eventInfo[1]);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

            if (i == event.size()-1) {

                query += "('" + dateFormat.format(start) + "', '" + dateFormat.format(end) + "', '" + eventInfo[2].replace("'"," ") + "', '" + eventInfo[3] + "', '" + eventInfo[4] + "')";
            }else{
                query += "('" + dateFormat.format(start) + "', '" + dateFormat.format(end) + "', '" + eventInfo[2].replace("'"," ") + "', '" + eventInfo[3] + "', '" + eventInfo[4] + "'),\n";
            }
        }
        db.execSQL(query);
    }

    /**
     * @param db : The database
     * @param time : The time to add
     * @return boolean : true if the time is added, false otherwise
     */
    public boolean addOneTime (SQLiteDatabase db, String time){
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID,0);
        cv.put(COLUMN_TIME, time);

        long insert = db.insert(TIME_TABLE, null, cv);
        return insert != -1;
    }

    /**
     * update table event everyday once the app is opened
     */
    public void update(){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryStringFloor = "SELECT DISTINCT " + COLUMN_TIME + " FROM " + TIME_TABLE;
        Cursor cursor = db.rawQuery(queryStringFloor,null);
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String newDate = dateFormat.format(new Date());
        String previousDate ="";
        if(cursor.moveToFirst()) {
            previousDate = cursor.getString(0);
        }
        if(previousDate.compareTo(newDate) < 0) {
            String updateTableStatementTIME_TABLE = "UPDATE " + TIME_TABLE +
                    " SET " + COLUMN_TIME + " = '" + newDate + "'";
            db.execSQL(updateTableStatementTIME_TABLE);
            String statement1 = "DROP TABLE IF EXISTS " + EVENTS_TABLE;
            db.execSQL(statement1);

            String createTableStatementEVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + EVENTS_TABLE + " (" +
                    COLUMN_START_EVENT + " /*date" + COLUMN_TIME + "*/text NOT NULL," +
                    COLUMN_END_EVENT + " /*date" + COLUMN_TIME + "*/text NOT NULL," +
                    COLUMN_SUBJECT + " varchar(64) DEFAULT NULL," +
                    COLUMN_BUILDING + " varchar(64) NOT NULL," +
                    COLUMN_NAME + " varchar(64) NOT NULL," +
                    " UNIQUE ("+COLUMN_START_EVENT+","+ COLUMN_END_EVENT +","+COLUMN_SUBJECT+","+ COLUMN_BUILDING +","+ COLUMN_NAME + ")," +
                    " FOREIGN KEY ("+COLUMN_BUILDING+")" + "REFERENCES " + ROOMS_TABLE + "("+COLUMN_BUILDING+")" +
                    ")";
            db.execSQL(createTableStatementEVENTS_TABLE);

            Background background = new Background(context);
            background.execute(EVENTS_TABLE);
        }
        cursor.close();
        db.close();
    }


    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        String statement1 = "DROP TABLE IF EXISTS " + ROOMS_TABLE;
        db.execSQL(statement1);
        String statement2 = "DROP TABLE IF EXISTS " + EVENTS_TABLE;
        db.execSQL(statement2);
    }
}
