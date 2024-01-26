/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.movieapp.parsers.rss;

import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Movie;
import hr.algebra.utilities.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 *
 * @author Franko
 */
public class MovieParser {
    
    private static final String RSS_URL = "https://www.blitz-cinestar-bh.ba/rss.aspx?id=2682";
    //private static final String ATTRIBUTE_URL = "url";
    private static final String EXT = ".jpg";
    private static final String DIR = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "assets";
    
    private static final String DATETIME_PATTERN = "dd.MM.yyyy";
    
    private static final String DEL = ",";

    
    public static List<Movie> parse() throws IOException, XMLStreamException, ParseException {
        List<Movie> movies = new ArrayList<>();
        Set<String> pictureUrls = new HashSet<>();
        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL);
        try (InputStream is = con.getInputStream()) { // stream will close the connection
            XMLEventReader reader = ParserFactory.createStaxParser(is);

            Optional<TagType> tagType = Optional.empty();
            Movie movie = null;
            StartElement startElement;
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        startElement = event.asStartElement();
                        String qName = startElement.getName().getLocalPart();
                        tagType = TagType.from(qName);
                        // put breakpoint here
                        if (tagType.isPresent() && tagType.get().equals(TagType.ITEM)) {
                            movie = new Movie();
                            movies.add(movie);
                        }
                    }
                    case XMLStreamConstants.CHARACTERS -> {
                        if (tagType.isPresent() && movie != null) {
                            Characters characters = event.asCharacters();
                            String data = characters.getData().trim();
                            switch (tagType.get()) {
                                case TITLE -> {
                                    if (!data.isEmpty()) {
                                        movie.setTitle(data);
                                    }
                                }
                                case PUB_DATE -> {
                                    if (!data.isEmpty()) {
                                        LocalDateTime publishedDate 
                                                = LocalDateTime.parse(data, DateTimeFormatter.RFC_1123_DATE_TIME);
                                        movie.setPublishDate(
                                                Date.from(publishedDate.atZone(ZoneId.systemDefault()).toInstant()));
                                    }
                                }
                                case DESCRIPTION -> {
                                    if (!data.isEmpty()) {
                                        
                                        int indexStart = data.indexOf("\"left\">");
                                        int indexEnd = data.indexOf("<br />");
                                        int mod = 7;
                                        
                                        String finalData = data.substring(indexStart + mod, indexEnd);
                                        
                                        
                                        movie.setDescripition(finalData);
                                    }
                                }
                                case BANNER_PATH -> {
                                    
                                    if(!data.isEmpty() && !pictureUrls.contains(data))
                                    {
                                        pictureUrls.add(data);
                                        handlePicture(movie, data);
                                    }

                                    /*if (startElement != null && movie.getBannerPath() == null) {
                                        Attribute urlAttribute = startElement.getAttributeByName(new QName(ATTRIBUTE_URL));
                                        if (urlAttribute != null) {
                                            handlePicture(movie, urlAttribute.getValue());
                                            System.out.println("Banner: " + movie.getBannerPath() + "\n");
                                        }
                                    }*/
                                }
                                case LINK -> {
                                    if (!data.isEmpty()) {
                                        movie.setLink(data);
                                    }
                                }
                                case SHOW_DATE -> {
                                    if (!data.isEmpty()) {
                                        LocalDate showDate 
                                                = LocalDate.parse(
                                                        data, DateTimeFormatter.ofPattern(DATETIME_PATTERN));
                                        movie.setShowingDate(
                                                Date.from(showDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                                    }
                                }
                                case ACTORS -> {
                                    if (!data.isEmpty()) {
                                        String actors[] = data.split(DEL);
                                        Set<Actor> actorsSet = new HashSet<>();
                                        for (String a : actors) {
                                            Actor actor;
                                            if(a.trim().contains(" "))
                                            {
                                                String actorInfo[] = a.trim().split(" ", 2);
                                                actor = new Actor(actorInfo[0], actorInfo[1]);
                                            }
                                            else
                                            {
                                                actor = new Actor(a.trim());
                                            }
                                            actorsSet.add(actor);
                                        }
                                        
                                        movie.setActors(actorsSet);
                                    }
                                }
                                case DIRECTORS -> {
                                    if (!data.isEmpty()) {
                                        String directors[] = data.split(DEL);
                                        Set<Director> directorsSet = new HashSet<>();
                                        for (String d : directors) {
                                            Director director;
                                            if(d.trim().contains(" "))
                                            {
                                                String directorInfo[] = d.trim().split(" ", 2);
                                                director = new Director(directorInfo[0], directorInfo[1]);
                                            }
                                            else
                                            {
                                                director = new Director(d.trim());
                                            }
                                            directorsSet.add(director);
                                        }
                                        
                                        movie.setDirectors(directorsSet);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        
        List<Movie> newMovies = new ArrayList<>();
        
        movies.forEach((m) -> 
        {
            if(m.getBannerPath() != null)
            {
                newMovies.add(m);
            }
        });
        
        return newMovies;

    }
    
    private static void handlePicture(Movie movie, String pictureUrl) {
        // if picture is not ok, we must continue!!!
        try {
            String ext = pictureUrl.substring(pictureUrl.lastIndexOf("."));
            if (ext.length() > 4) {
                ext = EXT;
            }
            String pictureName = UUID.randomUUID() + ext;
            String localPicturePath = DIR + File.separator + pictureName;

            FileUtils.copyFromUrl(pictureUrl, localPicturePath);
            // put breakpoint
            movie.setBannerPath(localPicturePath);
        } catch (IOException ex) {
            Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private enum TagType {

        ITEM("item"),
        TITLE("title"),
        PUB_DATE("pubDate"),
        DESCRIPTION("description"),
        BANNER_PATH("plakat"),
        LINK("link"),
        SHOW_DATE("datumprikazivanja"),
        ACTORS("glumci"),
        DIRECTORS("redatelj");

        private final String name;

        private TagType(String name) {
            this.name = name;
        }

        private static Optional<TagType> from(String name) {
            for (TagType value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }
    
}
