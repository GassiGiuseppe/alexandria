package com.swgroup.alexandria.data.internal;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;

import com.swgroup.alexandria.data.database.ShelfEntry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil{
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private FileUtil() {    }

    public static void destroyFile(String filePath){
        try{
            File file = new File(filePath);
            file.delete();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteBookFile(ShelfEntry shelfEntry){
        destroyFile(shelfEntry.getFile());
        if((shelfEntry.cover!=null)&&(!shelfEntry.cover.equals("ic_cover_not_found.png"))){
            destroyFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/Alexandria/"+shelfEntry.cover);
        }
    }

    public static File from(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = File.createTempFile(splitName[0], splitName[1]);
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }

    private static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }

        return new String[]{name, extension};
    }

    protected static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
        }
        System.out.println("\n\nExternal Media: readable="
                +mExternalStorageAvailable+" writable="+mExternalStorageWriteable);
    }

    public static void writeToSDFile(Context context,File inputFile){
        File dir = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"Alexandria");
        if(!checkAlexandriaDir())
            dir.mkdirs();

        File file = new File(dir, inputFile.getName());
        try {

            //COPIA IL FILE, DALLA CARTELLA SCELTA DELL'UTENTE NELLA CARTELLA ALEXANDRIA

            FileInputStream i = new FileInputStream(inputFile);
            FileOutputStream f = new FileOutputStream(file);
            copy(i,f);
            f.close();
            i.close();
            //CREARE LA CARTELLA INTERNAMENTE ALLO STORAGE DELL'APP e PASSARCI I PARAMETRI E LA FOTO DENTRO
            //System.out.println(context.getFilesDir());
            //altri metodi per costruire i file di testo e la directory e l'immagine

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nFile written to "+file);
    }

    public static boolean checkAlexandriaDir(){
        File testdir = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Alexandria","test");
        testdir.mkdir();
        if(testdir.isDirectory()){
            testdir.delete();
            System.out.println("LA CARTELLA ERA GIA' PRESENTE E NON E' STATA CREATA");
            return true;
        }else{
            System.out.println("LA CARTELLA NON ERA PRESENTE ED SARA' CREATA");

            return false;
        }
    }

    public void clearTempDir (){
        //the fastest way is deleting temp then recreating it
        //the following is the path to the directory (it does not contain the name)
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/Alexandria/";
        //the following contains the name of the directory
        String directoryName = "temp";
        //let's create a file that is an instance of the directory, in this way we can destroy it
        File directoryToDestroy = new File(path+directoryName);
        //let's destroy the directory
        directoryToDestroy.delete();
        //let's create a file that is an instance of the directory, in this way we can create it
        File directoryToCreate = new File (path+directoryName);
        //in the end the directory is created empty
        directoryToCreate.mkdir();
    }

}
