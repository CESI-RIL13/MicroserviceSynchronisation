package synchronisation

class BonCommande {

    Date date
    String emetteur
    String adresse_emmeteur

    static hasMany = [produits:Produit]
    static constraints = {
    }
}
