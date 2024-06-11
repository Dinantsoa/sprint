package mg.itu.prom16;

import java.lang.reflect.*;
import java.util.Enumeration;

import jakarta.servlet.http.*;

public class Mapping {
    private String className;
    private String methodName;
    private Class classe;
    private Class[] typeArgs;

    private Method methode;

    public Mapping(String className, String methodName, Class classe, Method method) {
        this.className = className;
        this.methodName = methodName;
        this.methode = method;
        this.classe = classe;
        this.typeArgs = typeArgs;
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public Class[] getArgs() {
        return typeArgs;
    }

    public void setArgs(Class[] args) {
        this.typeArgs = typeArgs;
    }

    public Method getMethod() {
        return this.methode;
    }

    public Class getClasse() {
        return this.classe;
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

    public Object getReponse(HttpServletRequest request) throws Exception {
        Object[] repons = new Object[methode.getParameterCount()];
        Enumeration<String> values = request.getParameterNames();
        for (int i = 0; i < repons.length; i++) {
            if (values.hasMoreElements()) {
                repons[i] = request.getParameter(values.nextElement());
            }
        }
        return methode.invoke(classe.getDeclaredConstructor().newInstance(), repons);
    }
}