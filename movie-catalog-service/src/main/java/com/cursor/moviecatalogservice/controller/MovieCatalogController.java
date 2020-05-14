package com.cursor.moviecatalogservice.controller;

import com.cursor.moviecatalogservice.domain.CatalogItem;
import com.cursor.moviecatalogservice.domain.Movie;
import com.cursor.moviecatalogservice.domain.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public MovieCatalogController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable String userId) {

        UserRating ratings = webClientBuilder.build()
                .get()
                .uri("http://ratings-data-service/ratings/user/" + userId)
                .retrieve()
                .bodyToMono(UserRating.class)
                .block();

        assert ratings != null;
        return ratings.getUserRating().stream()
                .map(rating -> {
                    Movie movie = webClientBuilder.build()
                            .get()
                            .uri("http://movie-info-service/movies/" + rating.getMovieId())
                            .retrieve()
                            .bodyToMono(Movie.class)
                            .block();

                    assert movie != null;
                    return new CatalogItem(movie.getTitle(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());
    }
}