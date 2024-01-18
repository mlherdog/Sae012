import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Classification {


    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
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

    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier,int compteurComp) {
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
            PaireChaineEntier resultat;


            // vecteur contenant toutes les categories et leur associe un entier
            while (i<categories.size()){
                vCat_Reusite.add(new PaireChaineEntier(categories.get(i).getNom(),0));
                i++;
            }//pourcentage_reusite.size == categories.size
            i=0;

            while (i<depeches.size()-1){
                depeche_temp = depeches.get(i);// stockage dans depeche temps les info de la depeche qui vas etre traiter
                y =0;// remise des variable a la situation initiale
                score_total = new ArrayList<>();

                while (y<categories.size()){

                    PaireChaineEntier temp_P =new PaireChaineEntier(categories.get(y).getNom(),categories.get(y).score(depeche_temp));
                    score_total.add(temp_P);
                    y++;

                }// la depenche qui est entrain d'etre tratee a une note pour chaque categorie

                depecheTempChainemax = UtilitairePaireChaineEntier.chaineMax(score_total);// calcule de la categorie avec le score le plus élever

                file.write(depeches.get(i).getId()+":"+depecheTempChainemax+"\n");//ecriture de la categorie detreminer pour a depeche en cour de traitement, dans le ficher donnee

                if(depecheTempChainemax.compareTo(depeches.get(i).getCategorie()) == 0){// actualisation de vCat_reusite en fonction d ela verrifiacation du resultat
                    resultat =new PaireChaineEntier(depecheTempChainemax,vCat_Reusite.get(UtilitairePaireChaineEntier.indicePourChaine(vCat_Reusite, depecheTempChainemax)).getEntier()+1);
                    vCat_Reusite.set(UtilitairePaireChaineEntier.indicePourChaine(vCat_Reusite, depecheTempChainemax), resultat);
                    compteurComp += 1;
                }
                i++;

            }// toutes les depeches ont été traitee et le resultat ecrit dans le fichier donee
            file.write("----------------- % de bonnes reponses par categorie puis moyenne d e toutes les categories ----------------- \n");
            // pourcentage de bonne reponses pour la categorie de chaque dépeche
            i = 0;
            while (i< vCat_Reusite.size()){

                file.write(vCat_Reusite.get(i).getChaine()+":"+vCat_Reusite.get(i).getEntier()+"%\n");
                compteur_reussi = compteur_reussi + vCat_Reusite.get(i).getEntier();
                i++;

            }// les resultat sont calculer pour chaque categorie

            file.write("\nMOYENNE:"+UtilitairePaireChaineEntier.moyenne(vCat_Reusite)+"%\n");
            file.close();

            System.out.println(">>>> Fichier reponse crée sous le nom :     "+nomFichier+"\n");

        } catch (IOException e) {
            System.out.println(">>>> Probleme lors de l'ecriture dans le fichier");
            throw new RuntimeException(e);
        }
    }


    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie,int compteurComp) {
        //retourne une ArrayList<PaireChaineEntier> contenant tous les mots présents dans au moins
        //une dépêche de la catégorie categorie. les entiers, stockerons les scores associés à chaque mot
        //et dans un premier temps, ils seront initialisés a  0
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();

        int i=0;
        int y;
        int indice_mot;

        while (i<depeches.size()){

            if(depeches.get(i).getCategorie().compareTo(categorie) == 0){
                compteurComp += 1;
                y =0;

                while (y<depeches.get(i).getMots().size()){

                    indice_mot = UtilitairePaireChaineEntier.dichoIndice(resultat,depeches.get(i).getMots().get(y),compteurComp);

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
        return resultat;
    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire,int compteurComp) {
//        met à jour les scores des mots présents dans dictionnaire. Lorsqu'un mot présent dans
//        dictionnaire apparaît dans une dépêche de depeches, son score est : décrémenté si la dépêche
//        n'est pas dans la catégorie categorie et incrémenté si la dépêche est dans la catégorie categorie.
        int i=0;
        int j;
        int indiceMot;
        int changement_score;

        while (i<depeches.size()){

            j = 0;

            while (j<depeches.get(i).getMots().size()){

                indiceMot = UtilitairePaireChaineEntier.dichoIndice(dictionnaire,depeches.get(i).getMots().get(j),compteurComp);

                if (indiceMot >= 0) {	// On regarde si le mot courant est présent dans le dictionnaire
					if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
						changement_score = 2;
                        compteurComp +=1;
					} else {
						changement_score = -1;
					}

					dictionnaire.set(indiceMot, new PaireChaineEntier(dictionnaire.get(indiceMot).getChaine(), dictionnaire.get(indiceMot).getEntier() + changement_score));
                    // ont met le score a jour
				}
                compteurComp +=1;
                j++;
            }// tout les mots de la la depeche ont été examiner et leur score a été marquer dans dictionaire.getEntier

            i++;
        }// toutes les depeche ont été examiner

    }


    public static int poidsPourScore(int score) {
        // retourne une valeur de poids (1,2 ou 3) en fonction du score score
        // PROVISOIREMENT: score > 3 = poid de 3
        //                 score > -6 = poid de 2
        //                 sinon score de 1
        if (score > 5) {
            return 3;
        } else if (score > -5) {
            return 2;
        } else {
            return 1;
        }
    }
    public static void generationLexique(ArrayList<Depeche> depeches, String categorie,int compteurComparaison) {
//        Crée pour la catégorie categorie le fichier lexique de nom nomFichier à partir du vecteur de
//        Dépêches depeches. Cette méthode construit une ArrayList<PaireChaineEntier> avec
//        initDico, puis met à jour les scores dans ce vecteur avec calculScores et enfin utilise le vecteur
//        résultant pour créer un fichier lexique en utilisant la fonction poidsPourScore.
        try{

            FileWriter file = new FileWriter("lex_automatique_"+categorie+".txt");// Creation du fichier lexique

            ArrayList<PaireChaineEntier> dictionaire = initDico(depeches,categorie,compteurComparaison); // construction du vecteur avec les mots contrnu dans les depeches

            calculScores(depeches,categorie,dictionaire,compteurComparaison);// affectation d'un score pour chaque mots

            int i=0;
            while (i< dictionaire.size()){
                  file.write(dictionaire.get(i).getChaine()+":"//le mot a la position i du dictionaire
                          +poidsPourScore(dictionaire.get(i).getEntier())+"\n");// le poid du mot i calculer grace a sont score et le score du mot I
              i++;
            }// toute les paire poid mot ont été ecrites dans le fivhier file
            file.close();

        } catch (IOException e) {
            System.out.println(">>>> Probleme lors de l'ecriture dans le fichier");
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        //Chargement des dépêches en mémoire
       // System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./test.txt");
        System.out.println(">>>> Dépéches chargées\n");
/*
        for (int i = 0; i < depeches.size(); i++) {
            depeches.get(i).afficher();
        }

        // creation d'une categorie (5.2 c)
        Categorie test_sport = new Categorie("Sport");

        // initialisation puis affichage de son registre (5.2 c)
        test_sport.initLexique("lexique_SPORTS");
        System.out.println("lexique de la categorie Sport: ");
        for(int i=0;i<test_sport.getLexique().size();i++) {
            System.out.println(test_sport.getLexique().get(i).getChaine() + " : " + test_sport.getLexique().get(i).getEntier());
        }

        System.out.println("-------------------------------------------------------------------------------------------------------\n");
        //affichage du poid d'un mot initialiser par saisie (5.2 e)
        Scanner lecteur = new Scanner(System.in);
        String mot_saisi;
        int resultat;

        System.out.println(" le poids de quel mot voudriez vous connaitre ?");
        mot_saisi = lecteur.nextLine();

        resultat = UtilitairePaireChaineEntier.entierPourChaine(test_sport.getLexique(),mot_saisi);
        if(resultat == 0){
            System.out.println(">>> le mot saisie n'est pas present dans le lexique");
        }else {
            System.out.println("le poid  de "+mot_saisi+" est de "+resultat);
        }

        System.out.println("-------------------------------------------------------------------------------------------------------\n");
        // score de plusieure depeche pour la categorie sport (5.3)

        int num_depeche=400;

        while (num_depeche < 430){
            System.out.println("le score de la depeche "+num_depeche+" est "+test_sport.score(depeches.get(num_depeche)));
            num_depeche += 2;
        }

        //5.4
*/

       // Creation de 5 catégories puis Creation d'une Arraylist de categorie
        Categorie sport = new Categorie("SPORTS");
        Categorie culture = new Categorie("CULTURE");
        Categorie politique = new Categorie("POLITIQUE");
        Categorie economique = new Categorie("ECONOMIE");
        Categorie environnement_science = new Categorie("ENVIRONNEMENT-SCIENCES");

        ArrayList<Categorie> lesCategories =new ArrayList<>(Arrays.asList(sport,culture,politique,economique,environnement_science));
        System.out.println(">>>> Categories crées\n");

        int compteur_global =0;
        //initialisation a partir delexique genere automitiquement
        generationLexique(depeches,"SPORTS",compteur_global);
        generationLexique(depeches,"CULTURE",compteur_global);
        generationLexique(depeches,"POLITIQUE",compteur_global);
        generationLexique(depeches,"ECONOMIE",compteur_global);
        generationLexique(depeches,"ENVIRONNEMENT-SCIENCES",compteur_global);

        for(int i=0;i<lesCategories.size();i++){
            lesCategories.get(i).initLexique("lex_automatique_"+lesCategories.get(i).getNom()+".txt");
        }

/*
        //initialisation a partir de lexique crée mauellement
        for(int i=0;i<lesCategories.size();i++){
           lesCategories.get(i).initLexique("lexique_"+lesCategories.get(i).getNom());
        }

        System.out.println(">>>> lexiques initialisés\n");
*/


/*
        // Pour une depeche construire un vecteur avec son score pour chaque categorie
        ArrayList<PaireChaineEntier> score_total = new ArrayList<>();
        int i=0;
        int temp_int;
        String temp_st;

        Depeche depeche_donnee = depeches.get(111); // pour l'exemple nous avons choisit arbitrairement la depeche 111
        while(i < lesCategories.size()){
            temp_st = lesCategories.get(i).getNom();
            temp_int = lesCategories.get(i).score(depeche_donnee);
            PaireChaineEntier temp_P =new PaireChaineEntier(temp_st,temp_int);
            score_total.add(temp_P);
            i++;
        }

        System.out.println("Pour la depeche "+depeche_donnee.getId()+" la categorie qui a le meuilleur score est "+UtilitairePaireChaineEntier.chaineMax(score_total));
*/

        // creation d'un fichier reponse
       classementDepeches(depeches,lesCategories,"fichier_resultat.txt",compteur_global);
       long endTime = System.currentTimeMillis();
       System.out.println("votre saisie a été réalisée en : " + (endTime-startTime) + "ms");
       System.out.println("et a necessité : "+compteur_global+" comparaison.");
    }
}

