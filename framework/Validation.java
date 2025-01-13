package framework;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;


public class Validation {


    public static Erreur nonNullValidation(Object obj,Field field) throws IllegalAccessException
    {
        Erreur valiny=null;
        if (field.isAnnotationPresent(NonNull.class)) {
            field.setAccessible(true);
            Object value = field.get(obj);
            if (value == null) {
                NonNull annotationNonNull = field.getAnnotation(NonNull.class);
                valiny=new Erreur(annotationNonNull.message(),null);
                
            }
        }

        return valiny;

    }
    public static Erreur minValidator(Object obj,Field field) throws IllegalAccessException{
        Erreur valiny=null;
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
                        String errorMessage = annotationMin.message()
                                .replace("{value}", String.valueOf(annotationMin.value()));
                        valiny=new Erreur(errorMessage,String.valueOf(intValue));
                        
                    }
                }
                
            
        }
        return valiny;
    }
    public static Erreur maxValidator(Object obj,Field field) throws IllegalAccessException
    {
        Erreur valiny=null;
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
                        String errorMessage = annotationMax.message()
                                .replace("{value}", String.valueOf(annotationMax.value()));
                        valiny=new Erreur(errorMessage,String.valueOf(intValue));
                        
                    }
                }
                
            
        }
        return valiny;
    }
    public static Erreur getErreurParField(Object obj,Field field) throws IllegalAccessException
    {

        Erreur valiny=null;
        Erreur nullField=nonNullValidation(obj, field);
        
        if (nullField!=null) {
            if (valiny==null) {
                valiny=nullField;
                
            }


                    
        }
        Erreur minField=minValidator(obj, field);
        if (minField!=null) {
            if (valiny==null) {

                valiny=minField;
            }
            valiny.manampyErreur(minField);
            
        }
        Erreur maxField=maxValidator(obj, field);
        if (maxField!=null) {
            if (valiny==null) {
                valiny=maxField;

            }
            valiny.manampyErreur(maxField);

                 
        }

        
        return valiny;



    }
    // public  static HashMap<String,Erreur> manaoValidation(Object obj) throws IllegalAccessException {
    //     HashMap<String,Erreur> valiny = new HashMap<String,Erreur>();
    //     Field[] fields = obj.getClass().getDeclaredFields();

    //     for (Field field : fields) {
    //         Erreur erreur=getErreurParField(obj, field);
    //         valiny.put(field.getName(), erreur);
            
            
            
           
    //     }
    //     return valiny;
    // }
    
}