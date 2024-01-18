import java.util.ArrayList;

public class UtilitairePaireChaineEntier {


    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        // retourne l'indice de chaine dans listePair si chaine est présente et -1 sinon
        int i=0;
        while((i< listePaires.size()) && (listePaires.get(i).getChaine().compareTo(chaine) != 0)){
            i++;
        }
        if( i == listePaires.size()){
            return -1;
        }else {
            return i;
        }
    }

    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        // retourne l'entier associé a la chaine de caractères chaine dans listePaires si elle est présente et 0 sinon.
        int i=0;

        while( (i < listePaires.size()) && (listePaires.get(i).getChaine().compareTo(chaine) != 0)){
            i++;
        }// l'invariant est i < la taille du vecteur, et valeur observer est diffrerente de la valeur chercher
        if( i == listePaires.size()){
            return 0;
        }else {
            return listePaires.get(i).getEntier();
        }
    }

    public static int entierPourIndice(ArrayList<PaireChaineEntier> listePaires,int entier){
        // retourne l'indice associer a l'entier
        int i=0;

        while( (i < listePaires.size()) && (listePaires.get(i).getEntier() != entier)){
            i++;
        }// l'invariant est i < la taille du vecteur, et valeur observer est diffrerente de la valeur chercher

        if( i == listePaires.size()){
            return 0;
        }else {
            return i;
        }

    }

    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        // retourne la chaine associé au plus grand entier de listePaires

        int i= 1;
        int max= listePaires.get(0).getEntier();
        int indice_max = 0;

        while (i<listePaires.size()){
            if(max < listePaires.get(i).getEntier()){
                max = listePaires.get(i).getEntier();
                indice_max = i;
            }
            i++;
        }// invariant i est plus petit que la taille de listPaires
        return listePaires.get(indice_max).getChaine();
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        // retourne  la moyenne des entiers stockés dans listePaires.
        int i=0;
        int compteur=0;

        while (i<listePaires.size()){
            compteur += listePaires.get(i).getEntier();
            i++;
        }
        return (compteur/i);
    }
    public static void TrieBulleAmeliorer_String(ArrayList<PaireChaineEntier> listePaires){
        int i = 0;
        int j;
        PaireChaineEntier temp;
        boolean onAPermute = true;

        while (onAPermute) {
            onAPermute = false;
            j = listePaires.size()-1;

            while (j > i) {
                if (listePaires.get(j).getChaine().compareTo(listePaires.get(j-1).getChaine()) < 0) {
                    temp = listePaires.get(j);
                    listePaires.set(j, listePaires.get(j-1));
                    listePaires.set(j-1, temp);
                    onAPermute = true;
                }
                j--;
            }

            i++;
        }
    }

    public static int dichoIndice(ArrayList<PaireChaineEntier> vPaires,String unString) {

        if ((vPaires.size() <= 0)||(unString.compareTo(vPaires.get(vPaires.size() - 1).getChaine()) > 0)) { // v.[v.size()-1] < val
            return -vPaires.size()-1;
        } else { // v.[v.size()-1] ≥ val
            int inf = 0;
            int sup = vPaires.size() - 1; // invariant vérifié
            int m;

            while (inf < sup) {

                m = (inf + sup) / 2;

                if (unString.compareTo(vPaires.get(m).getChaine()) <= 0){ // v[m] ≥ val

                    sup = m; // continuer de chercher à gauche sur [inf..m-1]

                } else { // v[m] < val

                    inf = m + 1; // continuer de chercher à droite sur [m+1..sup-1]

                }
            }// inf = sup
            if (vPaires.get(sup).getChaine().compareTo(unString) == 0) {
                return inf; // val trouvée
            } else {
                return -inf-1; // val pas trouvée, val aurait été à l'indice sup
            }
        }
    }
}
