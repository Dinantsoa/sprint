package framework;


public class Erreur {
    String message;
    String valeur;
    
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getValeur() {
        return valeur;
    }
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }
    
    public Erreur()
    {
        
    }
    public Erreur(String message, String valeur) {
        this.message = message;
        this.valeur = valeur;
    }
    public void manampyErreur(Erreur erreur)
    {
        if (erreur == null) {
            // Ignorer ou gérer le cas où 'erreur' est null
            return;
        }
        if (this.getValeur().contains(erreur.getValeur()) ==false) {
            this.setMessage(this.getMessage() + " " + erreur.getMessage());
        }
    }

    


    
}