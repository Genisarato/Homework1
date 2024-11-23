
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Genis i Jan
 */

@XmlRootElement
@Entity
public class Usuari {
    @Id
    @SequenceGenerator(name="Usuari_gen", allocationSize=1) 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Usuari_gen")
    private long id;
    
    private String nom;
    private String dni;
    private int telef;
    
    @OneToMany(mappedBy = "Autor")
    private List<Article> articles;
    
    private String lastArticle;
    
    public Usuari() {
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setTelef(int telef) {
        this.telef = telef;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDni() {
        return dni;
    }

    public int getTelef() {
        return telef;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setLinkArticle(String lastArticle) {
        this.lastArticle = lastArticle;
    }   
}
