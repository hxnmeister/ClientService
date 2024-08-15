package com.example.clientservice.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.clientservice.models.Client;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
public class DBHelper extends SQLiteOpenHelper {
    private final String ID = "id";
    private final String SURNAME = "surname";
    private final String NAME = "name";
    private final String PHONE = "phone";
    private final String DATE = "date";

    @Setter
    private String tableName = "clients";

    public DBHelper(@Nullable Context context) {
        super(context, "clients.db", null, 1);
    }

    public DBHelper(@Nullable Context context, String tableName) {
        super(context, "clients.db", null, 1);

        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS %s(" +
                "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT, " +
                "%s TEXT)";

        getWritableDatabase().execSQL(String.format(query, tableName, ID, SURNAME, NAME, PHONE, DATE));

        Log.d("TAG", "table with name " + tableName + " created!");
    }

    public void dropTable() {
        String query = "DROP TABLE IF EXISTS " + tableName;

        getWritableDatabase().execSQL(query);

        Log.d("TAG", "Table " + tableName + " dropped!");
    }

    public long insert(@NonNull Client client) {
        ContentValues values = new ContentValues();

        values.put("name", client.getName());
        values.put("surname", client.getSurname());
        values.put("phone", client.getPhone());
        values.put("date", client.getDate().toString());

        Log.d("TAG", "Client " + client.getName() + " inserted!");

        return getWritableDatabase().insert("clients", null, values);
    }


    @SuppressLint("Range")
    public List<Client> findAll() {
        List<Client> clientList = new ArrayList<>();
        String query = "SELECT * FROM " + tableName;

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

    public void insertMany(@NonNull List<Client> clients) {
        for (Client client : clients) {
            insert(client);
        }
    }

    public void deleteById(int id) {
        String whereClause = ID + " = ?";

        getWritableDatabase().delete(tableName, whereClause, new String[]{ String.valueOf(id) });
    }

    public void update(@NonNull Client client) {
        ContentValues values = new ContentValues();
        String whereClause = ID + " = ?";

        values.put("name", client.getName());
        values.put("surname", client.getSurname());
        values.put("phone", client.getPhone());
        values.put("date", client.getDate().toString());

        getWritableDatabase().update(tableName, values, whereClause, new String[]{ String.valueOf(client.getId()) });
    }
}
