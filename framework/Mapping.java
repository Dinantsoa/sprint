package framework;

import java.util.Enumeration;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Enumeration;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class Mapping {
    private String className;
    private String methodName;
    private Class classe;

    public Class getClasse() {
        return classe;
    }

    public Method getMethode() {
        return methode;
    }

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
            throw e;
        }
        return valiny;
    }

    public Method getMethodByName(Class<?> cls, String methodName) throws Exception {
        // Obtenir toutes les méthodes de la classe
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            // Vérifier si le nom de la méthode correspond au nom donné
            if (method.getName().equals(methodName)) {
                return method;

            }
        }
        Exception e = new Exception("Tsisy methode");
        throw e;
        // Retourner null si aucune méthode correspondante n'est trouvée

    }

    public ArrayList<String> makaParametre(HttpServletRequest request) throws Exception {
        Enumeration<String> parameterNames = request.getParameterNames();
        ArrayList<String> valiny = new ArrayList<String>();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            valiny.add(name);

        }
        return valiny;
    }

    public boolean isPrimitiveOrString(Class<?> paramType) {
        return paramType.isPrimitive() || paramType.equals(String.class);
    }

    private static Object mamadikaObject(Class<?> clazz, String value) throws Exception {
        if (value == null) {
            if (clazz == int.class || clazz == Integer.class) {
                return 0; // Valeur par défaut pour int
            }
            // Gestion d'autres types par défaut ici si nécessaire
        }
        try {
            if (clazz == String.class) {
                return value;
            } else if (clazz == int.class || clazz == Integer.class) {
                return Integer.parseInt(value);
            } else if (clazz == boolean.class || clazz == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (clazz == double.class || clazz == Double.class) {
                return Double.parseDouble(value);
            } else if (clazz == long.class || clazz == Long.class) {
                return Long.parseLong(value);
            } else if (clazz == float.class || clazz == Float.class) {
                return Float.parseFloat(value);
            } else if (clazz == short.class || clazz == Short.class) {
                return Short.parseShort(value);
            } else if (clazz == byte.class || clazz == Byte.class) {
                return Byte.parseByte(value);
            }
            // Ajouter d'autres types si nécessaire

            // Si le type n'est pas géré, lever une exception
            throw new IllegalArgumentException("Cannot convert String to " + clazz.getName());
        } catch (Exception e) {

            Object averina = clazz.getConstructor().newInstance();
            return averina;
        }

    }

    public Object getReponse(HttpServletRequest request) throws Exception {
        Parameter[] parameters = methode.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {

            if (isPrimitiveOrString(parameters[i].getType())) {
                args[i] = request.getParameter(parameters[i].getName());
                if (parameters[i].isAnnotationPresent(Param.class)) {
                    Param param = parameters[i].getAnnotation(Param.class);
                    String paramName = param.value();
                    String paramValue = request.getParameter(paramName);

                    // For simplicity, assume all parameters are of type String
                    args[i] = mamadikaObject(parameters[i].getType(), paramValue);
                } else {
                    ServletException e = new ServletException("ETU 2759 Exception misy tsy annote");
                    throw e;
                }
            } else {
                ArrayList<String> listeParametre = makaParametre(request);
                String nomParametre = parameters[i].getName();
                if (parameters[i].isAnnotationPresent(Param.class)) {
                    Param param = parameters[i].getAnnotation(Param.class);
                    nomParametre = param.value();
                } else {
                    ServletException e = new ServletException("ETU 2759 exception misy tsy annote");
                    throw e;
                }
                Class cl = parameters[i].getType();
                // Employer e=new Employer();
                Object object = cl.getConstructor().newInstance();
                Object p[] = new Object[1];

                for (String a : listeParametre) {
                    String[] saraka = a.split("\\.");
                    if (saraka.length > 1) {
                        if (saraka[0].equalsIgnoreCase(nomParametre)) {
                            String maj = saraka[1].substring(0, 1).toUpperCase() + saraka[1].substring(1);
                            Method m = getMethodByName(cl, "set" + maj);
                            Parameter[] pa = m.getParameters();
                            m.invoke(object, mamadikaObject(pa[0].getType(), request.getParameter(a)));

                        }
                    }
                }
                args[i] = object;

            }
        }
        return methode.invoke(classe.getDeclaredConstructor().newInstance(), args);

    }

<<<<<<< Updated upstream
=======
    public void checkSession(Object controlleur, HttpSession session) throws Exception {
        Field[] attributes = controlleur.getClass().getDeclaredFields();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getType().getName().equals(MySession.class.getName())) {

                attributes[i].setAccessible(true);
                attributes[i].set(controlleur, new MySession(session));
            }
        }
    }
>>>>>>> Stashed changes
}