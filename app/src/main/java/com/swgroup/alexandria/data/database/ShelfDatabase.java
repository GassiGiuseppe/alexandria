package com.swgroup.alexandria.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ShelfEntry.class}, version = 1)
public abstract class ShelfDatabase extends RoomDatabase {

    /*
     * DAO Initialization
     */
    public abstract ShelfDAO shelfDao();

    /*
     * Always results in one and only one instance of ShelfDatabase
     */
    private static ShelfDatabase Instance;

    /*
     * Database name to be used
     */
    public static final String NAME = "Alexandria";

    /*
     * Loads the single instance of ShelfDatabase and if it
     * does not exists, a new database referencing the DAO
     * will be generated
     */
    public static synchronized ShelfDatabase getInstance(Context context) {
        if (Instance == null) {
            Instance = Room
                    .databaseBuilder(context.getApplicationContext(), ShelfDatabase.class, NAME)
                    .build();
        }
        return Instance;
    }
}
