package com.swgroup.alexandria.data.internal;

import com.swgroup.alexandria.data.database.ShelfEntry;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataScratcher {
    private static String scrapeFromFile(File content, ShelfEntry shelfEntry){
        String autore = null;
        String titolo = null;
        String genere = "";

        // abbiamo in ingresso content.opf
        LinkedList<String> lista = new LinkedList<>();
        String output = null;
        String outputFinale= null;

        //System.out.println(content.canRead()); <-- TRUE
        try {

            Scanner myReader = new Scanner(content);
            while (myReader.hasNextLine()) {

                 lista.add(myReader.nextLine());
              //  System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");

        }

        /// variabili per ricerca
        boolean locatefMetadata = false;
        boolean locatefManifest = false;
        // end
        // ogni elemento è una riga
        for ( String temp: lista) {
            if(temp.contains("<metadata")){
                locatefMetadata=true;
            }else if(temp.contains("</metadata")){
                locatefMetadata=false;
            }


            if(temp.contains("<manifest")){
                locatefManifest=true;
            }else if(temp.contains("</manifest")){
                locatefManifest=false;
            }

            if(locatefMetadata){
                if(temp.contains("<meta")){
                    if(temp.contains("cover")){
                        //EVVIVA, ABBIAMO TROVATO LA COVER
                        //System.out.println("EVVIVA");
                        //System.out.println(temp);

                        String [] parts = temp.split("\"");
                        String cover1 = parts[1];
                        String cover2 = parts[3];
                        //System.out.println(cover1);
                        //System.out.println(cover2);
                        if(cover1.equals("cover")){
                            output = cover2;
                        } else {
                            output = cover1;
                        }
                        //System.out.println(output);
                    }
                } else if(temp.contains("<dc")){
                    if(temp.contains("<dc:title")){
                        //scraping titolo
                        String [] parts = temp.split(">");
                        String partialAuthor = parts[1];
                        String [] parts1 = partialAuthor.split("<");
                        titolo = parts1[0];
                    }else if(temp.contains("<dc:creator")){
                        //scraping autore
                        String [] parts = temp.split(">");
                        String partialAuthor = parts[1];
                        String [] parts1 = partialAuthor.split("<");
                        autore = parts1[0];
                    }else if(temp.contains("<dc:subject")){
                        //scraping genere
                        if(genere.equals("")){
                            String [] parts = temp.split(">");
                            String partialAuthor = parts[1];
                            String [] parts1 = partialAuthor.split("<");
                            genere = parts1[0];
                        }
                    }
                }
            }

            if(locatefManifest){
                if (output!=null) {
                    if (temp.contains(output)) {
                        String[] parts = temp.split(" ");
                        for (String parola : parts) {
                            if (parola.contains("href")) {
                                String[] sottoparte = temp.split("\"");
                                outputFinale = sottoparte[1];
                            }
                        }
                    }
                }
            }
            //System.out.println(temp);
        }

        //System.out.println(autore+"\n"+titolo+"\n"+genere);
        shelfEntry.setAuthor(autore);
        shelfEntry.setTitle(titolo);
        shelfEntry.setGenre(genere);

        return outputFinale;
    }

    private static String modifyStringforMeta (String ingresso){
        // se ingresso contiene / allora si toglie
        // example: META-INF/com.apple.ibooks.display-options.xml
        return ingresso.replace("/","_");
    }

    private static Boolean checkIfCreate(String nameWanted, String fileToCreate){
        boolean risultato = false;
        // se fileToCreate contiene nameWnted allora risultato true
        if (fileToCreate.contains(nameWanted)){
            risultato=true;
        }
        return risultato;
    }

    private static File createFile (String nameWanted,String path, String zipname){
        //THINGS TO DO: make file only if match whit the string
        InputStream is;
        ZipInputStream zis;
        File outputFile = null;
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
                /*if (firstLoop) {
                    firstLoop = false;
                }*/
                // Need to create directories if not exists, or
                // it will generate an Exception...

                //QUI STA IL PROBLEMA, MODIFICARE IL FILENAME
                //System.out.println(filename);
                filename = modifyStringforMeta(filename);
                //System.out.println(filename);
                // questo if è inutile per adesso
                /*
                if (ze.isDirectory()) {
                    File fmd = new File(path + filename);
                    fmd.mkdirs();
                    continue;
                }*/
                //creaFile = checkIfWeNeedToCreateFile(filename,)
                // qui si mette l'if che controlla che sia il file che a noi interessa
                //System.out.println("un file è passato di qui");
                if(checkIfCreate(nameWanted,filename)) {
                    //System.out.println("YAYYY TI STO CREANDO");
                    FileOutputStream fout = new FileOutputStream(path + filename);

                    while ((count = zis.read(buffer)) != -1) {
                        fout.write(buffer, 0, count);
                    }

                    fout.close();
                    outputFile = new File(path+filename);
                    zis.closeEntry();
                    zis.close();
                    return outputFile;
                }
                zis.closeEntry();
            }

            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //return folderName;
        return outputFile;
    }

    public static void getMetaDataFromEpub (String path, String zipname, ShelfEntry shelfEntry){
        String conteiner = ".opf";
        // first time i cancel everything execpt OEBPS_content.opf
        File content = createFile (conteiner, path,zipname);
        //then i get metadata from content and in the end i restarc createfile but this time i will create the jpg file
        String coverName = scrapeFromFile(content,shelfEntry);


        //System.out.println(coverName);
        if(coverName!=null) {
            coverName = modifyStringforMeta(coverName);
            //System.out.println(coverName);
            File cover = createFile(coverName, path, zipname);

            String[] parts = coverName.split("\\.");
            String pathname = path + shelfEntry.title + "Cover" + "." + parts[1];
            //System.out.println(pathname);
            cover.renameTo(new File(pathname));
            shelfEntry.setCover(shelfEntry.title+"Cover"+"."+parts[1]);
        }else{
            shelfEntry.setCover("ic_cover_not_found.png");
        }
        //cover.ren
        // destroy container
        content.delete();
        //rename cover as titolo+cover
        //rename epub as title
        File copyOfEpub = new File(path + zipname);
        String[] parts1 = zipname.split("\\.");
        if(shelfEntry.title!=null) {
            copyOfEpub.renameTo(new File(path + shelfEntry.title + "." + parts1[1]));
            shelfEntry.setFile(path + shelfEntry.title + "." + parts1[1]);
        }else{
            shelfEntry.title=parts1[0];
            shelfEntry.setFile(path+zipname);
        }
        // IN THE END WE HAVE CREATED ONLY THOSE TWO FILE AND WE HAVE THE POINTERS
    }
}
