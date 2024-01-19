import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Classification {


    public static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }

    public static int classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier,int compteurComp) {
        // calcule le score de chaque categorie pour chaque depeche et ecrit
        // dans un fichier donnee le nom de la catégorie ayant le plus grand score
        try {
            FileWriter file = new FileWriter(nomFichier);
            ArrayList<PaireChaineEntier> score_total;
            ArrayList<PaireChaineEntier> vCat_Reusite =new ArrayList<>();

            int i = 0;
            int y;
            int compteur_reussi=0;
            Depeche depeche_temp;
            String depecheTempChainemax;
            Paireresultatcompteur resultat_compteur;
            PaireChaineEntier resultat;


            // vecteur contenant toutes les categories et leur associe un entier
            while (i<categories.size()){
                vCat_Reusite.add(new PaireChaineEntier(categories.get(i).getNom(),0));
                i++;
            }//pourcentage_reusite.size == categories.size
            i=0;

            while (i<depeches.size()){
                depeche_temp = depeches.get(i);// stockage dans depeche temps les info de la depeche qui vas etre traiter
                y =0;// remise des variable a la situation initiale
                score_total = new ArrayList<>();

                while (y<categories.size()){
                    resultat_compteur = categories.get(y).score(depeche_temp);
                    PaireChaineEntier temp_P =new PaireChaineEntier(categories.get(y).getNom(),(int)resultat_compteur.getResultat());

                    score_total.add(temp_P);
                    compteurComp += resultat_compteur.getCompteur();
                    y++;

                }// la depenche qui est entrain d'etre traitee a une note pour chaque categorie

                depecheTempChainemax = UtilitairePaireChaineEntier.chaineMax(score_total);// calcule de la categorie avec le score le plus élever

                file.write(depeches.get(i).getId()+":"+depecheTempChainemax+"\n");//ecriture de la categorie detreminer pour a depeche en cour de traitement, dans le ficher donnee

                if(depecheTempChainemax.compareTo(depeches.get(i).getCategorie()) == 0){// actualisation de vCat_reusite en fonction d ela verrifiacation du resultat
                    resultat =new PaireChaineEntier(depecheTempChainemax,vCat_Reusite.get(UtilitairePaireChaineEntier.indicePourChaine(vCat_Reusite, depecheTempChainemax)).getEntier()+1);
                    vCat_Reusite.set(UtilitairePaireChaineEntier.indicePourChaine(vCat_Reusite, depecheTempChainemax), resultat);
                    compteurComp += 1;
                }
                i++;

            }// toutes les depeches ont été traitee et le resultat ecrit dans le fichier donnee
            file.write("----------------- % de bonnes reponses par categorie puis moyenne de toutes les categories ----------------- \n");
            // pourcentage de bonne reponses pour la categorie de chaque dépeche
            i = 0;
            while (i< vCat_Reusite.size()){

                file.write(vCat_Reusite.get(i).getChaine()+":"+vCat_Reusite.get(i).getEntier()+"%\n");
                compteur_reussi = compteur_reussi + vCat_Reusite.get(i).getEntier();
                i++;
            }// les resultat sont calculer pour chaque categorie

            file.write("MOYENNE:"+UtilitairePaireChaineEntier.moyenne(vCat_Reusite)+"%\n");
            file.close();

            System.out.println("    Les resultat ont été ecrit dans le fichier : "+nomFichier);

        } catch (IOException e) {
            System.out.println(">>>> Probleme lors de l'ecriture dans le fichier");
            throw new RuntimeException(e);
        }
        return compteurComp;
    }


    public static Paireresultatcompteur<ArrayList<PaireChaineEntier>>  initDico(ArrayList<Depeche> depeches, String categorie) {
        //retourne une ArrayList<PaireChaineEntier> contenant tous les mots présents dans au moins
        //une dépêche de la catégorie categorie. les entiers, stockerons les scores associés à chaque mot
        //et dans un premier temps, ils seront initialisés a  0
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();

        int compteurComp=0;
        int i=0;
        int y;
        Paireresultatcompteur indince_mot_plus_comparaison;
        int indice_mot;

        while (i<depeches.size()){

            if(depeches.get(i).getCategorie().compareTo(categorie) == 0){
                compteurComp += 1;
                y =0;

                while (y<depeches.get(i).getMots().size()){
                    indince_mot_plus_comparaison = UtilitairePaireChaineEntier.dichoIndice(resultat,depeches.get(i).getMots().get(y));
                    indice_mot = (int)indince_mot_plus_comparaison.getResultat();
                    compteurComp += indince_mot_plus_comparaison.getCompteur();

                    if(indice_mot < 0) {//si le mot n'est pas deja present dans le vecteur
                        resultat.add(-indice_mot-1, new PaireChaineEntier(depeches.get(i).getMots().get(y), 0));
                    }
                    compteurComp +=1;
                    y++;
                }
            }// tout les mots de la depeche ont été analyser
            i++;
            compteurComp +=1;
        }//toute les mots de toute les requettes qui appartienne a categorie (hors doublou)ont été mit dans resultat

        int indice_pour_double_point = UtilitairePaireChaineEntier.indicePourChaine(resultat,":");
        if(indice_pour_double_point > 0){
            resultat.remove(indice_pour_double_point);
        }// ce code est pour supprimer ":" du lexique car il posse des probleme lors de
        // l'initialisation des lexique et qu'il n'as pas d'intéret pour les lexique car sont score est de 1 pour toute les categories

        return new Paireresultatcompteur<>(resultat,compteurComp);
    }

    public static int calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
