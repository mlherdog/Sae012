import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Presentation_oral {
    public static void main(String[] args) {
        // cette classe n'est pas faite pour etre rendu elle est juste la pour faire une demonstration lors de la presentation orale

        System.out.println("I.   Fichier resultat generé grace aux lexiques crée que nous avons ecrit\n");

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

        System.out.println(">>>> creation du fichier reponse\n");

        long startTime_classement = System.currentTimeMillis();
        compteur_global += Classification.classementDepeches(depeches,lesCategories,"fichier_resultat_Old.txt",compteur_global);
        long endTime_classement = System.currentTimeMillis();
        System.out.println("\n    Les resultats de bonne categorisation de depeche sont de :");
        try {
            int i = 0;
            String resultat=" ";
            FileInputStream file = new FileInputStream("fichier_resultat_Old.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                if(i>501){
                    resultat= (resultat+"\n    "+ scanner.nextLine());
                }else {
                    scanner.nextLine();
                }
                i++;
            }
            System.out.println(resultat+"\n");
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier resultat n'as pas put étre lut");
        }
        System.out.println("    Le classemnt et le calul des bonnes reponses on été realiser en : " + (endTime_classement-startTime_classement) + "ms");
        System.out.println("    Ils ont necessité : "+compteur_global+" comparaisons.");


        depeches = Classification.lectureDepeches("./depeches.txt");
        ArrayList<Depeche> test = Classification.lectureDepeches("./test.txt"); //Chargement des dépêches en mémoire

        System.out.println("\n II.   Fichier resultat generé grace aux lexiques crée automatiquement \n");
        System.out.println(">>>> creation des lexiques\n");
        long startTime_lexique = System.currentTimeMillis();
        compteur_global =0;
        int i=0;
        //initialisation a partir de lexique génerer automitiquement
        while (i<lesCategories.size()){
            compteur_global += Classification.generationLexique(depeches,lesCategories.get(i).getNom());
            lesCategories.get(i).initLexique("lex_automatique_"+lesCategories.get(i).getNom()+".txt");
            i++;
        }

        long endTime_lexique = System.currentTimeMillis();
        System.out.println("    Les lexique ont été crée en : " + (endTime_lexique-startTime_lexique) + "ms");
        System.out.println("    Leur creation a demander : "+compteur_global+" comparaisons");

        compteur_global = 0;
        // creation d'un fichier reponse
        System.out.println("\n>>>> creation du fichier reponse\n");

        long startTime_classement2 = System.currentTimeMillis();
        compteur_global += Classification.classementDepeches(test,lesCategories,"fichier_resultat.txt",compteur_global);
        long endTime_classement2 = System.currentTimeMillis();

        System.out.println("\n    Les resultats de bonne categorisation de depeche sont de :");
        try {
            i = 0;
            String resultat=" ";
            FileInputStream file = new FileInputStream("fichier_resultat.txt");
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                if(i>501){
                    resultat= (resultat+"\n    "+ scanner.nextLine());
                }else {
                    scanner.nextLine();
                }
                i++;
            }
            System.out.println(resultat+"\n");
        } catch (FileNotFoundException e) {
            System.out.println("Le fichier resultat n'as pas put étre lut");
        }
        System.out.println("    Le classemnt et le calul des bonnes reponses on été realiser en : " + (endTime_classement2-startTime_classement2) + "ms");
        System.out.println("    Ils ont necessité : "+compteur_global+" comparaisons.");
    }
}