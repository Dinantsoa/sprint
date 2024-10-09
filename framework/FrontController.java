package mg.itu.prom16;

import java.io.File;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.http.*;

import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import framework.*;

// FontController
public class FrontController extends HttpServlet {

    private HashMap<String, Mapping> zeanotte;

    public void init() throws ServletException {
        ServletContext context = getServletContext();
        String chemin = context.getInitParameter("chemin");
        zeanotte = new HashMap<>();
        try {
            List<Class> controlleurs = scan(chemin);
            List<VerbAction> verbaction=new ArrayList<>(); 

            for (int i = 0; i < controlleurs.size(); i++) { 
                List<Method> methodes = getMethode(controlleurs.get(i));
                for (Method method : methodes) {
                    String verb = "GET";
                    Url getannotation = method.getAnnotation(Url.class);
                    String nom = getannotation.value();
                    if(method.isAnnotationPresent(Post.class)){
                        verb="POST";
                    }
                    verbaction=getMethodeMemeNom(controlleurs.get(i), nom);
                    zeanotte.put(nom,
                            new Mapping(controlleurs.get(i).getSimpleName(),
                                    method.getName(), controlleurs.get(i), verbaction));
                }

            }
        } catch (Exception w) {
            throw new ServletException("Erreur lors de l'initialisation du FrontController", w);
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        PrintWriter out = response.getWriter();
        ServletContext context = getServletContext();
        String chemin = context.getInitParameter("chemin");
        try {
            String requestUrl = request.getRequestURI();// maka an'ilay url
            requestUrl = requestUrl.substring(requestUrl.lastIndexOf('/') + 1);
            Mapping mapping = null;
            mapping = zeanotte.get(requestUrl);

            if (mapping != null) {
                Method method = mapping.getMethode(request,response);
                Class classe = mapping.getClasse();
                Object instance = classe.getDeclaredConstructor().newInstance();
                Object valiny = mapping.getReponse(request);
                if (method.isAnnotationPresent(RestAPI.class)) {
                    response.setContentType("application/json");
                    Gson gson=new Gson();
                    String val="";
                    if (valiny instanceof ModelView) {
                        ModelView modelView = (ModelView) valiny;
                        String url = modelView.getUrl();
                        HashMap<String, Object> data = modelView.getData();
                        val=gson.toJson(data);
                    }else{
                        val=gson.toJson(valiny);

                    }
                    out.println(val);
                }
                else{
                    if (valiny instanceof String) {
                        out.println("<h1>URL: " + requestUrl + "</h1>");
                        out.println("<p>Class: " + mapping.getClassName() + "</p>");
                        out.println("<p>Retour: " + mapping.retour() + "</p>");
                        out.println("<p>Method: " + mapping.getMethodName() + "</p>");
                    } else if (valiny instanceof ModelView) {
                        ModelView modelView = (ModelView) valiny;
                        String url = modelView.getUrl();
                        HashMap<String, Object> data = modelView.getData();
                        for (Map.Entry<String, Object> entry : data.entrySet()) {
                            request.setAttribute(entry.getKey(), entry.getValue());
    
                        }
                        request.getRequestDispatcher(url).forward(request, response);
    
                    } else {
                        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Non reconnue.");
    
                    }
                } 
            } else {
                out.println("<h1>No Methode sur : " + requestUrl + "</h1>");
            }
        } catch (Exception e) {
            response.sendError(405,e.getMessage());
        } finally {
            out.close();
        }
    }

    public List<Class> scan(String chemin) throws Exception {
        List<Class> liste = new ArrayList<Class>();
        // liste.add(chemin);
        try {
            String cheminRepertoire = chemin.replace('.', '/');
            URL urPackage = Thread.currentThread().getContextClassLoader().getResource(cheminRepertoire);
            if (urPackage != null) {
                File directory = new File(urPackage.getFile());
                File[] fichiers = directory.listFiles();
                if (fichiers != null) {
                    for (File fichier : fichiers) {
                        if (fichier.isFile() && fichier.getName().endsWith(".class")) {
                            String nomClasse = fichier.getName().substring(0, fichier.getName().length() - 6);
                            String nomCompletClasse = chemin + "." + nomClasse;
                            Class class1 = Class.forName(nomCompletClasse);
                            if (class1.isAnnotationPresent(Annote.class)) {
                                Annote annotation = (Annote) class1.getAnnotation(Annote.class);
                                if (annotation.value().equalsIgnoreCase("Controlleur")) {
                                    // liste.add(nomClasse + ".class");
                                    liste.add(class1);
                                }
                            }
                        } else if (fichier.isDirectory()) {
                            List<Class> li = scan(cheminRepertoire + "." + fichier.getName());
                            liste.addAll(li);
                        }
                    }
                }

            } else {
                Exception ex = new Exception("Tsisy package");
                throw ex;
            }

        } catch (Exception e) {
            throw e;
        }
        if (liste.size() == 0) {
            Exception e = new Exception("Tsisy controlleur");
            throw e;
        }
        return liste;
    }

    public List<Method> getMethode(Class<?> test) {
        List<Method> liste = new ArrayList<Method>();
        Method[] methodes = test.getDeclaredMethods();
        for (Method method : methodes) {
            if (method.isAnnotationPresent(Url.class)) {
                liste.add(method);
            }
        }
        return liste;
    }
    List<VerbAction> getMethodeMemeNom(Class <?> nomClass,String nomMethode) throws Exception{
        List<VerbAction> retour=new ArrayList<>();
        List<Method> methodes=getMethode(nomClass);
        List<String> listeNom = new ArrayList<>();
        for(Method methode : methodes){
            String verb="GET";
            Url url = methode.getAnnotation(Url.class);
            String nomUrl=url.value(); 
            if (nomUrl.equalsIgnoreCase(nomMethode)) {
                if (methode.isAnnotationPresent(Post.class)) {
                    verb="POST";   
                }
                String concateNomVerb=nomUrl+verb;
                if (listeNom.contains(concateNomVerb)) {
                    Exception e=new Exception("Methode "+nomUrl+ " double");
                }
                listeNom.add(concateNomVerb);
                VerbAction verbAction=new VerbAction(verb,methode);
                retour.add(verbAction);
            }
        }
        return retour;
    }
}