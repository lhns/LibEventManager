package org.lolhens.nio.file;

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
    public static Path get(URI uri) throws IllegalArgumentException {
        if (!uri.toString().contains("!")) {
            return Paths.get(uri);
        } else {
            // JAVA BUG WORKAROUND
            String splitURL[] = uri.toString().split("!");
            try {
                FileSystem fs = FileSystems.newFileSystem(URI.create(splitURL[0]), new HashMap<String, String>());
                return fs.getPath(splitURL[1]);
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to get Path from: " + uri.toString(), e);
            }
        }
    }

    public static Path get(URL url) throws IllegalArgumentException {
        try {
            return get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Unable to convert to URI: " + url.toString(), e);
        }
    }
}
