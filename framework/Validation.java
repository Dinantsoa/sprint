package framework;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class Validation {


    public static String nonNullValidation(Object obj,Field field) throws IllegalAccessException
    {
        String valiny=null;
        if (field.isAnnotationPresent(NotNull.class)) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) {
                NotNull annotationNonNull = field.getAnnotation(NotNull.class);
                valiny=field.getName() + " (" + annotationNonNull.message() + ")";
            }
        }

        return valiny;

    }
    public static String minValidator(Object obj,Field field) throws IllegalAccessException{
        String valiny=null;
        if (field.isAnnotationPresent(Min.class)) {
            field.setAccessible(true);
            Object value = field.get(obj);
            
                Min annotationMin = field.getAnnotation(Min.class);
                int minValue = annotationMin.value();

            // Vérifie si le champ est de type Integer ou int
                if (value instanceof Integer ) {
                    Integer intValue=(Integer) value;
                    if (intValue < minValue) {
                        // Ajouter une erreur si la valeur est inférieure au minimum
                        valiny=field.getName() + ": " + annotationMin.message().replace("{value}", String.valueOf(minValue));
                    }
                }
                
            
        }
        return valiny;
    }
    public static String maxValidator(Object obj,Field field) throws IllegalAccessException
    {
        String valiny=null;
        if (field.isAnnotationPresent(Max.class)) {
            field.setAccessible(true);
            Object value = field.get(obj);
        
                Max annotationMax = field.getAnnotation(Max.class);
                int maxValue = annotationMax.value();

            // Vérifie si le champ est de type Integer ou int
                if (value instanceof Integer ) {
                    Integer intValue=(Integer) value;
                    if (intValue > maxValue) {
                        // Ajouter une erreur si la valeur est inférieure au minimum
                        valiny=field.getName() + ": " + annotationMax.message().replace("{value}", String.valueOf(maxValue));
                    }
                }
                
            
        }
        return valiny;
    }
    public  static ArrayList<String> manaoValidation(Object obj) throws IllegalAccessException {
        ArrayList<String> valiny = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            String nullField=nonNullValidation(obj, field);
            if (nullField!=null) {
                valiny.add(nullField);

                
            }
            String minField=minValidator(obj, field);
            if (minField!=null) {
                valiny.add(minField);

                
            }
            String maxField=maxValidator(obj, field);
            if (maxField!=null) {
                valiny.add(maxField);

                
            }
           
        }
        return valiny;
    }
    
}