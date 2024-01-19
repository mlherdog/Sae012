public class Paireresultatcompteur<Res> {
    private Res resultat;
    private int compteur;

    Paireresultatcompteur(Res resutat,int compteur){
        this.resultat = resutat;
        this.compteur = compteur;
    }

    public int getCompteur(){
        return compteur;
    }

    public Res getResultat() {
        return resultat;
    }
}
