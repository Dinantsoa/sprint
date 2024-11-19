package framework;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Définition de l'annotation @Min
@Target(ElementType.FIELD) // Peut être utilisée sur les champs
@Retention(RetentionPolicy.RUNTIME) // Disponible à l'exécution
public @interface Min {
    // La valeur minimale autorisée
    int value();

    // Message d'erreur par défaut
    String message() default "La valeur doit être supérieure ou égale à {value}";
}
