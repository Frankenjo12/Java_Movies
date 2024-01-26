/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Movie;
import hr.algebra.model.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author dnlbe
 */
public class SqlRepository implements Repository {
    
    private static final String SUCCESS = "Success"; 

    //user
    private static final String ID_USER = "IDUser";      
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String ROLE = "Role";

    private static final String CREATE_USER = "{ CALL createUser (?,?,?,?,?) }";
    private static final String UPDATE_USER = "{ CALL updateUser (?,?,?,?) }";
    private static final String DELETE_USER = "{ CALL deleteUser (?) }";
    private static final String SELECT_USER = "{ CALL selectUser (?) }";
    private static final String SELECT_USERS = "{ CALL selectUsers }";
    //movies
    private static final String ID_MOVIE = "IDMovie";
    private static final String TITLE = "Title";    
    private static final String DESCRIPTION = "Description";
    private static final String BANNER_PATH = "BannerPath";
    private static final String LINK = "Link";
    private static final String PUBLISH_DATE = "PublishDate";
    private static final String SHOWING_DATE = "ShowingDate";

    private static final String CREATE_MOVIE = "{ CALL createMovie (?,?,?,?,?,?,?,?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?,?,?,?,?,?,?,?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteMovie (?) }";
    private static final String SELECT_MOVIE = "{ CALL selectMovie (?) }";
    private static final String SELECT_MOVIES = "{ CALL selectMovies }";
    //actors
    private static final String ID_ACTOR = "IDActor";
    private static final String ACTOR_FIRST = "FirstName";    
    private static final String ACTOR_LAST = "LastName";

    private static final String CREATE_ACTOR = "{ CALL createActor (?,?,?) }";
    private static final String UPDATE_ACTOR = "{ CALL updateActor (?,?,?,?) }";
    private static final String DELETE_ACTOR = "{ CALL deleteActor (?) }";
    private static final String SELECT_ACTOR = "{ CALL selectActor (?) }";
    private static final String SELECT_ACTORS = "{ CALL selectActors }";
    //directors
    private static final String ID_DIRECTOR = "IDDirector";
    private static final String DIR_FIRST = "FirstName";    
    private static final String DIR_LAST = "LastName";

    private static final String CREATE_DIR = "{ CALL createDirector (?,?,?) }";
    private static final String UPDATE_DIR = "{ CALL updateDirector (?,?,?,?) }";
    private static final String DELETE_DIR = "{ CALL deleteDirector (?) }";
    private static final String SELECT_DIR = "{ CALL selectDirector (?) }";
    private static final String SELECT_DIRS = "{ CALL selectDirectors }";
    
    //relationships
    private static final String ID_REL_MOVIE = "MovieID";
    private static final String ID_REL_ACTOR = "ActorID";    
    private static final String ID_REL_DIR = "DirectorID";
    
    private static final String SELECT_ACTORS_MOVIE = "{ CALL selectActorsForMovie (?) }";
    private static final String ADD_ACTOR_MOVIE = "{ CALL addActorToMovie (?,?,?) }";
    private static final String REMOVE_ACTOR_MOVIE = "{ CALL removeActorFromMovie (?,?) }";

    private static final String SELECT_DIRECTORS_MOVIE = "{ CALL selectDirectorsForMovie (?) }";
    private static final String ADD_DIRECTORS_MOVIE = "{ CALL addDirectorToMovie (?,?,?) }";
    private static final String REMOVE_DIRECTOR_MOVIE = "{ CALL removeDirectorFromMovie (?,?) }";
    
    //database clear
    private static final String CLEAR_DATABASE = "{ CALL clearDatabase }";
    
