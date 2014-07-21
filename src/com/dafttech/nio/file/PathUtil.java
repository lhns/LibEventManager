package com.dafttech.nio.file;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class PathUtil {
    public static Path get(URI uri) {
        if (!uri.toString().contains("!")) {
            return Paths.get(uri);
        } else {
            // JAVA BUG WORKAROUND
            String splitURL[] = uri.toString().split("!");
            try {
                FileSystem fs = FileSystems.newFileSystem(URI.create(splitURL[0]), new HashMap<String, String>());
                return fs.getPath(splitURL[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Path get(URL url) {
        try {
            return get(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
