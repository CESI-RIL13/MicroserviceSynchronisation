package synchronisation

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BonCommandeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond BonCommande.list(params), model:[bonCommandeCount: BonCommande.count()]
    }

    def show(BonCommande bonCommande) {
        respond bonCommande
    }

    def create() {
        respond new BonCommande(params)
    }

    @Transactional
    def save(BonCommande bonCommande) {
        if (bonCommande == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (bonCommande.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond bonCommande.errors, view:'create'
            return
        }

        bonCommande.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'bonCommande.label', default: 'BonCommande'), bonCommande.id])
                redirect bonCommande
            }
            '*' { respond bonCommande, [status: CREATED] }
        }
    }

    def edit(BonCommande bonCommande) {
        respond bonCommande
    }

    @Transactional
    def update(BonCommande bonCommande) {
        if (bonCommande == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (bonCommande.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond bonCommande.errors, view:'edit'
            return
        }

        bonCommande.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'bonCommande.label', default: 'BonCommande'), bonCommande.id])
                redirect bonCommande
            }
            '*'{ respond bonCommande, [status: OK] }
        }
    }

    @Transactional
    def delete(BonCommande bonCommande) {

        if (bonCommande == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        bonCommande.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'bonCommande.label', default: 'BonCommande'), bonCommande.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'bonCommande.label', default: 'BonCommande'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
