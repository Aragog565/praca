package sklep.utils;

import java.lang.reflect.Field;

public class ObjectOverwrite {

    public static <T> void map(T object1, T object2){
        for(Field field : object1.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                if(field.get(object2) != null){
                    field.set(object1, field.get(object2));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
