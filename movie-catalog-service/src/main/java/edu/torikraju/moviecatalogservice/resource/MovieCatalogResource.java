package edu.torikraju.moviecatalogservice.resource;

import edu.torikraju.moviecatalogservice.model.CatalogItem;
import edu.torikraju.moviecatalogservice.model.Movie;
import edu.torikraju.moviecatalogservice.model.Rating;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @RequestMapping(value = "/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {

        RestTemplate restTemplate = new RestTemplate();

        List<Rating> ratings = Arrays.asList(
                new Rating("1234", 3),
                new Rating("5678", 4)
        );

        return ratings.stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://127.0.0.1:8082/movies/" + rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "Test", rating.getRating());
        }).collect(Collectors.toList());
    }

}