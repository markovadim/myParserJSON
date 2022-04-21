package by.markov;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyParser<T> implements LinkList<T> {
    @Override
    public String saveObject(T ob) throws IllegalAccessException {
        StringBuilder result = new StringBuilder();
        result.append("{\n");
        Field[] fields = ob.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.get(ob) == null) {
                result.append("    \"").append(field.getName()).append("\": \"").append(field.get(ob)).append("\"");
                checkOfLastField(fields, field, result);
            } else {
                //if field is primitive and not array
                if ((field.getType().toString().contains("String") || (field.getType().isPrimitive())) && (!field.getType().isArray())) {
                    result.append("  \"").append(field.getName()).append("\": \"").append(field.get(ob)).append("\"");
                    checkOfLastField(fields, field, result);
                    //if field is array (primitives and objects
                } else if (field.getType().isArray()) {
                    result.append(parseArray(field.get(ob), field.getName()));
                    checkOfLastField(fields, field, result);
                } else {
                    result.append(parseObject(field.get(ob), field.getName()));
                    checkOfLastField(fields, field, result);
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
                result.append(field.get(obj));
                checkOfLastField(fields, field, result);
            } else if (field.getType().isPrimitive() || field.toString().contains("String")) {
                result.append("    \"").append(field.getName()).append("\": \"").append(field.get(obj)).append("\"");
                checkOfLastField(fields, field, result);
            } else if (field.getType().isArray()) {
                result.append(parseArray(field.get(obj), field.getName()));
            } else if ((field.get(obj) instanceof List<?>)) {
                result.append(parseList(field.get(obj), field.getName()));
                checkOfLastField(fields, field, result);
            } else if ((field.get(obj) instanceof Map<?, ?>)) {
                result.append(parseMap(field.get(obj), field.getName()));
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
        result.append("]");
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
        result.append("\t]");
        return result.toString();
    }

    public String parseMap(Object ob, String name) {
        StringBuilder result = new StringBuilder();
        Map<Object, Object> map = (Map<Object, Object>) ob;
        result.append("\t\"").append(name).append("\": ").append("{\n");
        Iterator<Map.Entry<Object, Object>> entries = map.entrySet().iterator();
        for (Object object : map.keySet()) {
            Map.Entry<Object, Object> entry = entries.next();
            if (object.equals(map.keySet().size())) {
                result.append("\t\"").append(entry.getKey()).append("\"").append(":").append("\"").append(entry.getValue()).append("\"").append("\n");
            } else {
                result.append("\t\"").append(entry.getKey()).append("\"").append(":").append("\"").append(entry.getValue()).append("\"").append(",\n");
            }
        }
        result.append("\t}\n");
        System.out.println(result);
        return result.toString();
    }

    public StringBuilder checkOfLastField(Field[] fields, Field field, StringBuilder result) {
        if (field == fields[fields.length - 1]) {
            result.append("\n");
        } else {
            result.append(",\n");
        }
        return result;
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
