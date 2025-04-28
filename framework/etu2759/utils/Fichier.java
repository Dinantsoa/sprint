package framework.etu2759.utils;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import javax.servlet.http.Part;

public class Fichier {
    private String nomFichier;
    private byte[] contenu;

    // Constructeur
    public Fichier(String nomFichier, byte[] contenu) {
        this.nomFichier = nomFichier;
        this.contenu = contenu;
    }

    // Getter et Setter pour nomFichier
    public String getNomFichier() {
        return nomFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
    }

    // Getter et Setter pour contenu
    public byte[] getContenu() {
        return contenu;
    }

    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }

    // Méthode pour afficher un aperçu du contenu sous forme de chaîne de caractères
    public String getContenuApercu() {
        return contenu != null ? new String(contenu) : "Aucun contenu";
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Fichier{" +
                "nomFichier='" + nomFichier + '\'' +
                ", contenu=" + Arrays.toString(contenu) +
                '}';
    }
    public static Fichier convertirPartEnFichier(Part part) throws IOException {
        if (part == null || part.getSubmittedFileName() == null) {
            throw new IllegalArgumentException("Le fichier est vide.");
        }

        String nomFichier = Paths.get(part.getSubmittedFileName()).getFileName().toString(); // Récupération du nom du fichier
        byte[] contenu = lireContenu(part.getInputStream()); // Lecture des bytes

        return new Fichier(nomFichier, contenu);
    }

    // Lecture du contenu en bytes (compatible avec Java 8+)
    private static byte[] lireContenu(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }
}
