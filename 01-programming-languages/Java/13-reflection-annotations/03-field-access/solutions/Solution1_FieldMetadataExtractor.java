package academy.javaengineering.reflection.fieldaccess.solutions;

import java.lang.reflect.*;
import java.util.*;

public class Solution1_FieldMetadataExtractor {

    public static Map<String, String> extractMetadata(Class<?> clazz) {
        Map<String, String> result = new LinkedHashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            result.put(field.getName(), Modifier.toString(field.getModifiers()));
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(extractMetadata(String.class));
    }
}
