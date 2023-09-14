package de.tudbut.todo;

import java.util.ArrayList;

public class ToDoList extends ArrayList<ToDoItem> {

    public static ToDoList fromString(String s) {
        ToDoList list = new ToDoList();
        String[] items = s.split("\n");
        for (int i = 0; i < items.length; i++) {
            if(items[i].isEmpty()) {
                continue;
            }
            list.add(ToDoItem.fromString(items[i]));
        }
        return list;
    }

    public String intoString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.size(); i++) {
            builder.append(this.get(i).intoString()).append("\n");
        }
        return builder.toString();
    }
}
