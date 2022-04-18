package by.markov;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;

public class MyParser<T> implements LinkList<T> {
    @Override
    public String saveObject(T ob) throws IllegalAccessException {
        StringBuilder result = new StringBuilder();
        result.append("{\n");
        Field[] fields = ob.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.get(ob) == null) {
                //if field is last
                if (field == fields[fields.length - 1]) {
                    result.append("  \"").append(field.getName()).append("\": ").append(field.get(ob)).append("\n");
                } else {
                    result.append("  \"").append(field.getName()).append("\": ").append(field.get(ob)).append(",\n");
                }

            } else {
                //if field is primitive and not array
                if ((field.getType().toString().contains("String") || (field.getType().isPrimitive())) && (!field.getType().isArray())) {
                    result.append("  \"").append(field.getName()).append("\": \"").append(field.get(ob)).append("\",\n");

                    //if field is array (primitives and objects
                } else if (field.getType().isArray()) {
                    result.append(parseArray(field.get(ob), field.getName()));
                } else {
                    result.append(parseObject(field.get(ob), field.getName()));
                }
            }
            field.setAccessible(false);
        }
        result.append("}");
        try {
            writeToFile(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private String parseObject(Object obj, String name) throws IllegalAccessException {
        StringBuilder result = new StringBuilder();
        result.append("  \"").append(name).append("\": ").append("{\n");
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(obj) == null) {
                result.append(field.get(obj)).append(",\n");
            } else if (field.getType().isPrimitive() || field.toString().contains("String")) {

                //if field is last
                if (field == fields[fields.length - 1]) {
                    result.append("    \"").append(field.getName()).append("\": \"").append(field.get(obj)).append("\"\n");
                } else {
                    result.append("    \"").append(field.getName()).append("\": \"").append(field.get(obj)).append("\",\n");
                }
            } else if (field.getType().isArray()) {
                result.append(parseArray(field.get(obj), field.getName()));
            } else if ((field.get(obj) instanceof List<?>)) {
                result.append(parseList(field.get(obj), field.getName()));
            } else {
                result.append(parseObject(field.get(obj), field.getName()));
            }
        }
        result.append("  }\n");
        return result.toString();
    }

    public String parseArray(Object ob, String name) {
        StringBuilder result = new StringBuilder();
        result.append("  \"").append(name).append("\": ").append("[\n");
        for (int i = 0; i < Array.getLength(ob); i++) {
            if (Array.get(ob, i) == null) {
                if (i == Array.getLength(ob) - 1) {
                    result.append("\t").append(Array.get(ob, i)).append("\n  ");
                } else {
                    result.append("\t").append(Array.get(ob, i)).append(",\n  ");
                }
            } else {
                if (i == Array.getLength(ob) - 1) {
                    result.append("\t\"").append(Array.get(ob, i)).append("\"\n  ");
                } else {
                    result.append("\t\"").append(Array.get(ob, i)).append("\", \n");
                }
            }
        }
        result.append("],\n");
        return result.toString();
    }

    public String parseList(Object ob, String name) {
        StringBuilder result = new StringBuilder();
        List<?> list = (List<?>) ob;
        result.append("\t\"").append(name).append("\": ").append("[\n");
        for (int i = 0; i < list.size() - 1; i++) {
            if (i == list.size() - 2) {
                result.append("\t\"").append(list.get(i)).append("\"\n  ");
            } else {
                result.append("\t\"").append(list.get(i)).append("\",\n");
            }
        }
        result.append("\t]\n");
        return result.toString();
    }

    public void writeToFile(String result) throws IOException {
        File file = new File(System.getProperty("user.dir"), "json_object.json");
        FileWriter fw = new FileWriter(file, false);
        if (!file.exists()) {
            file.createNewFile();
        } else {
            fw.append(result);
            fw.close();
        }
    }
}
