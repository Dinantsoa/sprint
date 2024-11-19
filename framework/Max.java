package framework;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Définition de l'annotation @Max
@Target(ElementType.FIELD) // Peut être utilisée sur les champs
@Retention(RetentionPolicy.RUNTIME) // Disponible à l'exécution
public @interface Max {
    // La valeur maximale autorisée
    int value();

    // Message d'erreur par défaut
    String message() default "La valeur doit être inférieure ou égale à {value}";
}
