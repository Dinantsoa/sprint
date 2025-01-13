package framework;
import java.util.HashMap;
import java.util.Map;

public class AllErreur{
    HashMap<String,Erreur> listeErreur;

    public HashMap<String,Erreur> getListeErreur() {
        return listeErreur;
    }

    public void setListeErreur(HashMap<String, Erreur> listeErreur) {
        this.listeErreur = listeErreur;
    }
    public void ajouterListeErreur(String name,Erreur apidirina)
    {
        if (listeErreur==null) {
            listeErreur= new HashMap<String,Erreur>();
        }
        listeErreur.put(name, apidirina);

    }

    public Erreur getErreur(String jerena)
    {
        Erreur valiny=listeErreur.get(jerena);
        return valiny;
    }
    public boolean misyErreur(String jerena)
    {
        Erreur li=getErreur(jerena);
        return li.getMessage()!=null;
    }
    public String erreur(String jerena)
    {
        String valiny=null;
        Erreur li=getErreur(jerena);
        valiny=li.getMessage();
        return valiny;

    }
    public String getValeur(String jerena)
    {
        
        Erreur li=getErreur(jerena);
        String valiny=li.getValeur();
        if (valiny==null) {
            valiny="";
        }
        return valiny;
        
    }
    public boolean misyErreur() {
        boolean valiny=false;
        if (listeErreur == null) {
            listeErreur = new HashMap<String, Erreur>();
        }
        int isa=0;
        int isaErreur=0;
        // Parcourir toutes les erreurs dans la HashMap
        for (Map.Entry<String, Erreur> entry : listeErreur.entrySet()) {
            // String key = entry.getKey();  // Clé de l'erreur
            Erreur valeur = entry.getValue();  

            // Vérifier ou traiter la valeur si nécessaire
            if (valeur != null) {
                isa++;
                if (valeur.getMessage()==null) {
                    isaErreur++;
                }

                
            
            }
        }
        valiny=isaErreur!=isa;

        // Si la liste d'erreurs contient des éléments, retourner `true`
        return valiny;
    }



    
    

}