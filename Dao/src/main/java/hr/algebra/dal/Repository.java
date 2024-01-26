/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Movie;
import hr.algebra.model.User;
import java.util.List;

/**
 *
 * @author hyperv
 */
public interface Repository {
    
    int createUser(User user) throws Exception;
    void updateUser(int id, User data) throws Exception;
    void deleteUser(int id) throws Exception;
    User selectUser(int id) throws Exception;
    List<User> selectUsers() throws Exception;
    
    int createMovie(Movie movie) throws Exception;
    void updateMovie(int id, Movie data) throws Exception;
    void deleteMovie(int id) throws Exception;
    Movie selectMovie(int id) throws Exception;
    List<Movie> selectMovies() throws Exception;
    
    int createActor(Actor actor) throws Exception;
    void updateActor(int id, Actor data) throws Exception;
    void deleteActor(int id) throws Exception;
    Actor selectActor(int id) throws Exception;
    List<Actor> selectActors() throws Exception;
    
    int createDirector(Director director) throws Exception;
    void updateDirector(int id, Director data) throws Exception;
    void deleteDirector(int id) throws Exception;
    Director selectDirector(int id) throws Exception;
    List<Director> selectDirectors() throws Exception;
    
    List<Actor> selectActorsForMovie(int idMovie) throws Exception;
    void addActorToMovie(int idActor, int idMovie) throws Exception;
    void removeActorFromMovie(int idActor, int idMovie) throws Exception;
    
    List<Director> selectDirectorsForMovie(int idMovie) throws Exception;
    void addDirectorToMovie(int idDirector, int idMovie) throws Exception;
    void removeDirectorFromMovie(int idDirector, int idMovie) throws Exception;
    
    void clearDatabase() throws Exception; 
    
}
