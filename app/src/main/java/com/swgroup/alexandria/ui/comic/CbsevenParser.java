package com.swgroup.alexandria.ui.comic;

import android.net.Uri;

import com.swgroup.alexandria.data.internal.ArchiveUtil;
import com.swgroup.alexandria.data.internal.EnvDirUtil;
import com.swgroup.alexandria.data.internal.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CbsevenParser implements ComicParser{
    private File cbsvnFile;
    private ArrayList<File> entries = new ArrayList<>();

    @Override
    public void setFileLocation(File file) throws IOException {
        this.cbsvnFile = file;
    }

    @Override
    public void parse() throws IOException {
        File target = EnvDirUtil.getTargetDirectory("temp");
        ArchiveUtil.unsevenzip(cbsvnFile, target);

        File[] files = target.listFiles();
        entries.addAll(Arrays.asList(files));

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
        return "zip";
    }

    @Override
    public void destroy() throws IOException {
        FileUtil.clearTempDir();
    }

}