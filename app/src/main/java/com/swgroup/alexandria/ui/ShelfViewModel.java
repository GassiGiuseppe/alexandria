package com.swgroup.alexandria.ui;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.swgroup.alexandria.data.database.EntryType;
import com.swgroup.alexandria.data.database.ShelfEntry;
import com.swgroup.alexandria.data.database.ShelfRepository;

import java.util.List;

public class ShelfViewModel extends AndroidViewModel {
    private ShelfRepository repository;
    private LiveData<List<ShelfEntry>> allEntries;

    public ShelfViewModel(Application application) {
        super(application);
        repository = new ShelfRepository(application);
        allEntries = repository.getAll();
    }

    public void insert(ShelfEntry entry) {
        repository.insert(entry);
    }

    public void update(ShelfEntry entry) {
        repository.update(entry);
    }

    public void delete(ShelfEntry entry) {
        repository.delete(entry);
    }

    public LiveData<List<ShelfEntry>> getAllEntries() {
        return allEntries;
    }

    public LiveData<List<ShelfEntry>> getByDataType(EntryType type) {return repository.getByDataType(type);}

    public LiveData<List<ShelfEntry>> getByTitle(String title) {return repository.getByTitle(title);}

    public LiveData<List<ShelfEntry>> getByAuthor(String author) {return repository.getByAuthor(author); }

    public LiveData<List<ShelfEntry>> getByGenre(String genre) {return repository.getByGenre(genre);}

    public LiveData<List<ShelfEntry>> getFavorites() {return repository.getFavorites();}

    public LiveData<List<ShelfEntry>> searchByTitle(String title) {return repository.searchByTitle(title);}

    public LiveData<List<ShelfEntry>> searchByAuthor(String author) {return  repository.searchByAuthor(author);}

    public LiveData<List<ShelfEntry>> searchByGenre(String genre) {return repository.searchByGenre(genre);}

}

