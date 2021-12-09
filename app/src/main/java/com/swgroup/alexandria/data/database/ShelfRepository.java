package com.swgroup.alexandria.data.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ShelfRepository {
    private ShelfDAO shelfDAO;
    private LiveData<List<ShelfEntry>> allEntries;


    public ShelfRepository(Application application) {
        ShelfDatabase shelfDatabase = ShelfDatabase.getInstance(application);

        shelfDAO = shelfDatabase.shelfDao();
        allEntries = shelfDAO.getAll();
    }



    public void insert(ShelfEntry entry) {
        new InsertEntry(shelfDAO).execute(entry);
    }

    public void update(ShelfEntry entry) {
        new UpdateEntry(shelfDAO).execute(entry);
    }

    public void delete(ShelfEntry entry) {
        new DeleteEntry(shelfDAO).execute(entry);
    }

    public LiveData<List<ShelfEntry>> getAll() {
        return allEntries;
    }

    public LiveData<List<ShelfEntry>> getByDataType(EntryType dataType) {
        return shelfDAO.getByDataType(dataType);
    }

    public LiveData<List<ShelfEntry>> getByTitle(String title) {
        return shelfDAO.getByTitle(title);
    }

    public LiveData<List<ShelfEntry>> getByAuthor(String author) {
        return shelfDAO.getByAuthor(author);
    }

    public LiveData<List<ShelfEntry>> getByGenre(String genre) {
        return shelfDAO.getByGenre(genre);
    }

    public LiveData<List<ShelfEntry>> getFavorites() {
        return shelfDAO.getFavorites();
    }

    public LiveData<List<ShelfEntry>> searchByTitle(String title) {
        return shelfDAO.searchByTitle(title);
    }

    public LiveData<List<ShelfEntry>> searchByAuthor(String author) {
        return shelfDAO.searchByAuthor(author);
    }

    public LiveData<List<ShelfEntry>> searchByGenre(String genre) {
        return shelfDAO.searchByGenre(genre);
    }



    private static class InsertEntry extends AsyncTask<ShelfEntry, Void, Void> {
        private ShelfDAO shelfDAO;

        private InsertEntry(ShelfDAO shelfDAO) {
            this.shelfDAO = shelfDAO;
        }

        @Override
        protected Void doInBackground(ShelfEntry ... entries) {
            shelfDAO.insertEntry(entries[0]);
            return null;
        }
    }

    private static class UpdateEntry extends AsyncTask<ShelfEntry, Void, Void>{
        private ShelfDAO shelfDAO;

        private UpdateEntry(ShelfDAO shelfDAO) {
            this.shelfDAO = shelfDAO;
        }

        @Override
        protected Void doInBackground(ShelfEntry ... entries) {
            shelfDAO.updateEntry(entries[0]);
            return null;
        }
    }

    private static class DeleteEntry extends AsyncTask<ShelfEntry, Void, Void>{
        private ShelfDAO shelfDAO;

        private DeleteEntry(ShelfDAO shelfDAO) {
            this.shelfDAO = shelfDAO;
        }

        @Override
        protected Void doInBackground(ShelfEntry ... entries) {
            shelfDAO.deleteEntry(entries[0]);
            return null;
        }
    }

}
