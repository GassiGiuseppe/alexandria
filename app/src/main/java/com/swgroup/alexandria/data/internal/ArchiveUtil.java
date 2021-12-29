package com.swgroup.alexandria.data.internal;

import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

public class ArchiveUtil {

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)))) {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                try (FileOutputStream fout = new FileOutputStream(file)) {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                }
            }
        }
    }

    public static void untar(File tarFile, File targetDirectory) throws IOException {
        try (TarArchiveInputStream tis = new TarArchiveInputStream(
                new BufferedInputStream(new FileInputStream(tarFile)))) {
            TarArchiveEntry te;
            int count;
            byte[] buffer = new byte[8192];
            while ((te = tis.getNextTarEntry()) != null) {
                File file = new File(targetDirectory, te.getName());
                File dir = te.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw  new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (te.isDirectory())
                    continue;
                try (FileOutputStream fout = new FileOutputStream(file)) {
                    while ((count = tis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                }
            }

        }
    }

    public static void unsevenzip(File sevenzFile, File targetDirectory) throws IOException {
        try (SevenZFile szis = new SevenZFile(sevenzFile)) {
            SevenZArchiveEntry sze;
            int count;
            byte[] buffer = new byte[8192];
            while ((sze = szis.getNextEntry()) != null) {
                File file = new File(targetDirectory, sze.getName());
                File dir = sze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw  new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (sze.isDirectory())
                    continue;
                try (FileOutputStream fout = new FileOutputStream(file)) {
                    while ((count = szis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                }
            }
        }
    }

    public static void unrar(File rarFile, File targetDirectory) throws IOException {
        try {
            Junrar.extract(rarFile, targetDirectory);
        }
        catch (RarException e) {
            throw new IOException("unable to open archive");
        }
    }


}
