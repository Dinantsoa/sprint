package mg.itu.prom16;

import java.io.File;
import java.io.PrintWriter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            int isa = 1;
            List<String> listeNom = new ArrayList<>();

            for (int i = 0; i < controlleurs.size(); i++) {
                List<Method> methodes = getMethode(controlleurs.get(i));
                for (Method method : methodes) {
                    Get getannotation = method.getAnnotation(Get.class);
                    String nom = getannotation.value();
                    if (listeNom.contains(nom)) {
                        Exception e = new Exception("Efa niverina ny " + nom);
                        throw e;
                    }
                    listeNom.add(nom);
                    zeanotte.put(nom,
                            new Mapping(controlleurs.get(i).getSimpleName(),
                                    method.getName(), controlleurs.get(i), method));
                    isa++;
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

            out.println("<html>");
            out.println("<head><title>URL Mapping</title></head>");
            out.println("<body>");
            Mapping mapping = null;
            mapping = zeanotte.get(requestUrl);

            if (mapping != null) {
                Method method = mapping.getMethode();
                Class classe = mapping.getClasse();
                Object instance = classe.getDeclaredConstructor().newInstance();
                Object valiny = mapping.getReponse(request);
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

            } else {
                out.println("<h1>No Methode sur : " + requestUrl + "</h1>");
            }
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            out.println(e.getMessage());
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
            if (method.isAnnotationPresent(Get.class)) {
                liste.add(method);
            }
        }
        return liste;
    }

}