    @Override
    public int createUser(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_USER)) {
            // careful - if we use integers as positions, it is 1 based! 
            //stmt.setString(1, student.getFirstName()); 
            // so, we better use parameter names!
            stmt.setString(USERNAME, user.getUsername());
            stmt.setString(PASSWORD, user.getPassword());
            stmt.setString(ROLE, user.getRole());
            stmt.registerOutParameter(ID_USER, Types.INTEGER);            
            stmt.registerOutParameter(SUCCESS, Types.BIT);


            stmt.executeUpdate();
            return stmt.getInt(ID_USER);
        }
    }

    @Override
    public void updateUser(int id, User data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_USER)) {
            stmt.setInt(ID_USER, id);
            stmt.setString(USERNAME, data.getUsername());
            stmt.setString(PASSWORD, data.getPassword());
            stmt.setString(ROLE, data.getRole());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_USER)) {
            stmt.setInt(ID_USER, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public User selectUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_USER)) {
            stmt.setInt(ID_USER, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt(ID_USER),
                            rs.getString(USERNAME),
                            rs.getString(PASSWORD),
                            rs.getString(ROLE));
                }
            }
        }
        return null;
    }

    @Override
    public List<User> selectUsers() throws Exception {
        List<User> users = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_USERS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt(ID_USER),
                        rs.getString(USERNAME),
                        rs.getString(PASSWORD),
                        rs.getString(ROLE)));
            }
        }
        return users;
    }

    @Override
    public int createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {
            // careful - if we use integers as positions, it is 1 based! 
            //stmt.setString(1, student.getFirstName()); 
            // so, we better use parameter names!
            stmt.setString(TITLE, movie.getTitle());
            stmt.setString(DESCRIPTION, movie.getDescripition());
            stmt.setString(BANNER_PATH, movie.getBannerPath());
            stmt.setString(LINK, movie.getLink());            
            stmt.setDate(PUBLISH_DATE, new java.sql.Date(movie.getPublishDate().getTime()));            
            stmt.setDate(SHOWING_DATE, new java.sql.Date(movie.getShowingDate().getTime()));            
            stmt.registerOutParameter(ID_MOVIE, Types.INTEGER);
            stmt.registerOutParameter(SUCCESS, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(ID_MOVIE);
        }
    }

    @Override
    public void updateMovie(int id, Movie data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {
            stmt.setInt(ID_MOVIE, id);
            stmt.setInt(SUCCESS, 1);
            stmt.setString(TITLE, data.getTitle());
            stmt.setString(DESCRIPTION, data.getDescripition());
            stmt.setString(BANNER_PATH, data.getBannerPath());
            stmt.setString(LINK, data.getLink());            
            stmt.setDate(PUBLISH_DATE, new java.sql.Date(data.getPublishDate().getTime()));            
            stmt.setDate(SHOWING_DATE, new java.sql.Date(data.getShowingDate().getTime())); 

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {
            stmt.setInt(ID_MOVIE, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Movie selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIE)) {
            stmt.setInt(ID_MOVIE, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Movie(
                            rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            rs.getString(DESCRIPTION),
                            rs.getString(BANNER_PATH),
                            rs.getString(LINK),
                            rs.getDate(PUBLISH_DATE),
                            rs.getDate(SHOWING_DATE));
                }
            }
        }
        return null;
    }

    @Override
    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            rs.getString(DESCRIPTION),
                            rs.getString(BANNER_PATH),
                            rs.getString(LINK),
                            rs.getDate(PUBLISH_DATE),
                            rs.getDate(SHOWING_DATE)));
            }
        }
        return movies;
    }

    @Override
    public int createActor(Actor actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_ACTOR)) {
            // careful - if we use integers as positions, it is 1 based! 
            //stmt.setString(1, student.getFirstName()); 
            // so, we better use parameter names!
            stmt.setString(ACTOR_FIRST, actor.getFirstName());
            stmt.setString(ACTOR_LAST, actor.getLastName());
            stmt.registerOutParameter(ID_ACTOR, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(ID_ACTOR);
        }
    }

    @Override
    public void updateActor(int id, Actor data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_ACTOR)) {
            stmt.setInt(ID_ACTOR, id);
            stmt.setInt(SUCCESS, 1);
            stmt.setString(ACTOR_FIRST, data.getFirstName());
            stmt.setString(ACTOR_LAST, data.getLastName()); 

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteActor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_ACTOR)) {
            stmt.setInt(ID_ACTOR, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Actor selectActor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTOR)) {
            stmt.setInt(ID_ACTOR, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Actor(
                            rs.getInt(ID_ACTOR),
                            rs.getString(ACTOR_FIRST),
                            rs.getString(ACTOR_LAST));
                }
            }
        }
        return null;
    }

    @Override
    public List<Actor> selectActors() throws Exception {
        List<Actor> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                actors.add(new Actor(
                        rs.getInt(ID_ACTOR),
                            rs.getString(ACTOR_FIRST),
                            rs.getString(ACTOR_LAST)));
            }
        }
        return actors;
    }

    @Override
    public int createDirector(Director director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_DIR)) {
            // careful - if we use integers as positions, it is 1 based! 
            //stmt.setString(1, student.getFirstName()); 
            // so, we better use parameter names!
            stmt.setString(ACTOR_FIRST, director.getFirstName());
            stmt.setString(ACTOR_LAST, director.getLastName());
            stmt.registerOutParameter(ID_DIRECTOR, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(ID_DIRECTOR);
        }
    }

    @Override
    public void updateDirector(int id, Director data) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(UPDATE_DIR)) {
            stmt.setInt(ID_DIRECTOR, id);
            stmt.setInt(SUCCESS, 1);
            stmt.setString(DIR_FIRST, data.getFirstName());
            stmt.setString(DIR_LAST, data.getLastName()); 

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteDirector(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(DELETE_DIR)) {
            stmt.setInt(ID_DIRECTOR, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Director selectDirector(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIR)) {
            stmt.setInt(ID_DIRECTOR, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Director(
                            rs.getInt(ID_DIRECTOR),
                            rs.getString(DIR_FIRST),
                            rs.getString(DIR_LAST));
                }
            }
        }
        return null;
    }

    @Override
    public List<Director> selectDirectors() throws Exception {
        List<Director> directors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRS);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                directors.add(new Director(
                        rs.getInt(ID_DIRECTOR),
                            rs.getString(DIR_FIRST),
                            rs.getString(DIR_LAST)));
            }
        }
        return directors;
    }

    @Override
    public List<Actor> selectActorsForMovie(int idMovie) throws Exception {
        List<Actor> actors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS_MOVIE)) {
            stmt.setInt(ID_REL_MOVIE, idMovie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(new Actor(
                        rs.getInt(ID_ACTOR),
                            rs.getString(ACTOR_FIRST),
                            rs.getString(ACTOR_LAST)));
                }
            }
        }
        return actors;
    }

    @Override
    public void addActorToMovie(int idActor, int idMovie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(ADD_ACTOR_MOVIE)) {
            stmt.setInt(ID_REL_ACTOR, idActor);
            stmt.setInt(ID_REL_MOVIE, idMovie);
            stmt.registerOutParameter(SUCCESS, Types.INTEGER);

            stmt.executeUpdate();
        }
    }

    @Override
    public void removeActorFromMovie(int idActor, int idMovie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(REMOVE_ACTOR_MOVIE)) {
            stmt.setInt(ID_REL_ACTOR, idActor);
            stmt.setInt(ID_REL_MOVIE, idMovie);

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Director> selectDirectorsForMovie(int idMovie) throws Exception {
        List<Director> directors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRECTORS_MOVIE)) {
            stmt.setInt(ID_REL_MOVIE, idMovie);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    directors.add(new Director(
                        rs.getInt(ID_DIRECTOR),
                            rs.getString(DIR_FIRST),
                            rs.getString(DIR_LAST)));
                }
            }
        }
        return directors;
    }

    @Override
    public void addDirectorToMovie(int idDirector, int idMovie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(ADD_DIRECTORS_MOVIE)) {
            stmt.setInt(ID_REL_DIR, idDirector);
            stmt.setInt(ID_REL_MOVIE, idMovie);
            stmt.setInt(SUCCESS, 1);

            stmt.executeUpdate();
        }
    }

    @Override
    public void removeDirectorFromMovie(int idDirector, int idMovie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(REMOVE_DIRECTOR_MOVIE)) {
            stmt.setInt(ID_REL_DIR, idDirector);
            stmt.setInt(ID_REL_MOVIE, idMovie);


            stmt.executeUpdate();
        }
    }

    @Override
    public void clearDatabase() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CLEAR_DATABASE)) {
            stmt.executeUpdate();
        }
    }

}
