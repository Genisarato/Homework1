/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import authn.Credentials;
import authn.Secured;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import model.entities.Article;
import model.entities.Comment;
import model.entities.Topic;
import model.entities.Usuari;

/**
 *
 * @author USUARIO
 */

@Stateless
@Path("/article")
public class ArticleService extends AbstractFacade<Article>{
    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;
    private Map<Integer, Article> articles = new HashMap<>();
    
    @Context
    private HttpHeaders headers;
    
    public ArticleService() {
        super(Article.class);
    }
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    //Acabar de revisar si ha de retorna tot, 
    /*
    Yo suposo que només a de retorna titol, data, Nom_Escritor, Descripció i imatge
    Tot lo demés no fa falta, mirar-meu millor
    */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getByTopicAndUser(@QueryParam("author") long author, @QueryParam("topic") long... topics) {

        Usuari autorBD = null;
        List<Topic> resultatNoms = null;
        String query = "SELECT a FROM Article a WHERE 1=1";

        // Recuperamos el autor por su ID
        if (author > 0) {
            try {
                autorBD = em.find(Usuari.class, author);  // Comparar el objeto 'autorBD' en la consulta
                query += " AND a.autor=:author";
            
            } catch (NoResultException ex) {
                return Response.status(Response.Status.NOT_FOUND).entity("Usuai no registrat").build();
            }
        }

        // Validación de los tópicos
        if (topics != null && topics.length > 0) {
            try {
                List<Long> primers2 = Arrays.stream(topics)  // Convertimos a Stream<Long>
                                   .limit(2)       // Tomamos los primeros 2 elementos
                                   .boxed()        // Convertimos a Long (tipo objeto)
                                   .collect(Collectors.toList());
                // Obtener los tópicos por sus IDs
                String existQuery = "SELECT t FROM Topic t WHERE t.id IN :ids";
                resultatNoms = em.createQuery(existQuery, Topic.class)
                                 .setParameter("ids", primers2)  // Usamos los IDs directamente
                                 .getResultList();
                // Comprobamos que todos los tópicos existen
                if (resultatNoms.size() != primers2.size()) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Un o més tòpics no són vàlids").build();
                }
                query += " AND a.topic IN :topics";  // Compara los objetos 'topic' en la consulta
            } catch (NoResultException ex) {
                return Response.status(Response.Status.NOT_FOUND).entity("Topics no existents").build();
            }
        }

        query += " ORDER BY a.num_views DESC";

        // Crear la consulta final
        TypedQuery<Article> consulta = em.createQuery(query, Article.class);

        // Establecer parámetros
        if (author > 0) {
            consulta.setParameter("author", autorBD);  // Pasamos el objeto completo 'autorBD'
        }

        if (topics != null && topics.length > 0) {
            consulta.setParameter("topics", resultatNoms);  // Pasamos la lista completa de objetos 'Topic'
        }

        // Ejecutar la consulta y obtener los resultados
        List<Article> articlesList = consulta.getResultList();

        // Si no se encuentran artículos, devolver un error 404
        if (articlesList.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No se encontraron artículos con los parámetros proporcionados.")
                           .build();
        }

        // Si se encontraron artículos, devolver los artículos encontrados
        return Response.status(Response.Status.OK)
                       .entity(articlesList)
                       .build();
    }
   
    //Fer últim mètode de article
    /*//Acabar
    //Headers params
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getArticleById(@PathParam("id") int id){
        Article a = articles.get(id);
        if(a != null){
            if (a.isPrivat()){
                //Codi del RESTRequestFilter per mirar el header de la petició
                List<String> authHeaders = headers.getRequestHeader(HttpHeaders.AUTHORIZATION);
                       if (authHeaders == null || authHeaders.isEmpty()) {
                           return Response.status(Response.Status.UNAUTHORIZED).entity("Aquest article és privat").build();
                       }

                       String auth = authHeaders.get(0);
                       if (!auth.startsWith("Basic ")) {
                           return Response.status(Response.Status.BAD_REQUEST).entity("Tipus d'autenticació no suportat").build();
                       }

                       try {
                           String encodedCredentials = auth.replace("Basic ", "");
                           String decodedCredentials = new String(Base64.getDecoder().decode(encodedCredentials));
                           String[] credentials = decodedCredentials.split(":");
                           String username = credentials[0];
                           String password = credentials[1];

                           // Comprova les credencials a la BD
                           TypedQuery<Credentials> query = em.createNamedQuery("Credentials.findUser", Credentials.class);
                           Credentials user = query.setParameter("username", username).getSingleResult();

                           if (!user.getPassword().equals(password)) {
                               return Response.status(Response.Status.FORBIDDEN).entity("Credencials incorrectes").build();
                           }

                       } catch (Exception e) {
                           return Response.status(Response.Status.UNAUTHORIZED).entity("Autenticació fallida").build();
                       }
                       return Response.status(Response.Status.OK).entity(a).build();
            }
            else{
                a.sumarViews();
                return Response.ok().entity(a).build();
            }
            //mirar lo de privat i retorna o no i sumar views
        }
        else return Response.status(Response.Status.NOT_FOUND).build();
    }*/
    

    
    //FET i funcional
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Secured
    public Response crearArticle(Article e){
        //comprovar que asta registrat
        Usuari autor = e.getAutor();
        //Comprovem que usuari existeix a la BD, sino retornem error per simplificar el disseny, podriem crear-lo.
         //if(autor == null)return Response.status(Response.Status.NOT_FOUND).entity("Usuari no trobat").build();
         Usuari autorBD;
        try {
            // Recuperamos el usuario por su ID desde la base de datos
            String queryAutor = "SELECT u FROM Usuari u WHERE u.id = :id";
            autorBD = em.createQuery(queryAutor, Usuari.class)
                        .setParameter("id", autor.getId())
                        .getSingleResult();
        } catch (NoResultException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuari no trobat").build();
        }
        //Hem de comprovar que els 2 primers tòpics de l'article existeixein (si li passem més de 2 agafem els 2 primers)
        //Decisions de disseny
        //Obtenim els noms dels 2 primers tòpics per a buscar-los a la BD, com en el cas anterior retornarem error si no hi ha
        //els tòpics, però podriem crear-los.
        List<String> llistaTopics = e.getTopics().stream().map(Topic::getName).limit(2).collect(Collectors.toList());
        //Comprovem que hi ha almenys un tòpic per no haver errors en fer la query
        if(llistaTopics.isEmpty())return Response.status(Response.Status.BAD_REQUEST).entity("Cal proporcionar almenys un tòpic").build();
        
        String existQuery = "Select t FROM Topic t WHERE t.name IN :noms";
        List<Topic> resultatNoms = em.createQuery(existQuery, Topic.class).
                                    setParameter("noms", llistaTopics).
                                    getResultList();
        //Comprovem la existència dels tòpics ja que si la size no coincideix signficia que un dels dos no coincideix
        if(llistaTopics.size() != resultatNoms.size())return Response.status(Response.Status.BAD_REQUEST).entity("Un o més tòpics no són vàlids").build();
        autorBD.addArticle(e);
        autorBD.setLinkArticle(e.getTitol());
        
        e.setData_publi(new Date());
        e.setTopics(resultatNoms);
        e.setPrivat(false);
        em.persist(e);
        //Una vegada fetes totes les comprovacions es fa la inserció del article a la llista
        articles.put( e.getId(), e);
        return Response.status(Response.Status.CREATED).entity(e.getId()).build();
    }
    
}
