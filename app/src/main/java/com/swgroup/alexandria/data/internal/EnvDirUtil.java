package com.swgroup.alexandria.data.internal;

import android.os.Environment;

import java.io.File;

public class EnvDirUtil {
    private static final String path = Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            +"/Alexandria/";

    public static File getTargetDirectory(String input){
        File output;

        output = new File(path+input);
        if (!output.exists())
            output.mkdir();     // se la cartella non esiste, creala

        return output;
    }

    public static String atEnvDir(String input){
        return path + input;
    }

}
