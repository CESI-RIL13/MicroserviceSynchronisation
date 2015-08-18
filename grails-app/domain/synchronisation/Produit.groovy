package synchronisation

class Produit {

    Long ref_produit
    Long quantite

    static belongsTo = [bonCommande:BonCommande]
    static constraints = {
    }
}
