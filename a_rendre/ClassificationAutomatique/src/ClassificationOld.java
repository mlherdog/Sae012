import java.util.ArrayList;
import java.util.Arrays;

public class ClassificationOld {
    // cette classe sert a crée un fichier resultat a partir de lexique donnée. que l'ont a crée a la main.
    public static void main(String[] args) {
        ArrayList<Depeche> depeches = Classification.lectureDepeches("./depeches.txt");
        int compteur_global=0;
        Categorie sport = new Categorie("SPORTS");
        Categorie culture = new Categorie("CULTURE");
        Categorie politique = new Categorie("POLITIQUE");
        Categorie economique = new Categorie("ECONOMIE");
        Categorie environnement_science = new Categorie("ENVIRONNEMENT-SCIENCES");

        ArrayList<Categorie> lesCategories =new ArrayList<>(Arrays.asList(sport,culture,politique,economique,environnement_science));

        for(int i=0;i<lesCategories.size();i++){
            lesCategories.get(i).initLexique("lex_"+lesCategories.get(i).getNom()+".txt");
        }

        System.out.println("\n>>>> creation du fichier reponse\n");

        long startTime_classement = System.currentTimeMillis();
        compteur_global += Classification.classementDepeches(depeches,lesCategories,"fichier_resultat_Old.txt",compteur_global);
        long endTime_classement = System.currentTimeMillis();

        System.out.println("    Le classemnt et le calul des bonnes reponses on été realiser en : " + (endTime_classement-startTime_classement) + "ms");
        System.out.println("    Ils ont necessité : "+compteur_global+" comparaisons.");
    }
}
