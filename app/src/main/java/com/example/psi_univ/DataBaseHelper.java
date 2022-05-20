package com.example.psi_univ;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {


    public static final String COLUMN_BUILDING = "building";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_FLOOR = "floor";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_INFOS = "infos";
    public static final String COLUMN_START_EVENT = "startEvent";
    public static final String COLUMN_END_EVENT = "endEvent";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_NAME = "name";
    public static final String ROOMS_TABLE = "rooms";
    public static final String EVENTS_TABLE = "events";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "psi-univ.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //String createTableStatement = "CREATE TABLE " + EVENT_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_BUILDING + " TEXT, " + COLUMN_FLOOR + " INT, " + COLUMN_ROOM + " TEXT, " + COLUMN_START_EVENT + " DATE, " + COLUMN_END_EVENT + " DATE, " + COLUMN_SUBJECT + " TEXT)";
        String createTableStatementRoom = "CREATE TABLE IF NOT EXISTS " + ROOMS_TABLE + " (" +
                COLUMN_BUILDING + " varchar(64) NOT NULL," +
                COLUMN_NUMBER + " varchar(64) NOT NULL," +
                COLUMN_FLOOR + " int(11) DEFAULT NULL," +
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
        db.execSQL(createTableStatementRoom);
        db.execSQL(createTableStatementEVENTS_TABLE);
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

        cursor.close();
        db.close();


        return returnList;
    }
}
