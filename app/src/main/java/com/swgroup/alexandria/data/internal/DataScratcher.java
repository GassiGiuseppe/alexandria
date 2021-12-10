package com.swgroup.alexandria.data.internal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataScratcher {

    private static String modifyStringforMeta (String ingresso){
        // se ingresso contiene / allora si toglie
        String uscita = null;
        // example: META-INF/com.apple.ibooks.display-options.xml
        uscita = ingresso.replace("/","_");
        return uscita;
    }
    public static String getMetaDataFromEpub (String path, String zipname){
        InputStream is;
        ZipInputStream zis;
        String folderName = null;
        try {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;
            boolean firstLoop = true;
            while ((ze = zis.getNextEntry()) != null) {
                // zapis do souboru
                filename = ze.getName();
                if (firstLoop) {
                    folderName = filename;
                    firstLoop = false;
                }
                // Need to create directories if not exists, or
                // it will generate an Exception...

                //QUI STA IL PROBLEMA, MODIFICARE IL FILENAME
                System.out.println(filename);
                filename = modifyStringforMeta(filename);
                System.out.println(filename);
                //
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path + filename);

                // cteni zipu a zapis
                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return folderName;
    }
}
