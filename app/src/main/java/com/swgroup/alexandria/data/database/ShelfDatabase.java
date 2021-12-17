package com.swgroup.alexandria.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                    //.fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return Instance;
    }

    // TODO: Eventualmente rimuovere questo metodo, serve solo per testare il database popolandolo con valaori predefiniti
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(Instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ShelfDAO shelfDAO;

        private PopulateDBAsyncTask(ShelfDatabase database) {
            shelfDAO = database.shelfDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            shelfDAO.insertEntry((new ShelfEntry("Neuromante", "Gibson", "Distopico")));
            shelfDAO.insertEntry((new ShelfEntry("Blade Runner", "Dick", "Distopico")));
            shelfDAO.insertEntry((new ShelfEntry("1984", "Orwell", "Distopico")));
            shelfDAO.insertEntry((new ShelfEntry("Barone Rampante", "Calvino", "Romanzo")));
            shelfDAO.insertEntry((new ShelfEntry("Nome della Rosa", "Eco", "Romanzo")));

            return null;
        }
    }
}
