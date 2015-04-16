package com.bali.nusadua.productmonitor.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bali.nusadua.productmonitor.model.Team;
import com.bali.nusadua.productmonitor.sqlitedb.DBHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamRepo {
    private DBHelper dbHelper;

    public TeamRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    //Delete all data in the table
    public void delete() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(Team.TABLE, null, null);
        database.close();
    }

    //Insert team records
    public long insert(Team team) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        team.setGuid(UUID.randomUUID().toString());
        contentValues.put(Team.GUID, team.getGuid());
        contentValues.put(Team.NAME, team.getName());

        //Inserting Row
        long id = db.insert(Team.TABLE, null, contentValues);
        db.close();
        return (int) id;
    }

    //Retrieve all records and populate List<Team>
    public List<Team> getAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Team.TABLE;

        List<Team> listTeam = new ArrayList<Team>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Looping through all rows and adding to list
        if(cursor.moveToFirst()){
            do {
                Team team = new Team();
                team.setId((int) cursor.getLong(cursor.getColumnIndex(Team.ID)));
                team.setGuid(cursor.getString(cursor.getColumnIndex(Team.GUID)));
                team.setName(cursor.getString(cursor.getColumnIndex(Team.NAME)));
                listTeam.add(team);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listTeam;
    }
}
