package com.swgroup.alexandria.ui.comic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.swgroup.alexandria.utils.NaturalOrderComparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class CDirParser implements ComicParser {
    private ArrayList<File> entries = new ArrayList<>();
    private File dir;

    @Override
    public void setFileLocation(File dir) throws IOException {
        this.dir = dir;
    }

    @Override
    public void parse() throws IOException {
        if (!dir.isDirectory()) {
            throw new IOException("Not a directory: " + dir.getAbsolutePath());
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files)  {
                if (f.isDirectory())
                    throw new IOException("Probably not a comic directory");

                if (f.getAbsolutePath().toLowerCase().matches(".*\\.(jpg|jpeg|bmp|gif|png|webp)$"))
                    entries.add(f);
                }
            }

        Collections.sort(entries);

    }


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
    public String getType() {
        return "dir";
    }

    @Override
    public void destroy() throws IOException {
    }
}
