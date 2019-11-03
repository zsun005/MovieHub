DELIMITER $$
CREATE PROCEDURE search_movie(movieid VARCHAR(10))
BEGIN
SELECT m.id, m.title, m.director, m.year, m.backdrop_path, m.overview, m.poster_path, m.revenue, m.hidden ,r.rating, r.numVotes,
       GROUP_CONCAT(DISTINCT gm.genre SEPARATOR ',') AS genres,
       GROUP_CONCAT(DISTINCT sm.star SEPARATOR  ',') AS stars

FROM
    movies m
        LEFT OUTER JOIN ratings r on m.id = r.movieId
        LEFT OUTER JOIN (
            SELECT gm1.movieId as movieId,
                CONCAT('{id: ', g1.id, ', name: "', g1.name, '"}') as genre
            FROM genres_in_movies gm1, genres g1 WHERE gm1.genreId = g1.id) gm on m.id = gm.movieId
        LEFT OUTER JOIN (
            SELECT sm1.movieId as movieId,
                   CONCAT('{id: "', s1.id, '", name: "', s1.name, '"}') as star
            FROM stars_in_movies sm1, stars s1 WHERE sm1.starId = s1.id) sm ON m.id = sm.movieId
        , genres_in_movies gmFilter
        , genres gFilter
WHERE
      m.id = gmFilter.movieId AND
      gmFilter.genreId = gFilter.id AND
      m.id = movieid;
#       m.title LIKE '%D%' AND
#       m.director LIKE '%R%' AND
#       gFilter.name LIKE '%Action%'
# GROUP BY m.id, r.rating
# ORDER BY r.rating DESC
# LIMIT 10
#     OFFSET 0;
END $$
DELIMITER ;