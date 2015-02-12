package org.lolhens.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by LolHens on 12.02.2015.
 */
public class DynamicInvoke {
    public static void main(String[] args) {
        String className = null;

        if (args.length > 0) {
            className = args[0];
            args = Arrays.copyOfRange(args, 1, args.length);
        } else {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                className = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Class<?> clazz = Class.forName(className);
            Method main = clazz.getDeclaredMethod("main", String[].class);
            main.invoke(null, new Object[]{args});
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
