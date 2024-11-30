/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */

@XmlRootElement
@Entity
public class Link {
    @Id
    @SequenceGenerator(name="Link_gen", allocationSize=1) 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Link_gen")
    private long id;
    
    private String lastArticle;
    
    public Link(){
    }
    
    public void setLastArticle(int id){
        lastArticle = "/article/" + id;
    }
    
    public String getLastArticle(){
        return lastArticle;
    }
}
