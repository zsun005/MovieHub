package edu.uci.ics.zexis1.service.movies.models;

public class RatingModel {
    String movieId;
    Float rating;
    Integer numVotes;

    public RatingModel(String movieId, Float rating, Integer numVotes) {
        this.movieId = movieId;
        this.rating = rating;
        this.numVotes = numVotes;
    }

    public String getMovieId() {
        return movieId;
    }

    public Float getRating() {
        return rating;
    }

    public Integer getNumVotes() {
        return numVotes;
    }
}
