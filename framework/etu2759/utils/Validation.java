package framework.etu2759.utils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

import framework.etu2759.erreur.Erreur;
import framework.etu2759.validation.contraints.*;

import java.sql.Timestamp;

public class Validation {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );


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
    public static Erreur presentOrFutureValidator(Object obj, Field field) throws IllegalAccessException {
        Erreur valiny = null;

        if (field.isAnnotationPresent(PresentOrFuture.class)) {
            field.setAccessible(true);
            Object value = field.get(obj);
            PresentOrFuture annotation = field.getAnnotation(PresentOrFuture.class);

            if (value instanceof LocalDate) {
                LocalDate dateValue = (LocalDate) value;
                if (dateValue.isBefore(LocalDate.now())) {
                    valiny = new Erreur(annotation.message(), dateValue.toString());
                }
            } 
            else if (value instanceof LocalDateTime) {
                LocalDateTime dateTimeValue = (LocalDateTime) value;
                if (dateTimeValue.isBefore(LocalDateTime.now())) {
                    valiny = new Erreur(annotation.message(), dateTimeValue.toString());
                }
            } 
            else if (value instanceof java.util.Date) { // java.util.Date
                java.util.Date dateValue = (java.util.Date) value;
                if (dateValue.before(new java.util.Date())) {
                    valiny = new Erreur(annotation.message(), dateValue.toString());
                }
            }
            else if (value instanceof java.sql.Date) { // java.sql.Date
                java.sql.Date sqlDateValue = (java.sql.Date) value;
                if (sqlDateValue.toLocalDate().isBefore(LocalDate.now())) {
                    valiny = new Erreur(annotation.message(), sqlDateValue.toString());
                }
            } 
            else if (value instanceof Timestamp) { // java.sql.Timestamp
                Timestamp timestampValue = (Timestamp) value;
                if (timestampValue.toInstant().isBefore(new java.util.Date().toInstant())) {
                    valiny = new Erreur(annotation.message(), timestampValue.toString());
                }
            }
        }

        return valiny;
    }

    public static Erreur emailValidator(Object obj, Field field) throws IllegalAccessException {
        Erreur valiny = null;
        
        if (field.isAnnotationPresent(EmailValid.class)) {
            field.setAccessible(true);
            Object value = field.get(obj);
            
            if (value instanceof String) {
                String email = (String) value;
                if (!EMAIL_PATTERN.matcher(email).matches()) {
                    EmailValid annotationEmail = field.getAnnotation(EmailValid.class);
                    valiny = new Erreur(annotationEmail.message(), email);
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
        Erreur presentOrFutureField = presentOrFutureValidator(obj, field);
        if (presentOrFutureField != null) {
            if (valiny == null) {
                valiny = presentOrFutureField;
            }
            valiny.manampyErreur(presentOrFutureField);
        }
        Erreur emailField = emailValidator(obj, field);
        if (emailField != null) {
            if (valiny == null) valiny = emailField;
            valiny.manampyErreur(emailField);
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