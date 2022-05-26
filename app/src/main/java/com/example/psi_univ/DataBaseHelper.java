package com.example.psi_univ;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.psi_univ.ui.models.Building;
import com.example.psi_univ.ui.models.Level;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public static final String IMAGES_TABLE = "images";
    public static final String COLUMN_IMAGES = "images";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "psi-univ.db", null, 1);
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

        String createTableStatementIMAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + IMAGES_TABLE + "(" +
                COLUMN_BUILDING + " varchar(64) NOT NULL," +
                COLUMN_FLOOR + " varchar(64) NOT NULL," +
                COLUMN_IMAGES + " TEXT, " +
                " FOREIGN KEY (building)" + "REFERENCES " + ROOMS_TABLE + "(building)" +
                ")";

        db.execSQL(createTableStatementROOMS_TABLE);
        db.execSQL(createTableStatementEVENTS_TABLE);
        db.execSQL(createTableStatementIMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOneBuilding(BuildingDB buildingDB)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BUILDING, buildingDB.getBuilding());
        cv.put(COLUMN_FLOOR, buildingDB.getFloor());
        cv.put(COLUMN_NUMBER, buildingDB.getRoom());
        cv.put(COLUMN_TYPE, buildingDB.getType());
        cv.put(COLUMN_SIZE, buildingDB.getCapacity());
        cv.put(COLUMN_INFOS, buildingDB.getInfo());


        long insert = db.insert(ROOMS_TABLE, null, cv);
        return insert != -1;
    }

    public boolean addOneEvent(EventPSI eventPSI, BuildingDB buildingDB)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String start = dateFormat.format(eventPSI.getStartTime());
        String end = dateFormat.format(eventPSI.getEndTime());

        cv.put(COLUMN_START_EVENT,start);
        cv.put(COLUMN_END_EVENT,end);
        cv.put(COLUMN_SUBJECT, eventPSI.getSubject());
        cv.put(COLUMN_BUILDING, buildingDB.getBuilding());
        cv.put(COLUMN_NAME, buildingDB.getRoom());

        long insert = db.insert(EVENTS_TABLE, null, cv);
        return insert != -1;

    }

    public boolean addOneImage (BuildingDB buildingDB, String text){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BUILDING, buildingDB.getBuilding());
        cv.put(COLUMN_FLOOR, buildingDB.getFloor());
        cv.put(COLUMN_IMAGES, text);


        long insert = db.insert(IMAGES_TABLE, null, cv);
        return insert != -1;
    }

    public List<EventPSI> getEvent() throws ParseException {

        List<EventPSI> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + EVENTS_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            do {
                String eventStart = cursor.getString(0);
                String eventEnd = cursor.getString(1);
                String eventSubject = cursor.getString(2);
                String eventBuilding = cursor.getString(3);
                String eventRoom = cursor.getString(4);

                EventPSI eventPSI = new EventPSI();
                @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date start = dateFormat.parse(eventStart);
                Date end = dateFormat.parse(eventEnd);
                eventPSI.setStartTime(start);
                eventPSI.setEndTime(end);
                eventPSI.setSubject(eventSubject);
                returnList.add(eventPSI);

            }while (cursor.moveToNext());


        }
        else {}

        cursor.close();
        db.close();


        return returnList;
    }

    public List<Building> getBuildings() {
        List<Building> returnList = new ArrayList<>();

        String queryStringBuilding = "SELECT DISTINCT " + COLUMN_BUILDING + " FROM " + ROOMS_TABLE
                + " ORDER BY " + COLUMN_BUILDING + " ASC";

        String queryStringFloor;
        String queryStringRoom;
        String queryStringMap;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorBuilding = db.rawQuery(queryStringBuilding,null);
        Cursor cursorFloor = null;
        Cursor cursorMap = null;
        Cursor cursorRoom = null;

        if (cursorBuilding.moveToFirst()) {
            do { //fetch building
                Building building = new Building(cursorBuilding.getString(0),null );


                queryStringFloor = "SELECT DISTINCT " + COLUMN_FLOOR + " FROM " + ROOMS_TABLE
                        + " WHERE " + COLUMN_BUILDING + " = '" + building.getName() + "'"
                        + " ORDER BY " + COLUMN_BUILDING + " ASC, "
                        + COLUMN_FLOOR + " ASC";
                cursorFloor = db.rawQuery(queryStringFloor,null);

                if (cursorFloor.moveToFirst()) {
                    do{ //fetch levels
                        Level level = new Level(cursorFloor.getString(0),null);

                        queryStringMap = "SELECT DISTINCT " + COLUMN_IMAGES + " FROM " + IMAGES_TABLE
                                + " WHERE " + COLUMN_BUILDING + " = '" + building.getName() + "'"
                                + " AND " + COLUMN_FLOOR + " = '" + level.getLevelName() + "'";
                        cursorMap = db.rawQuery(queryStringMap,null);

                        if (cursorMap.moveToFirst()) {
                            level.setLevelMap(cursorMap.getString(0));
                        }

                        queryStringRoom = "SELECT DISTINCT " + COLUMN_NUMBER + " FROM " + ROOMS_TABLE
                                + " WHERE " + COLUMN_BUILDING + " = '" + building.getName()+"'"
                                + " AND " + COLUMN_FLOOR + " = " + level.getLevelName()
                                + " ORDER BY " + COLUMN_NUMBER + " ASC;";
                        cursorRoom = db.rawQuery(queryStringRoom,null);
                        if (cursorRoom.moveToFirst()) {
                            do { //fetch rooms
                                com.example.psi_univ.ui.models.Room room = new com.example.psi_univ.ui.models.Room(cursorRoom.getString(0), null);
                                level.addRoom(room);
                            } while (cursorRoom.moveToNext());
                        }

                        building.addLevel(level);

                    }while (cursorFloor.moveToNext());
                }
                returnList.add(building);

            }while (cursorBuilding.moveToNext());


        }
        else {}

        cursorBuilding.close();
        cursorFloor.close();
        cursorMap.close();
        cursorRoom.close();
        db.close();


        return returnList;
    }

    public List<Building> getAllBuildingsOnly() {
        List<Building> returnList = new ArrayList<>();

        String queryStringBuilding = "SELECT DISTINCT " + COLUMN_BUILDING + " FROM " + ROOMS_TABLE
                + " ORDER BY " + COLUMN_BUILDING + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorBuilding = db.rawQuery(queryStringBuilding,null);

        if (cursorBuilding.moveToFirst()) {
            do {
                Building building = new Building(cursorBuilding.getString(0), null);

                returnList.add(building);
            } while (cursorBuilding.moveToNext());
        }
        else {}

        cursorBuilding.close();
        db.close();

        return returnList;
    }

    public List<Building> getAllFloors() {
        List<Building> returnList = new ArrayList<>();

        String queryStringBuilding = "SELECT DISTINCT " + COLUMN_BUILDING + " FROM " + ROOMS_TABLE
                + " ORDER BY " + COLUMN_BUILDING + " ASC";

        String queryStringFloor;

        String queryStringRoom;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorBuilding = db.rawQuery(queryStringBuilding,null);
        Cursor cursorFloor = null;
        Cursor cursorRoom = null;

        if (cursorBuilding.moveToFirst()) {
            do {
                Building building = new Building(cursorBuilding.getString(0),null );
                Log.d("TEST",cursorBuilding.getString(0)+" "+building.getName());


                queryStringFloor = "SELECT DISTINCT " + COLUMN_FLOOR + " FROM " + ROOMS_TABLE
                        + " WHERE " + COLUMN_BUILDING + " = '" + building.getName() + "'"
                        + " ORDER BY " + COLUMN_BUILDING + " ASC, "
                        + COLUMN_FLOOR + " ASC";
                cursorFloor = db.rawQuery(queryStringFloor,null);

                if (cursorFloor.moveToFirst()) {
                    do{
                        Level level = new Level(cursorFloor.getString(0),null);

                        building.addLevel(level);

                    }while (cursorFloor.moveToNext());
                }
                returnList.add(building);

            }while (cursorBuilding.moveToNext());


        }
        else {}

        cursorBuilding.close();
        cursorFloor.close();
        db.close();


        return returnList;
    }

    public void getFloorFromBuilding(String building){

    }

    public Level getSpecificFloor(String building, String floor) {
        String queryStringRoom;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorRoom = null;

        Level returnLevel = new Level(floor,null);
        queryStringRoom = "SELECT DISTINCT " + COLUMN_NUMBER + " FROM " + ROOMS_TABLE
                + " WHERE " + COLUMN_BUILDING + " = '" + building + "'"
                + " AND " + COLUMN_FLOOR + " = " + floor
                + " ORDER BY " + COLUMN_NUMBER + " ASC;";
        cursorRoom = db.rawQuery(queryStringRoom,null);
        if (cursorRoom.moveToFirst()){
            do {
                com.example.psi_univ.ui.models.Room room = new com.example.psi_univ.ui.models.Room(cursorRoom.getString(0), null);
                returnLevel.addRoom(room);
            } while (cursorRoom.moveToNext());
        }


        cursorRoom.close();
        db.close();


        return returnLevel;
    }

    public Building getSpecificBuilding(String building) {
        String queryStringFloor;
        String queryStringRoom;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorFloor = null;
        Cursor cursorRoom = null;

        Building returnBuilding = new Building(building,null );


        queryStringFloor = "SELECT DISTINCT " + COLUMN_FLOOR + " FROM " + ROOMS_TABLE
                + " WHERE " + COLUMN_BUILDING + " = '" + building + "'"
                + " ORDER BY " + COLUMN_BUILDING + " ASC, "
                + COLUMN_FLOOR + " ASC";
        cursorFloor = db.rawQuery(queryStringFloor,null);

        if (cursorFloor.moveToFirst()) {
            do{
                Level level = new Level(cursorFloor.getString(0),null);
                queryStringRoom = "SELECT DISTINCT " + COLUMN_NUMBER + " FROM " + ROOMS_TABLE
                        + " WHERE " + COLUMN_BUILDING + " = '" + building + "'"
                        + " AND " + COLUMN_FLOOR + " = " + level.getLevelName()
                        + " ORDER BY " + COLUMN_NUMBER + " ASC;";
                cursorRoom = db.rawQuery(queryStringRoom,null);

                if (cursorRoom.moveToFirst()) {
                    do {
                        com.example.psi_univ.ui.models.Room room = new com.example.psi_univ.ui.models.Room(cursorRoom.getString(0), null);

                        level.addRoom(room);

                    } while (cursorRoom.moveToNext());
                }

                returnBuilding.addLevel(level);

            }while (cursorFloor.moveToNext());
        }
        cursorFloor.close();
        cursorRoom.close();
        db.close();


        return returnBuilding;
    }

    public String getLevelMap(String buildingName, String levelName) {


        return "";
    }
}
