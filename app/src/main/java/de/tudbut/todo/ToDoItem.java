package de.tudbut.todo;

public class ToDoItem {

    public String title;
    public String description;
    public boolean done;

    public ToDoItem(String title, String description, boolean done) {
        this.title = title;
        this.description = description;
        this.done = done;
    }

    public static ToDoItem fromString(String s) {
        String[] values = s.split(";");
        String title = decode(values[0]);
        String description = decode(values[1]);
        boolean done = Boolean.parseBoolean(decode(values[2]));
        return new ToDoItem(title, description, done);
    }

    public String intoString() {
        return new StringBuilder().append(encode(title)).append(';').append(encode(description)).append(';').append(encode(Boolean.toString(done))).toString();
    }

    private static String encode(String s) {
        return s.replace("%", "%P").replace(";", "%S").replace("\n", "%N");
    }

    private static String decode(String s) {
        return s.replace("%S", ";").replace("%N", "\n").replace("%P", "%");
    }
}
