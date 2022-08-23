package model;

import java.lang.reflect.Modifier;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;

public class PrivateFieldTester {
    
    public static void checkPrivateFields(Class<?> klasse) {
        for (Field field : klasse.getDeclaredFields()) {
            Assertions.assertTrue(Modifier.isPrivate(field.getModifiers()), "Feltet " + field.getName() + " skal være satt til private.");
        }
    }

}
