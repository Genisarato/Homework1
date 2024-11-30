/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import authn.Secured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.entities.Article;
import model.entities.Usuari;


/**
 *
 * @author USUARIO
 */
@Path("/customer")
public class UsuariService extends AbstractFacade<Usuari>{
    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;
    private Map<Integer, Article> articles = new HashMap<>();
    
    @Context
    private HttpHeaders headers;
    
    public UsuariService() {
        super(Usuari.class);
    }
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllCustomers(){
        List<Usuari> usuaris = new ArrayList<>();
        Collection<Usuari> result = new ArrayList<>();
        String query = "SELECT u FROM Usuari u";
        usuaris = em.createQuery(query, Usuari.class).getResultList();
        for(Usuari u : usuaris){
            /*if (u.getArticles() != null && !u.getArticles().isEmpty()) {
                Article a = u.getArticles().get(u.getArticles().size() - 1);  // Sol accedim si la llista no está buida
                u.setLinkArticle("/article" + a.getId());
            }*/
            result.add(u);
        }
        return Response.status(Response.Status.OK)
               .entity(result)
               .build();
    }
    
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCustomerById(@PathParam ("id") long id){
       Usuari result = em.find(Usuari.class, id);
       if (result != null) return Response.status(Response.Status.OK).entity(result).build();
        else return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    @Secured
    @Transactional
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response modifyCustomerById(@PathParam ("id") long id, Usuari u2){
       Usuari u = em.find(Usuari.class, id);
       if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
       String queryText = "UPDATE Usuari SET nom = :nom, dni = :dni, telef = :telef, username = :username WHERE id = :id";
       Query queryMod = em.createQuery(queryText);
       queryMod.setParameter("nom", u2.getNom());
       queryMod.setParameter("dni", u2.getDni());
       queryMod.setParameter("telef", u2.getTelef());
       queryMod.setParameter("username", u2.getUsername());
       queryMod.setParameter("id", id);
       if (queryMod.executeUpdate() == 1) return Response.status(Response.Status.OK).build();
       else return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}

