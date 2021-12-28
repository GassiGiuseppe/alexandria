package com.swgroup.alexandria.data.internal;

import android.os.Environment;

import java.io.File;

public class EnvDirUtil {

    public static File getTargetDirectory (String input){
        File output = null;
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/Alexandria/";
        // creo il file per temp e se la cartella non esiste la creo
        //creo il file temp
        output = new File(path+input);
        //controllo che esista (nota bene, non ha estensione, controlla che sia una cartella)
        if (!output.exists()){
                //se non è una cartella la crea
            output.mkdir();
        }

        return output;
    }
}
