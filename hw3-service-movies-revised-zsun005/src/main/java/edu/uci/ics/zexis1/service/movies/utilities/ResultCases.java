package edu.uci.ics.zexis1.service.movies.utilities;

public class ResultCases {
    public static String generalResultMessage(int resultCode){
        String to_return = "";
        switch (resultCode){
            case -1:
                return "Internal server error.";
            case -3:
                return "JSON parse exception.";
            case -2:
                return "JSON mapping exception.";
            case 210:
                return "Found movies with search parameters.";
            case 211:
                return "No movies found with search parameters.";
            case 141:
                return "User has insufficient privilege.";
            case 214:
                return "Movie successfully added.";
            case 215:
                return "Could not add movie.";
            case 216:
                return "Movie already exists.";
            case 241:
                return "Could not remove movie.";
            case 242:
                return "Movie has been already removed.";
            case 217:
                return "Genre successfully added.";
            case 218:
                return "Genre could not be added.";
            case 219:
                return "Genres successfully retrieved.";
            case 212:
                return "Found stars with search parameters.";
            case 213:
                return "No stars found with search parameters.";
            case 220:
                return "Star successfully added.";
            case 221:
                return "Could not add star.";
            case 222:
                return "Star already exists.";
            case 230:
                return "Star successfully added to movie.";
            case 231:
                return "Could not add star to movie.";
            case 232:
                return "Star already exists in movie.";
            case 240:
                return "Movie successfully removed.";
            case 250:
                return "Rating successfully updated.";
            case 251:
                return "Could not update rating.";



        }
        return to_return;
    }

}
