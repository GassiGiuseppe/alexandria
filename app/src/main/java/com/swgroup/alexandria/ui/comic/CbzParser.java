package com.swgroup.alexandria.ui.comic;

import com.swgroup.alexandria.utils.NaturalOrderComparator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CbzParser implements  ComicParser{
    private ZipFile zipFile;
    private ArrayList<ZipEntry> entries;

    @Override
    public void parse(File file) throws IOException {
        zipFile = new ZipFile(file.getAbsolutePath());
        entries = new ArrayList<ZipEntry>();

        Enumeration<? extends ZipEntry> e = zipFile.entries();
        while (e.hasMoreElements()) {
            ZipEntry ze = e.nextElement();
            if (!ze.isDirectory() && ze.getName().toLowerCase().matches(".*\\.(jpg|jpeg|bmp|gif|png|webp)$"))
                entries.add(ze);
        }

        Collections.sort(entries,
                new NaturalOrderComparator() {
                    @Override
                    public String stringValue(Object o) {
                        return ((ZipEntry) o).getName();
                }
        });
    }

    @Override
    public int numPages() {
        return entries.size();
    }

    @Override
    public InputStream getPage(int num) throws IOException {
        return zipFile.getInputStream(entries.get(num));
    }

    @Override
    public String getType() {
        return "zip";
    }

    @Override
    public void destroy() throws IOException {
        zipFile.close();
    }

}
