/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Franko
 */
public class Movie {

    public Movie(int id, String title, String descripition, String bannerPath, String link, Date publishDate, Date showingDate) {
        this.id = id;
        this.title = title;
        this.descripition = descripition;
        this.bannerPath = bannerPath;
        this.link = link;
        this.publishDate = publishDate;
        this.showingDate = showingDate;
    }
    
    public Movie(String title, String descripition, String link, Date publishDate, Date showingDate) {
        this.title = title;
        this.descripition = descripition;
        this.link = link;
        this.publishDate = publishDate;
        this.showingDate = showingDate;
    }
    
    public Movie() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescripition() {
        return descripition;
    }

    public void setDescripition(String descripition) {
        this.descripition = descripition;
    }

    public String getBannerPath() {
        return bannerPath;
    }

    public void setBannerPath(String bannerPath) {
        this.bannerPath = bannerPath;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Date getShowingDate() {
        return showingDate;
    }

    public void setShowingDate(Date showingDate) {
        this.showingDate = showingDate;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Set<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(Set<Director> directors) {
        this.directors = directors;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.title);
        hash = 79 * hash + Objects.hashCode(this.descripition);
        hash = 79 * hash + Objects.hashCode(this.bannerPath);
        hash = 79 * hash + Objects.hashCode(this.link);
        hash = 79 * hash + Objects.hashCode(this.publishDate);
        hash = 79 * hash + Objects.hashCode(this.showingDate);
        hash = 79 * hash + Objects.hashCode(this.actors);
        hash = 79 * hash + Objects.hashCode(this.directors);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movie other = (Movie) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.descripition, other.descripition)) {
            return false;
        }
        if (!Objects.equals(this.bannerPath, other.bannerPath)) {
            return false;
        }
        if (!Objects.equals(this.link, other.link)) {
            return false;
        }
        if (!Objects.equals(this.publishDate, other.publishDate)) {
            return false;
        }
        if (!Objects.equals(this.showingDate, other.showingDate)) {
            return false;
        }
        if (!Objects.equals(this.actors, other.actors)) {
            return false;
        }
        return Objects.equals(this.directors, other.directors);
    }
    
    private int id;
    private String title;
    private String descripition;
    private String bannerPath;
    private String link;
    private Date publishDate;
    private Date showingDate;   
    private Set<Actor> actors;
    private Set<Director> directors;
    
}
