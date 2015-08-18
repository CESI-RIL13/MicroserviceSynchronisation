package synchronisation

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ProduitController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Produit.list(params), model:[produitCount: Produit.count()]
    }

    def show(Produit produit) {
        respond produit
    }

    def create() {
        respond new Produit(params)
    }

    @Transactional
    def save(Produit produit) {
        if (produit == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (produit.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond produit.errors, view:'create'
            return
        }

        produit.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'produit.label', default: 'Produit'), produit.id])
                redirect produit
            }
            '*' { respond produit, [status: CREATED] }
        }
    }

    def edit(Produit produit) {
        respond produit
    }

    @Transactional
    def update(Produit produit) {
        if (produit == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (produit.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond produit.errors, view:'edit'
            return
        }

        produit.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'produit.label', default: 'Produit'), produit.id])
                redirect produit
            }
            '*'{ respond produit, [status: OK] }
        }
    }

    @Transactional
    def delete(Produit produit) {

        if (produit == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        produit.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'produit.label', default: 'Produit'), produit.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'produit.label', default: 'Produit'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
