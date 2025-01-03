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
    private List<VerbAction> verbaction;
    public Class getClasse() {
        return classe;
    }
    public Mapping(String className, String methodName, Class classe, List<VerbAction> verbaction) {
        this.className = className;
        this.methodName = methodName;
        this.classe = classe;
        this.verbaction=verbaction;
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
    public Object getReponse(HttpServletRequest request,HttpServletResponse response){
        Object valiny=null;
        try {
            for(VerbAction vA : verbaction){
                if(vA.getVerb().equals(request.getMethod())){
                    valiny=vA.getReponse(request, classe,response);
                }
            }
        } catch (Exception e) {
           System.out.println(e.getMessage());
        }
       
        return valiny;
    }
   

   

   

    

   

   
}