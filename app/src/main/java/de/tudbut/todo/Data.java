package de.tudbut.todo;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Data {

    private static String path;

    public static void populate(String path) {
        Data.path = path;
    }

    private static String currentListName = "main";

    public static String getListName() {
        return currentListName;
    }

    public static void setList(String name) {
        currentListName = name;
        list = null;
    }

    private static ToDoList list;

    public static ToDoList getList() {
        if (list == null) {
            try {
                InputStream stream = new FileInputStream(path + "/" + currentListName + ".todo");
                list = readList(stream);
                stream.close();
            } catch (IOException e) {
                list = new ToDoList();
            }
        }
        return list;
    }

    public static void save() {
        try {
            FileOutputStream writer = new FileOutputStream(path + "/" + currentListName + ".todo");
            writer.write(list.intoString().getBytes());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ToDoList readList(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int r;
        while ((r = stream.read(buf)) != -1) {
            baos.write(buf, 0, r);
        }
        return ToDoList.fromString(baos.toString());
    }
}
