package synchronisation

import grails.test.mixin.*
import spock.lang.*

@TestFor(ProduitController)
@Mock(Produit)
class ProduitControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.produitList
            model.produitCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.produit!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def produit = new Produit()
            produit.validate()
            controller.save(produit)

        then:"The create view is rendered again with the correct model"
            model.produit!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            produit = new Produit(params)

            controller.save(produit)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/produit/show/1'
            controller.flash.message != null
            Produit.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def produit = new Produit(params)
            controller.show(produit)

        then:"A model is populated containing the domain instance"
            model.produit == produit
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def produit = new Produit(params)
            controller.edit(produit)

        then:"A model is populated containing the domain instance"
            model.produit == produit
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/produit/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def produit = new Produit()
            produit.validate()
            controller.update(produit)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.produit == produit

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            produit = new Produit(params).save(flush: true)
            controller.update(produit)

        then:"A redirect is issued to the show action"
            produit != null
            response.redirectedUrl == "/produit/show/$produit.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/produit/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def produit = new Produit(params).save(flush: true)

        then:"It exists"
            Produit.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(produit)

        then:"The instance is deleted"
            Produit.count() == 0
            response.redirectedUrl == '/produit/index'
            flash.message != null
    }
}
