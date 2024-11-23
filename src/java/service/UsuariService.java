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
public class UsuariService {
    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;
    private Map<Integer, Article> articles = new HashMap<>();
    
    @Context
    private HttpHeaders headers;
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Collection<Usuari> getAllCustomers(){
        Collection<Usuari> usuaris = new ArrayList<>();
        Collection<Usuari> result = new ArrayList<>();
        String query = "Select * from Usuari";
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
    @Produces({MediaType.APPLICATION_JSON})
    public Response getCustomerById(@PathParam ("id") int id){
       String queryText = "SELECT u FROM Usuari u WHERE u.id = :id";    //Contrasenya????
       TypedQuery<Usuari> query = em.createQuery(queryText, Usuari.class);
       query.setParameter("id", id);
       Usuari result = query.getSingleResult();
       if (result != null) {
           //result.setPassword(null);  //Si es guardava la contrasenya, aqui s'anula per a no ensenyarla (sha de crear setPassword)
           return Response.status(Response.Status.OK).entity(result).build();
       } else return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @PUT
    @Secured
    @Consumes({MediaType.APPLICATION_JSON})
    public Response modifyCustomerById(@PathParam ("id") int id, String nom, String dni, int telef){
       String queryText = "SELECT * FROM Usuari u WHERE u.id = :id";
       TypedQuery<Usuari> query = em.createQuery(queryText, Usuari.class);
       query.setParameter("id", id);
       Usuari u = query.getSingleResult();
       if (u == null) return Response.status(Response.Status.NOT_FOUND).build();
       queryText = "UPDATE Usuari SET nom = :nom, dni = :dni, telef = :telef WHERE id = :id";
       Query queryMod = em.createQuery(queryText);
       queryMod.setParameter("nom", nom);
       queryMod.setParameter("dni", dni);
       queryMod.setParameter("telef", telef);
       queryMod.setParameter("id", id);
       queryMod.executeUpdate();
       return Response.status(Response.Status.OK).build();
    }
}

