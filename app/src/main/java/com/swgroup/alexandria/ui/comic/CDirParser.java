package com.swgroup.alexandria.ui.comic;

import com.swgroup.alexandria.utils.NaturalOrderComparator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class CDirParser implements ComicParser {
    private ArrayList<File> entries = new ArrayList<>();

    @Override
    public void parse(File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IOException("Not a directory: " + dir.getAbsolutePath());
        }

        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : dir.listFiles())  {
                if (f.isDirectory())
                    throw new IOException("Probably not a comic directory");

                if (f.getAbsolutePath().toLowerCase().matches(".*\\.(jpg|jpeg|bmp|gif|png|webp)$"))
                    entries.add(f);
                }
            }

        Collections.sort(entries,
            new NaturalOrderComparator() {
                @Override
                public String stringValue(Object o) {
                    return ((File) o).getName();
            }
        });
    }

    @Override
    public int numPages() {
        return entries.size();
    }

    @Override
    public InputStream getPage(int num) throws IOException {
        return new FileInputStream(entries.get(num));
    }

    @Override
    public String getType() {
        return "dir";
    }

    @Override
    public void destroy() throws IOException {
    }
}
