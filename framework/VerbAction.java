package framework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class VerbAction {
    private String verb;
    private Method methode;
    public VerbAction(){
        
    }
    public VerbAction (String verb,Method methode){
        this.verb=verb;
        this.methode=methode;
    }
    public String getVerb() {
        return verb;
    }
    public Method getMethode() {
        return methode;
    }
    public boolean isPrimitiveOrString(Class<?> paramType) {
        return paramType.isPrimitive() || paramType.equals(String.class);
    }
    public String retour(Class classe) {
        String valiny = "";
        try {
            Object instance = classe.getDeclaredConstructor().newInstance();
            valiny = this.methode.invoke(instance).toString();
        } catch (Exception e) {
            valiny = "Tsy mety";
        }
        return valiny;
    }
    private static Object mamadikaObject(Class<?> clazz, String value) throws Exception {
        if (value == null) {
            if (clazz == int.class || clazz == Integer.class) {
                return 0; // Valeur par défaut pour int
            }
            // Gestion d'autres types par défaut ici si nécessaire
        }
        try {
            if (clazz.equals(String.class)) {
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
     public ArrayList<String> makaParametre(HttpServletRequest request) throws Exception {
        Enumeration<String> parameterNames = request.getParameterNames();
        ArrayList<String> valiny = new ArrayList<String>();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            valiny.add(name);
        }
        return valiny;
    }
    public void checkSession(Object controlleur, HttpSession session) throws Exception {
        Field[] attributes = controlleur.getClass().getDeclaredFields();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getType().getName().equals(MySession.class.getName())) {
                
                attributes[i].setAccessible(true);
                attributes[i].set(controlleur, new MySession(session));
            }
        }
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
     public Object getReponse(HttpServletRequest request,Class classe) throws Exception {
        Parameter[] parameters = methode.getParameters();
        Object[] args = new Object[parameters.length];
        String typeMethode=request.getMethod();
        if(!this.verb.equalsIgnoreCase(typeMethode)){
            throw new Exception("Erreur lors du type methode different" +" "+ typeMethode + " "+ this.verb );
        }
        for (int i = 0; i < parameters.length; i++) {

            if (isPrimitiveOrString(parameters[i].getType())) {
                args[i] = request.getParameter(parameters[i].getName());
                if (parameters[i].isAnnotationPresent(Param.class)) {
                    Param param = parameters[i].getAnnotation(Param.class);
                    String paramName = param.value();
                    String paramValue = request.getParameter(paramName);
                    args[i] = mamadikaObject(parameters[i].getType(), paramValue);
                } else {
                    ServletException e = new ServletException("ETU 2759 Exception misy tsy annote");
                    throw e;
                }
            } else if (parameters[i].getType().getName().equals(MySession.class.getName())) {
                HttpSession session = request.getSession();
                MySession mysession = new MySession(session);
                args[i] = mysession;
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
        Object instanceControlleur = classe.getDeclaredConstructor().newInstance();
        checkSession(instanceControlleur, request.getSession());
        return methode.invoke(instanceControlleur, args);

    }

}