//        met à jour les scores des mots présents dans dictionnaire. Lorsqu'un mot présent dans
//        dictionnaire apparaît dans une dépêche de depeches, son score est : décrémenté si la dépêche
//        n'est pas dans la catégorie categorie et incrémenté si la dépêche est dans la catégorie categorie.

        int i=0;
        int compteurComp= 0;
        int j;
        int indiceMot;
        int changement_score;
        Paireresultatcompteur<Integer> resultat;

        while (i<depeches.size()){

            j = 0;

            while (j<depeches.get(i).getMots().size()){

                resultat = UtilitairePaireChaineEntier.dichoIndice(dictionnaire,depeches.get(i).getMots().get(j));// stocke dans resultat l 'indice du mot et le nb de comparaison
                indiceMot = resultat.getResultat();
                compteurComp += resultat.getCompteur();

                if (indiceMot >= 0) {	// On regarde si le mot courant est présent dans le dictionnaire
					if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
						changement_score = 1;
                        compteurComp +=1;
					} else {
						changement_score = -6;
					}

					dictionnaire.set(indiceMot, new PaireChaineEntier(dictionnaire.get(indiceMot).getChaine(), dictionnaire.get(indiceMot).getEntier() + changement_score));
                    // ont met le score a jour
				}
                compteurComp +=1;
                j++;
            }// tout les mots de la la depeche ont été examiner et leur score a été marquer dans dictionaire.getEntier

            i++;
        }// toutes les depeche ont été examiner
        return compteurComp;

    }


    public static int poidsPourScore(int score) {
        // retourne une valeur de poids (1,2 ou 3) en fonction du score score
        // PROVISOIREMENT: score > 3 = poid de 3
        //                 score > -6 = poid de 2
        //                 sinon score de 1
        if (score > 3) {
            return 3;
        } else if (score > -6) {
            return 2;
        } else {
            return 1;
        }
    }
    public static int generationLexique(ArrayList<Depeche> depeches, String categorie) {
//        Crée pour la catégorie categorie le fichier lexique de nom nomFichier à partir du vecteur de
//        Dépêches depeches. Cette méthode construit une ArrayList<PaireChaineEntier> avec
//        initDico, puis met à jour les scores dans ce vecteur avec calculScores et enfin utilise le vecteur
//        résultant pour créer un fichier lexique en utilisant la fonction poidsPourScore.
        int compteurComparaison=0;
        try{
            FileWriter file = new FileWriter("lex_automatique_"+categorie+".txt");// Creation du fichier lexique


            Paireresultatcompteur<ArrayList<PaireChaineEntier>> resultat_vecteur_plus_comparaison =initDico(depeches,categorie);

            ArrayList<PaireChaineEntier> dictionaire = resultat_vecteur_plus_comparaison.getResultat(); // construction du vecteur avec les mots contrnu dans les depeches
            compteurComparaison += resultat_vecteur_plus_comparaison.getCompteur();

            compteurComparaison += calculScores(depeches,categorie,dictionaire);// affectation d'un score pour chaque mots

            int i=0;
            while (i< dictionaire.size()){
                    file.write(dictionaire.get(i).getChaine() + ":"//le mot a la position i du dictionaire
                            + poidsPourScore(dictionaire.get(i).getEntier()) + "\n");// le poid du mot i calculer grace a sont score et le score du mot I

              i++;
            }// toute les paire poid mot ont été ecrites dans le fivhier file
            file.close();
        } catch (IOException e) {
            System.out.println(">>>> Probleme lors de l'ecriture dans le fichier");
            throw new RuntimeException(e);
        }

        return compteurComparaison;
    }

    public static void main(String[] args) {
        // Ce main crée un fichier resultat crée avec des lexique entrainer sur depeches.txt
        // La classification par contre ce fait a partir du fichier test.txt
        //  Pour avoir un fichier resultat crée a partir de nos propre lexique, il faut run le main de la classe ClassificationOld

        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");
        ArrayList<Depeche> test = lectureDepeches("./test.txt"); //Chargement des dépêches en mémoire
        System.out.println(">>>> Dépéches chargées et fichier test chargés \n");

        System.out.println(">>>> creation des lexiques\n");
        long startTime_lexique = System.currentTimeMillis();
       // Creation de 5 catégories puis Creation d'une Arraylist de categorie
        Categorie sport = new Categorie("SPORTS");
        Categorie culture = new Categorie("CULTURE");
        Categorie politique = new Categorie("POLITIQUE");
        Categorie economique = new Categorie("ECONOMIE");
        Categorie environnement_science = new Categorie("ENVIRONNEMENT-SCIENCES");

        ArrayList<Categorie> lesCategories =new ArrayList<>(Arrays.asList(sport,culture,politique,economique,environnement_science));

        int compteur_global =0;
        int i=0;
        //initialisation a partir de lexique génerer automitiquement
        while (i<lesCategories.size()){
            compteur_global += generationLexique(depeches,lesCategories.get(i).getNom());
            lesCategories.get(i).initLexique("lex_automatique_"+lesCategories.get(i).getNom()+".txt");
            i++;
        }

        long endTime_lexique = System.currentTimeMillis();
        System.out.println("    Les lexique ont été crée en : " + (endTime_lexique-startTime_lexique) + "ms");
        System.out.println("    Leur creation a demander : "+compteur_global+" comparaisons");

        compteur_global = 0;
        // creation d'un fichier reponse
        System.out.println("\n>>>> creation du fichier reponse\n");

        long startTime_classement = System.currentTimeMillis();
        compteur_global += classementDepeches(test,lesCategories,"fichier_resultat.txt",compteur_global);
        long endTime_classement = System.currentTimeMillis();

        System.out.println("    Le classemnt et le calul des bonnes reponses on été realiser en : " + (endTime_classement-startTime_classement) + "ms");
        System.out.println("    Ils ont necessité : "+compteur_global+" comparaisons.");
    }
}

