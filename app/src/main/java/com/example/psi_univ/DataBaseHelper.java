package com.example.psi_univ;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.psi_univ.ui.models.Building;
import com.example.psi_univ.ui.models.Level;
import com.example.psi_univ.ui.models.Room;

import java.util.ArrayList;
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

        db.execSQL(createTableStatementROOMS_TABLE);
        db.execSQL(createTableStatementEVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
    @return List<Building> : A list with all building, building's level, image and room's level
     */
    public List<Building> getBuildings() {
        List<Building> returnList = new ArrayList<>();

        String queryStringBuilding = "SELECT DISTINCT " + COLUMN_BUILDING + " FROM " + ROOMS_TABLE
                + " ORDER BY " + COLUMN_BUILDING + " ASC";



        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursorBuilding = db.rawQuery(queryStringBuilding,null);

        if (cursorBuilding.moveToFirst()) {
            do { //fetch building
                String res = cursorBuilding.getString(0);
                Building building = new Building(res,getLevels(res) );
                returnList.add(building);

            }while (cursorBuilding.moveToNext());


        }

        cursorBuilding.close();
        db.close();


        return returnList;
    }

    /**
        @param buildingName : The name of the building you search
        @return List<Level> : A list of all the level with their room
         */
    private List<Level> getLevels(String buildingName) {
        List<Level> returnLevels = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStringFloor = "SELECT DISTINCT " + COLUMN_FLOOR + " FROM " + ROOMS_TABLE
                + " WHERE " + COLUMN_BUILDING + " = '" + buildingName + "'"
                + " ORDER BY " + COLUMN_BUILDING + " ASC, "
                + COLUMN_FLOOR + " ASC";
        Cursor cursorFloor = db.rawQuery(queryStringFloor,null);

        if (cursorFloor.moveToFirst()) {
            do{ //fetch levels
                Level level = new Level(cursorFloor.getString(0),null);

                level.setRooms(getRooms(buildingName,level.getLevelName()));

                returnLevels.add(level);

            }while (cursorFloor.moveToNext());
        }

        cursorFloor.close();
        return  returnLevels;
    }

    /**
    @param buildingName :  Name of the building you search
    @param levelName : Name of the level's building you search
    @return List<Room> : A list of all the rooms
     */
    private List<Room> getRooms(String buildingName, String levelName) {
        List<Room> returnRooms = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String queryStringRoom = "SELECT DISTINCT " + COLUMN_NUMBER + " FROM " + ROOMS_TABLE
                + " WHERE " + COLUMN_BUILDING + " = '" + buildingName+"'"
                + " AND " + COLUMN_FLOOR + " = '" + levelName + "'"
                + " ORDER BY " + COLUMN_NUMBER + " ASC;";

        Cursor cursorRoom = db.rawQuery(queryStringRoom,null);

        if (cursorRoom.moveToFirst()) {
            do { //fetch rooms
                String res = cursorRoom.getString(0);
                Room room = new Room(res, Room.dummyEvents());//TODO replace dummyEvents()
                returnRooms.add(room);
            } while (cursorRoom.moveToNext());
        }
        cursorRoom.close();
        return returnRooms;
    }


}
