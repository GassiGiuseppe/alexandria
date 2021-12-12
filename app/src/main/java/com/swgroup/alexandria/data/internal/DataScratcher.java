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


    private static void createFile (String fileToCreate,String path, String zipname){
        //THINGS TO DO: make file only if match whit the string
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
                // questo if è inutile per adesso
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }
                //end
                //
                //creaFile = checkIfWeNeedToCreateFile(filename,)
                // qui si mette l'if che controlla che sia il file che a noi interessa
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

        //return folderName;
        return;
    }

    public static String getMetaDataFromEpub (String path, String zipname){
        // WHAT TO DO:
        // 1) FAR CREARE SOLO I FILE CHE CI INTERESSANO
        //      cosa ci serve?
        //          content.opf, contiene questo:
        //          <reference href="Text/9788817027489_epub_cvi_r1.htm" title="Cover" type="cover" />   <-- COVER
        // 2) FARLI CREARE IN UNA CARTELLA TMP
        // 3) RECUPERARE I DATI
        // 4) DISTRUGGERE TUTTO

        String nomeInconpletoCover;
        Boolean creaFile ; // ci dice se il file verrà creato

        // first time i cancel everything execpt OEBPS_content.opf
        createFile (null, path,zipname);
        //then i get metadata from content and in the end i restarc createfile but this time i will create the jpg file
        return null;
    }
}
