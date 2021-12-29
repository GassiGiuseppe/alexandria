package com.swgroup.alexandria.ui.comic;


import com.swgroup.alexandria.data.internal.EnvDirUtil;
import com.swgroup.alexandria.data.internal.ArchiveUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class CbzParser extends AbstractCbParser implements ComicParser{

    @Override
    public void parse() throws IOException {
        File target = EnvDirUtil.getTargetDirectory("temp");
        ArchiveUtil.unzip(cbFile, target);

        File[] files = target.listFiles();
        entries.addAll(Arrays.asList(files));

        Collections.sort(entries);

    }

    @Override
    public String getType() {
        return "zip";
    }

}
