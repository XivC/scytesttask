package ru.skytesttask.repository;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Util {
     static File getResFile(String path) {
        ClassLoader classLoader = Storage.class.getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("No file found " + path);
        } else {
            try {
                return new File(resource.toURI());
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
