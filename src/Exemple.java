package test;

import framework.*;

@Annote(value = "Controlleur")
public class Exemple {
    MySession session;

    @Get(value = "get")
    public String home() {
        return "Bonjour";
    }

    @Get(value = "get3")
    public String about() {
        return "Sprint3";
    }

    @Get(value = "int")
    public int nb() {
        return 0;
    }

    @Get(value = "model")
    public ModelView li(@Param(value = "emp") Employer emp, @Param(value = "nom") String nom,
            @Param(value = "age") int age) {
        ModelView valiny = new ModelView();
        valiny.setUrl("sprint7.jsp");
        String a = "Employer " + emp.getName() + " Nom " + nom + "  Age " + age;
        valiny.AddObject("model", a);
        return valiny;
    }

    @Get(value = "model1")
    public ModelView list(Employer emp, @Param(value = "nom") String nom,
            @Param(value = "age") int age) {
        ModelView valiny = new ModelView();
        valiny.setUrl("sprint7.jsp");
        String a = "Employer " + emp.getName() + " Nom " + nom + "  Age " + age;
        valiny.AddObject("model", a);
        return valiny;
    }

    @Get(value = "login")
    public ModelView login(@Param(value = "nom") String nom, @Param(value = "adress") String adress,
            @Param(value = "mdp") String mdp) {

        ModelView val = new ModelView();
        val.setUrl("sprint8.jsp");
        if (session != null) {
            session.add("nom", nom);
            String a = " Nom " + String.valueOf(session.get("nom"));
            val.AddObject("login", a);
        }
        return val;
    }

    @Get(value = "json")
    @RestAPI
    public String test() {
        String valiny="FORMAT JISON";
        return valiny;
    }
    @Get(value = "jsonmv")
    @RestAPI
    public ModelView json() {
        ModelView valiny = new ModelView();
        valiny.AddObject("Prenom", "Dinantsoa");
        valiny.AddObject("Age", 23);
        return valiny;
        
    }

    //Sprint10
    // @Url(value = "affiche")
    @Url(value = "affiche")
    public ModelView affiche(@Param(value = "nom") String nom,
            @Param(value = "age") int age) {
        ModelView valiny = new ModelView();
        valiny.setUrl("affiche.jsp");
        String a =" Nom " + nom + "  Age " + age;
        valiny.AddObject("affiche", a);
        return valiny;
    }

    @Post
    @Url(value = "sp10")
    public ModelView form() {
        ModelView valiny = new ModelView();
        valiny.setUrl("sprint10.jsp");
        return valiny;
    }

}