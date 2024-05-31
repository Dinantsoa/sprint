package mg.itu.prom16;

import java.lang.reflect.*;

public class Mapping {
    private String className;
    private String methodName;
    private Class classe;

    private Method methode;

    public Mapping(String className, String methodName, Class classe, Method method) {
        this.className = className;
        this.methodName = methodName;
        this.methode = method;
        this.classe = classe;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public String retour() {
        String valiny = "";
        try {
            Object instance = this.classe.getDeclaredConstructor().newInstance();
            valiny = this.methode.invoke(instance).toString();
        } catch (Exception e) {
            valiny = "Tsy mety";
        }
        return valiny;
    }
}