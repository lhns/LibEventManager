package com.dafttech.newclassloader;

import java.net.URL;

public class ClassLocation {
    private URL sourceURL;
    private String qualifiedName;

    public ClassLocation(URL sourceURL, String qualifiedName) {
        this.sourceURL = sourceURL;
        this.qualifiedName = qualifiedName;
    }

    public URL getSourceURL() {
        return sourceURL;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }
}
