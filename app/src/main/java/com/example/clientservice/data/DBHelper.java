package com.example.clientservice.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.clientservice.models.Client;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public final String ID = "id";
    public final String SURNAME = "surname";
    public final String NAME = "name";
    public final String PHONE = "phone";
    public final String DATE = "date";

    public DBHelper(@Nullable Context context) {
        super(context, "clients.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable(@NotNull String tableName) {
        String query = "CREATE TABLE IF NOT EXISTS %s(" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT)";

        query = String.format(query, tableName, ID, SURNAME, NAME, PHONE, DATE);
        getWritableDatabase().execSQL(query);

        Log.d("TAG", "table with name " + tableName + " created!");
    }

    public void dropTable(@NotNull String tableName) {
        String query = "DROP TABLE IF EXISTS " + tableName;

        getWritableDatabase().execSQL(query);

        Log.d("TAG", "Table " + tableName + " dropped!");
    }

    public void insert(@NonNull Client client, @NonNull String tableName) {
        String query = "INSERT INTO %s(%s, %s, %s, %s) " +
                "VALUES('%s', '%s', '%s', '%s')";

        query = String.format(query, tableName,
                SURNAME, NAME, PHONE, DATE,
                client.getSurname(), client.getName(), client.getPhone(), client.getDate());

        getWritableDatabase().execSQL(query);

        Log.d("TAG", "Client " + client.getName() + " inserted!");
    }


    @SuppressLint("Range")
    public List<Client> findAll(@NonNull String tableName) {
        List<Client> clientList = new ArrayList<>();
        String query = "SELECT *" +
                "FROM " + tableName;

        try(Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{})){
            while (cursor.moveToNext()) {
                clientList.add(new Client(
                        cursor.getInt(cursor.getColumnIndex(ID)),
                        cursor.getString(cursor.getColumnIndex(SURNAME)),
                        cursor.getString(cursor.getColumnIndex(NAME)),
                        cursor.getString(cursor.getColumnIndex(PHONE)),
                        Date.valueOf(cursor.getString(cursor.getColumnIndex(DATE)))
                ));
            }
        }
        catch (Exception e){
            Log.e("TAG", e.getMessage(), e);
        }

        return clientList;
    }
}
