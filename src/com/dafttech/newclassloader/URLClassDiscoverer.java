package com.dafttech.newclassloader;

import java.net.URL;

public class URLClassDiscoverer {
    private URL target;

    public URLClassDiscoverer(URL target) {
        this.target = target;
    }

    private void discover(URL url, IURLClassDiscoverStrategy strategy) {
        strategy.discover(url);
    }
}
