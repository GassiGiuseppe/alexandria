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

public class CDirParser extends AbstractCbParser implements ComicParser {

    @Override
    public void parse() throws IOException {
        if (!cbFile.isDirectory()) {
            throw new IOException("Not a directory: " + cbFile.getAbsolutePath());
        }

        File[] files = cbFile.listFiles();
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
    public String getType() {
        return "dir";
    }

    @Override
    public void destroy() throws IOException {
    }

}
