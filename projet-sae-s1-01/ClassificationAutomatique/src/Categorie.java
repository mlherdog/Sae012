import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Categorie {

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique; //le lexique de la catégorie

    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
    }


    public String getNom() {
        return nom;
    }


    public  ArrayList<PaireChaineEntier> getLexique() {
        return lexique;
    }


    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {
        try{
            // permet de lire le fichier nomFichier
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            String ligne;
            int indice_double_point;
            String mot;
            String poid_st; // poid du mot en chaine de character e
            int poid_int;// poid du mot en integer
            PaireChaineEntier resultat;

            while (scanner.hasNextLine()){
                ligne = scanner.nextLine();
                if(!ligne.isEmpty()) { // pour gerer si jamais il y a une lign evide dans le fichier
                    indice_double_point = ligne.indexOf(":"); // indice_double_point = la separation entre le mot et sont poids
                    mot = ligne.substring(0, indice_double_point);
                    poid_st = ligne.substring(indice_double_point + 1);

                    if(poid_st.indexOf(":") != -1){
                        poid_st = poid_st.substring(poid_st.indexOf(":")+1);
                    }

                    poid_int = Integer.parseInt(poid_st);

                    resultat = new PaireChaineEntier(mot, poid_int);
                    if(lexique == null){
                        lexique = new ArrayList<>();
                        lexique.add(0,resultat);
                    }else {
                        lexique.add(resultat);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(">>> Probleme avec le nom du fichier en entrée");
        }
    }


    //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {
        int i=0;
        int score =0;

        while (i< d.getMots().size()){
            score += UtilitairePaireChaineEntier.entierPourChaine(lexique,d.getMots().get(i));
            i++;
        }// invariant chaque ellements de d.getMot ont été comparer a tout les ellement de lexique
        return score;
    }


}
