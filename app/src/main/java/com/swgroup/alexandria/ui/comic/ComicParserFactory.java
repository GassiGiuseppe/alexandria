package com.swgroup.alexandria.ui.comic;


import java.io.File;
import java.io.IOException;

public class ComicParserFactory {

    public static ComicParser create(String file) {
        return create(new File(file));
    }

    public static ComicParser create(File file) {
        ComicParser parser = null;
        String fileName = file.getAbsolutePath().toLowerCase();
        if (file.isDirectory()) {
            parser = new CDirParser();
        }
        if (fileName.toLowerCase().matches(".*\\.(zip|cbz)$")) {
            parser = new CbzParser();
        }
        if (fileName.toLowerCase().matches(".*\\.(rar|cbr)$")) {
            parser = new CbrParser();
        }
        if (fileName.toLowerCase().matches(".*\\.(tar|cbt)$")) {
            parser = new CbtParser();
        }
        if (fileName.toLowerCase().matches(".*\\.(7z|cb7)$")) {
            parser = new CbsevenParser();
        }

        return tryParse(parser, file);
    }

    private static ComicParser tryParse(ComicParser parser, File file) {
        if (parser == null) {
            return null;
        }
        try {
            parser.setFileLocation(file);
            parser.parse();
        }
        catch (IOException e) {
            return null;
        }

        if (parser instanceof CDirParser && parser.numPages() < 4)
            return null;

        return parser;
    }
}