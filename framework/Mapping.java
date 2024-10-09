package framework;

import java.util.Enumeration;
import java.util.List;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class Mapping {
    private String className;
    private String methodName;
    private Class classe;
<<<<<<< Updated upstream

    public Class getClasse() {
        return classe;
    }

    public Method getMethode() {
        return methode;
    }

    private Method methode;

    public Mapping(String className, String methodName, Class classe, Method method) {
=======
    private List<VerbAction> verbaction;
    public Class getClasse() {
        return classe;
    }
    public Mapping(String className, String methodName, Class classe, List<VerbAction> verbaction) {
>>>>>>> Stashed changes
        this.className = className;
        this.methodName = methodName;
        this.classe = classe;
<<<<<<< Updated upstream
=======
        this.verbaction=verbaction;
>>>>>>> Stashed changes
    }
    public String retour() {
        String valiny = "";
        try {
            VerbAction v=new VerbAction();
            valiny=v.retour(this.classe);
        } catch (Exception e) {
            valiny = "Tsy mety";
        }
        return valiny;
    }
    public String getClassName() {
        return this.className;
    }
    public Method getMethode(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Method valiny=null;
        boolean error=true;
        try {
            for(VerbAction vA : verbaction){
                if(vA.getVerb().equals(request.getMethod())){
                    valiny=vA.getMethode();
                    error =false;
                }
                if (error) {
                    Exception a=new Exception("Type methode http different : "+vA.getVerb()+" && "+request.getMethod());
                    response.sendError(405,a.getMessage());
                }
            }
           
        } catch (Exception e) {
            response.sendError(405,e.getMessage());
        }
       
        return valiny;
    }
    public String getMethodName() {
        return this.methodName;
    }
    public Object getReponse(HttpServletRequest request){
        Object valiny=null;
        try {
            for(VerbAction vA : verbaction){
                if(vA.getVerb().equals(request.getMethod())){
                    valiny=vA.getReponse(request, classe);
                }
            }
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
       
        return valiny;
    }
   

<<<<<<< Updated upstream
    public Object getReponse(HttpServletRequest request) throws Exception {
        Parameter[] parameters = methode.getParameters();
        Object[] args = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
=======
   
>>>>>>> Stashed changes

   

    

   

<<<<<<< Updated upstream
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

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
    public void checkSession(Object controlleur, HttpSession session) throws Exception {
        Field[] attributes = controlleur.getClass().getDeclaredFields();
        for (int i = 0; i < attributes.length; i++) {
            if (attributes[i].getType().getName().equals(MySession.class.getName())) {
                
                attributes[i].setAccessible(true);
                attributes[i].set(controlleur, new MySession(session));
            }
        }
    }
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
=======
   
>>>>>>> Stashed changes
}