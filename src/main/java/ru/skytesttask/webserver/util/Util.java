package ru.skytesttask.webserver.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {
    public static Map<String, String> getQueryParams(String query){
        if (query == null || query.isEmpty()){
            return Collections.emptyMap();
        }
        Pattern pattern = Pattern.compile("&");
        return pattern.splitAsStream(query)
                .map(param -> Arrays.asList(param.split("=")))
                .filter(pair -> pair.size() == 2)
                .collect(Collectors.toMap(t -> t.get(0), t -> t.get(1)));

    }
}
