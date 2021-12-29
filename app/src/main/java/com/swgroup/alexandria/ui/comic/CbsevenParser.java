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


public class CbsevenParser extends AbstractCbParser implements ComicParser{

    @Override
    public void parse() throws IOException {
        File target = EnvDirUtil.getTargetDirectory("temp");
        ArchiveUtil.unsevenzip(cbFile, target);

        File[] files = target.listFiles();
        entries.addAll(Arrays.asList(files));

        Collections.sort(entries);

    }

    @Override
    public String getType() {
        return "7z";
    }

}