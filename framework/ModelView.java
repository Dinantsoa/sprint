package framework;

import java.util.HashMap;

public class ModelView {
    String url;
    HashMap<String, Object> data = new HashMap<>();
    Boolean erreur;

    public void setErreur(Boolean erreur) {
        this.erreur = erreur;
    }

    public Boolean getErreur() {
        return erreur;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void AddObject(String nom, Object apidirina) {
        data.put(nom, apidirina);
    }

}