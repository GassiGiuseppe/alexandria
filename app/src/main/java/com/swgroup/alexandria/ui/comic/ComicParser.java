package com.swgroup.alexandria.ui.comic;


import android.graphics.Bitmap;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.ArrayList;

public interface ComicParser {
    void setFileLocation(File file) throws IOException;
    void parse() throws IOException;
    void destroy() throws IOException;

    String getType();
    ArrayList<Uri> getPagesUri();
    int numPages();
}
