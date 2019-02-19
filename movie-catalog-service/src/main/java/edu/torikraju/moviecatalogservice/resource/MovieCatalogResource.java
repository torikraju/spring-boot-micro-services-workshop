package edu.torikraju.moviecatalogservice.resource;

import edu.torikraju.moviecatalogservice.model.CatalogItem;
import edu.torikraju.moviecatalogservice.model.Movie;
import edu.torikraju.moviecatalogservice.model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final RestTemplate restTemplate;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public MovieCatalogResource(WebClient.Builder webClientBuilder, RestTemplate restTemplate) {
        this.webClientBuilder = webClientBuilder;
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {
        UserRating ratings = restTemplate.getForObject("http://127.0.0.1:8083/ratingsData/users/" + userId, UserRating.class);
        return ratings.getRatings().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://127.0.0.1:8082/movies/" + rating.getMovieId(), Movie.class);
            return new CatalogItem(movie.getName(), "Test", rating.getRating());
        }).collect(Collectors.toList());
    }
}

//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://127.0.0.1:8082/movies/" + rating.getMovieId())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();
