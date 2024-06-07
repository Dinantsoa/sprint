package framework;

import java.util.HashMap;

public class ModelView {
    String url;
    HashMap<String, Object> data = new HashMap<>();

    public void setUrl(String url) {
        this.url = url;
    }

    public void AddObject(String nom, Object objet) {
        data.put(nom, objet);
    }

    public String getUrl() {
        return this.url;
    }

    public HashMap geData() {
        return this.data;
    }
}