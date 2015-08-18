class UrlMappings {

    static mappings = {

        "/bon-commandes"(resources:"bonCommande") {
            "/produits"(resources:"produit")
        }
        "/produits"(resources:"produit") {
            "/bon-commande"(resource:"bonCommande")
        }
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
