package com.dafttech.newclassloader;

import java.net.URL;
import java.util.Set;

import com.dafttech.hash.HashUtil;

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

    public static Set<ClassLocation> discoverSourceURL(URL sourceURL) {
        return null;
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(sourceURL, qualifiedName);
    }

    @Override
    public boolean equals(Object obj) {
        return HashUtil.equals(this, obj);
    }
}
