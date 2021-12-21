package com.swgroup.alexandria.data.internal;

import android.content.Context;
import android.os.Environment;

import androidx.core.os.EnvironmentCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AudioUtil {
    //metodo per popolare un arraylist di stringhe
    //datascratching del file zip
    private Context context;
    private List<File> chapterFileArrayList = new ArrayList<>();
    private int position;
    //popolating the array with the initial files and then modifying each instance of it

    public AudioUtil(String zipAudiopath, Context context) throws IOException {
        PopulateArrayList(zipAudiopath);
        context=this.context;
    }

    public List<String> getChapterTitleList(){
        List<String> stringArrayList = new ArrayList<>();
        for (File file : chapterFileArrayList) {
            stringArrayList.add(file.getName());
        }
        return stringArrayList;
    }

    public File getChapterFile(int position){
        return chapterFileArrayList.get(position);
    }

    public File getChapterFile(String filename){
        int index=0;
        for (File file : chapterFileArrayList){

        if(filename.equals(getChapterFile(index).getName())){
            return file;
        }
        index=index+1;
        }
        return null;
    }

    public File getNextChapterFile(){
        if (position==(chapterFileArrayList.size()-1)){
            return null;
        }else{
            position=position+1;
        return chapterFileArrayList.get(position);}
    }

    public File getPrevChapterFile(){
        if (position == 0){
            return null;
        }
        else{
            position=position-1;
        return chapterFileArrayList.get(position);}
    }

    private void PopulateArrayList (String audioFileName) throws IOException {
        chapterFileArrayList=UnZipToFileArrayList(audioFileName);
    }


        private static final int BUFFER_SIZE = 4096;
        private List<File> UnZipToFileArrayList(String zipFilePath) throws IOException {
            List<File> FileArrayList = new ArrayList<>();
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            int i=0;
            while (entry != null) {
                i++;
                FileArrayList.add(extractFile(zipIn, i));
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
            return FileArrayList;
        }

        private File extractFile(ZipInputStream zipIn, int i) throws IOException {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Alexandria","Chapter " + i +".mp3");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
            return file;
        }
}
