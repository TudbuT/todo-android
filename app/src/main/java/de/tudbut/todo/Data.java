package de.tudbut.todo;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
        if(list == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                FileInputStream reader = new FileInputStream(path + "/" + currentListName + ".todo");
                byte[] buf = new byte[1024];
                int r;
                while ((r = reader.read(buf)) != -1) {
                    baos.write(buf, 0, r);
                }
                reader.close();
            } catch (IOException e) {
                return list = new ToDoList();
            }
            list = ToDoList.fromString(new String(baos.toByteArray()));
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
}
