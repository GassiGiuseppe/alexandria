package com.swgroup.alexandria.ui.comic;

import android.net.Uri;

import com.swgroup.alexandria.data.internal.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public abstract class AbstractCbParser implements ComicParser{
    protected File cbFile;
    protected ArrayList<File> entries = new ArrayList<>();

    @Override
    public void setFileLocation(File file) throws IOException {
        this.cbFile = file;
    }

    @Override
    public abstract void parse() throws IOException;

    @Override
    public ArrayList<Uri> getPagesUri() {
        ArrayList<Uri> pagesUri = new ArrayList<>();
        for(File e : entries)
            pagesUri.add(Uri.fromFile(e));

        return pagesUri;
    }

    @Override
    public int numPages() {
        return entries.size();
    }

    @Override
    public abstract String getType();


    @Override
    public void destroy() throws IOException {
        FileUtil.clearTempDir();
    }

    public File getFileLocation() {
        return cbFile;
    }

    public ArrayList<File> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<File> entries) {
        this.entries = entries;
    }
}