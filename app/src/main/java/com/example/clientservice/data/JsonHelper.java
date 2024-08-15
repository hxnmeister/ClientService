package com.example.clientservice.data;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.clientservice.ClientsActivity;
import com.example.clientservice.models.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class JsonHelper {
    public static void saveToFileFromDB(Context context, Uri uri) {
        try {
            DBHelper dbHelper = new DBHelper(context);
            List<Client> clients = dbHelper.findAll();

            if(clients != null && clients.size() > 0) {
/*
                Gson gson = new Gson();
                String json = gson.toJson(clients);

                Log.d("TAG", "json from gson: " + json);
*/

                ContentResolver contentResolver = context.getContentResolver();
                Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
                String json = gsonBuilder.toJson(clients);
                OutputStream outputStream = contentResolver.openOutputStream(uri);

                if(outputStream != null) {
                    outputStream.write(json.getBytes());
                    outputStream.close();
                }
            }
        }
        catch (Exception e) {
            Log.e("TAG", "JsonHelper|saveToFile: ", e);
        }
    }

    public static void loadFromFileToDB(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();

        try(InputStream inputStream = contentResolver.openInputStream(uri)) {
            if(inputStream != null) {
                int charId;
                Gson gson = new Gson();
                DBHelper dbHelper = new DBHelper(context);
                StringBuilder stringBuilder = new StringBuilder();

                while ((charId = inputStream.read()) != -1) {
                    stringBuilder.append((char) charId);
                }

                List<Client> clients = gson.fromJson(stringBuilder.toString(), new TypeToken<List<Client>>(){});

                dbHelper.dropTable();
                dbHelper.createTable();
                dbHelper.insertMany(clients);

                Log.d("TAG", "loadFromFile: " + clients);
            }
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
