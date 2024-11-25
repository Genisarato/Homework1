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
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Collection<Usuari> getAllCustomers(){
        Collection<Usuari> usuaris = new ArrayList<>();
        Collection<Usuari> result = new ArrayList<>();
        String query = "SELECT u FROM Usuari u";
        TypedQuery<Usuari> consulta = em.createQuery(query, Usuari.class);
        usuaris = consulta.getResultList();
        for(Usuari u : usuaris){
            if(u.getArticles() != null){
                Article a = u.getArticles().get(u.getArticles().size() - 1);
                u.setLinkArticle("/article" + a.getId());
            }
            result.add(u);
        }
        return result;
    }
    
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCustomerById(@PathParam ("id") int id){
       Usuari result = em.find(Usuari.class, id);
       if (result != null) return Response.status(Response.Status.OK).entity(result).build();
        else return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @PUT
    @Secured
    @Consumes({MediaType.APPLICATION_JSON})
    public Response modifyCustomerById(@PathParam ("id") int id, String nom, String dni, int telef, String username){
       Usuari u = em.find(Usuari.class, id);
       if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
       String queryText = "UPDATE Usuari SET nom = :nom, dni = :dni, telef = :telef, username = :username WHERE id = :id";
       Query queryMod = em.createQuery(queryText);
       queryMod.setParameter("nom", nom);
       queryMod.setParameter("dni", dni);
       queryMod.setParameter("telef", telef);
       queryMod.setParameter("username", username);
       queryMod.setParameter("id", id);
       if (queryMod.executeUpdate() == 1) return Response.status(Response.Status.OK).build();
       else return Response.status(Response.Status.BAD_REQUEST).build();
    }
}

