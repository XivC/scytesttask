package ru.skytesttask.repository.util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Util {
    static Scanner getResFile(String path) throws FileNotFoundException {
        ClassLoader classLoader = Storage.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream(path);
        if (in == null) throw new FileNotFoundException("File " + path + " not found");
        return new Scanner(in);


    }
}
