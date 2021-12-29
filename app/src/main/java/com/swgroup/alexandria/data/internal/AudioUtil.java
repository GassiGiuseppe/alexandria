package com.swgroup.alexandria.data.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.swgroup.alexandria.MainActivity;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AudioUtil {
    //metodo per popolare un arraylist di stringhe
    //datascratching del file zip
    private Context context;
    private List<File> chapterFileArrayList = new ArrayList<>();
    private int position;
    private MediaMetadataRetriever name;
    private String zipAudiopath;

    //popolating the array with the initial files and then modifying each instance of it

    @RequiresApi(api = Build.VERSION_CODES.O)
    public AudioUtil(String zipAudiopath, Context context, boolean isItMain) throws IOException {
        if(!isItMain){
        PopulateArrayList(zipAudiopath);}
        zipAudiopath=this.zipAudiopath;
        context=this.context;
    }


    public List<String> getChapterTitleList(){
        List<String> stringArrayList = new ArrayList<>();
        for (File file : chapterFileArrayList) {
            stringArrayList.add(RetrieveMediaName(file));
        }
        Collections.sort(stringArrayList);
        return stringArrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public List<ImageView> getChapterImageList(){
        List<ImageView> MediaArrayList = new ArrayList<>();
        for (File file : chapterFileArrayList) {
            MediaArrayList.add(RetrieveMediaImageView(file));
        }
        return MediaArrayList;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void PopulateArrayList (String audioFileName) throws IOException {
        chapterFileArrayList=UnZipToFileArrayList(audioFileName);
        Collections.sort(chapterFileArrayList);
    }


        private static final int BUFFER_SIZE = 4096;
        @RequiresApi(api = Build.VERSION_CODES.O)
        private List<File> UnZipToFileArrayList(String zipFilePath) throws IOException {
            List<File> FileArrayList = new ArrayList<>();
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            int i=0;
            while (entry != null) {
                i++;
                FileArrayList.add(extractFile(zipIn, i));
                System.out.println("FILEARRAYLISTADD : " + FileArrayList.toString());
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
            return FileArrayList;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private File extractFile(ZipInputStream zipIn, int i) throws IOException {
            File tmpdir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Alexandria", "temp" );
            tmpdir.mkdir();
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Alexandria" + "/temp", "tmp"+i +".mp3");
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
            bos.close();
            System.out.println("FILE : " + file.toString());
            String RealName =  RetrieveMediaTrackNumber(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Alexandria/temp/tmp"+i+".mp3");
            file.renameTo(new File(file.getAbsolutePath().replace("tmp"+i,RealName)));
            File f = new File(file.getAbsolutePath().replace("tmp"+i,RealName));
            //tmpdir.renameTo(new File(tmpdir.getAbsolutePath().replace("temp",RetrieveMediaAlbum(file))));
            return f;
        }

        private String RetrieveMediaName(File file){
            MediaMetadataRetriever name = new MediaMetadataRetriever();
            System.out.println("RETRIEVEMEDIANAME: "+file.toString());
            name.setDataSource(file.getPath());
            return name.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        }

        private String RetrieveMediaTrackNumber(String path){
            MediaMetadataRetriever name = new MediaMetadataRetriever();
            name.setDataSource(path);
            return name.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        }

        private String RetrieveMediaAlbum(File file){
        MediaMetadataRetriever name = new MediaMetadataRetriever();
        name.setDataSource(file.getPath());
        return name.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        }


    @RequiresApi(api = Build.VERSION_CODES.P)
        private ImageView RetrieveMediaImageView(File file){
            MediaMetadataRetriever image = new MediaMetadataRetriever();
            image.setDataSource(file.getPath());
            if(image.getPrimaryImage()==null){return null;}
            else{ Bitmap bitmap =image.getPrimaryImage();
                ImageView imageView = new ImageView(context);
                imageView.setImageBitmap(bitmap);
                return imageView;
            }
        }

        //UTILITY

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String RetrieveShelfEntryName(File inputfile) throws IOException {
        MediaMetadataRetriever name = new MediaMetadataRetriever();
        //filetmp è un file temporaneo introdotto per lasciare pulita la cartella tmp
        File filetemp = UnZipSingleFile(inputfile.getPath());
        name.setDataSource(filetemp.getPath());
        // l'output non viene ritornato direttamente perchè richiama MediaMetaData, che potrebbe avere ancora bisogno di file tmp (idrk)
        String output =name.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        filetemp.delete();
        return output;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public String RetrieveShelfEntryCover(File inputfile) throws IOException { //CREO L'IMMAGINE E RITORNO IL SUO NOME
        MediaMetadataRetriever image = new MediaMetadataRetriever();
        //filetmp è un file temporaneo introdotto per lasciare pulita la cartella tmp
        File filetemp = UnZipSingleFile(inputfile.getPath());
        image.setDataSource(filetemp.getPath());
        try{
        Bitmap bitmap =image.getPrimaryImage();
        //ATTENZIONE @simone non bisogna cancellare questo file, perchè questo file è la cover!!!
        File file = bitmapToFile(context,bitmap,image.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)+".png");
        String path = file.getPath();
        filetemp.delete();
        return  path;}
        catch (Exception e){
            filetemp.delete();
            return "ic_cover_not_found.png";}
    }
    //end
    @RequiresApi(api = Build.VERSION_CODES.O)
    private File UnZipSingleFile(String zipFilePath) throws IOException {
        List<File> FileArrayList = new ArrayList<>();
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
            FileArrayList.add(extractFile(zipIn, 0));
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        zipIn.close();
        return FileArrayList.get(0);
    }

    private static File bitmapToFile(Context context,Bitmap bitmap, String fileNameToSave) { // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            file = new File(Environment.getExternalStorageDirectory() + File.separator + fileNameToSave);
            file.createNewFile();
            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();
            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return file; // it will return null
        }
    }
}

