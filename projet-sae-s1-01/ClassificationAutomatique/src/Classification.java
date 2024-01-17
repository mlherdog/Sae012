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

    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        // calcule le score de chaque categorie pour chaque depeche et ecrit
        // dans un fichier donnee le nom de la catégorie ayant le plus grand score
        int i = 1;
        int y;
        String temp_st;
        int temp_int;
        Depeche depeche_temp;
        ArrayList<PaireChaineEntier> score_total = new ArrayList<>();
        ArrayList<PaireChaineEntier> lesCategories_des_depeches = new ArrayList<>();

        try {
            FileWriter file = new FileWriter(nomFichier);
            while (i<depeches.size()-1){
                depeche_temp = depeches.get(i);// stockage dans depeche temps les info de la depeche qui vas etre traiter
                y =0;// remise des variable a la situation initiale
                score_total.clear();
                while (y<categories.size()){
                    temp_st = categories.get(y).getNom();
                    temp_int = categories.get(y).score(depeche_temp);

                    PaireChaineEntier temp_P =new PaireChaineEntier(temp_st,temp_int);
                    score_total.add(temp_P);
                    y++;
                }// la depenche qui est entrain d'etre tratee a une note pour chaque categorie

                //ecriture de la categorie detreminer pour a depeche en cour de traitement, dans le ficher donnee
                file.write(depeches.get(i).getId()+":"+UtilitairePaireChaineEntier.chaineMax(score_total)+"\n");

                lesCategories_des_depeches.add(new PaireChaineEntier(UtilitairePaireChaineEntier.chaineMax(score_total),i));
                // Arraylist uttile pour la suite de la fonction des test de bonne reponses
                i++;

            }// toutes les depeches ont été traitee et le resultat ecrit dans le fichier donee

            // pourcentage de bonne reponses pour la categorie de chaque dépeche
            i = 0;
            String cat_cour;
            float pourcentage;
            ArrayList<PaireChaineEntier> lesresultats = new ArrayList<>();
            int indice_depeche;
            UtilitairePaireChaineEntier.TrieBulleAmeliorer_String(lesCategories_des_depeches);//trie par categorie

            y=0;
            while ((i < categories.size())&& (y < lesCategories_des_depeches.size())){

                cat_cour = lesCategories_des_depeches.get(y).getChaine();// la  categorie qur l'ont cacule
                pourcentage = 0;

                while ((y < lesCategories_des_depeches.size())
                        &&(lesCategories_des_depeches.get(y).getChaine().compareTo(cat_cour) == 0)) {

                    indice_depeche = UtilitairePaireChaineEntier.entierPourIndice(lesCategories_des_depeches,y+1);

                    if (lesCategories_des_depeches.get(y).getChaine().compareTo(depeches.get(indice_depeche).getCategorie()) == 0) {
                        pourcentage += 1;
                    }// changement_scoree pourcentage si la categorie calculer est egal a la categorie de la depeche
                    y++;
                }// toutes les depeches associer a la categorie cat_cour ont été examiner


                lesresultats.add(new PaireChaineEntier(cat_cour,(int)pourcentage));
                i++;
            }// le score de toute les categorie a été calculer

            //incriction des resultat dans le fichier resultat
            for (i=0;i<lesresultats.size();i++){
                file.write(lesresultats.get(i).getChaine() + ":      " + lesresultats.get(i).getEntier() + "%\n");
            }
            file.write("Moyenne:    "+UtilitairePaireChaineEntier.moyenne(lesresultats));
            file.close();

        } catch (IOException e) {
            System.out.println("probleme lors de l'ecriture dans le fichier");
            throw new RuntimeException(e);
        }


    }


    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        //retourne une ArrayList<PaireChaineEntier> contenant tous les mots présents dans au moins
        //une dépêche de la catégorie categorie. Attention, même si le mot est présent plusieurs fois, il ne
        //doit apparaître qu’une fois dans la ArrayList retournée. Dans les entiers, nous stockerons les scores
        //associés à chaque mot et dans un premier temps, nous initialiserons ce score à 0
        int i=0;
        int y;
        int indice_mot;
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();

        while (i<depeches.size()){

            if(depeches.get(i).getCategorie().compareTo(categorie) == 0){

                y =0;

                while (y<depeches.get(i).getMots().size()){

                    indice_mot = UtilitairePaireChaineEntier.dichoIndice(resultat,depeches.get(i).getMots().get(y));

                    if(indice_mot < 0){
                        resultat.add(-indice_mot-1, new PaireChaineEntier(depeches.get(i).getMots().get(y), 0));
                    }

                    y++;
                }

            }// tout les mots de la depeche ont été analyser
            i++;
        }//toute les mots de toute les requettes qui appartienne a categorie (hors doublou)ont été mit dans resultat
        return resultat;
    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
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

                indiceMot = UtilitairePaireChaineEntier.dichoIndice(dictionnaire,depeches.get(i).getMots().get(j));

                if (indiceMot >= 0) {	// On regarde si le mot courant est présent dans le dictionnaire
					if (depeches.get(i).getCategorie().compareTo(categorie) == 0) {
						changement_score = 1;
					} else {
						changement_score = -1;
					}

					dictionnaire.set(indiceMot, new PaireChaineEntier(dictionnaire.get(indiceMot).getChaine(), dictionnaire.get(indiceMot).getEntier() + changement_score));
                    // ont met le score a jour
				}

                j++;
            }// tout les mots de la la depeche ont été examiner et leur score a été marquer dans dictionaire.getEntier

            i++;
        }// toutes les depeche ont été examiner

    }


    public static int poidsPourScore(int score) {
        // retourne une valeur de poids (1,2 ou 3) en fonction du score score
        // PROVISOIREMENT 
        return 0;
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {

    }

    public static void main(String[] args) {

        //Chargement des dépêches en mémoire
       // System.out.println("chargement des dépêches");
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");
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
        //initialisation a partir de leur fichier
        for(int i=0;i<lesCategories.size();i++){
           lesCategories.get(i).initLexique("lexique_"+lesCategories.get(i).getNom());
        }

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
        classementDepeches(depeches,lesCategories,"verrif.txt");
    }
}

