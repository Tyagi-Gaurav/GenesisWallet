package com.gw.user.repo;

import com.gw.common.domain.User;
import com.gw.user.utils.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.sql.SQLException;

import static com.gw.user.repo.DBTestUtils.addToDatabase;
import static com.gw.user.repo.DBTestUtils.clearDatabase;
import static com.gw.user.utils.TestUserBuilder.aUser;
import static com.gw.user.utils.TestUserBuilder.copyOf;

@ActiveProfiles("UserRepositoryTest")
@ContextConfiguration(initializers = TestContainerDatabaseInitializer.class)
@SpringBootTest(classes = {UserRepository.class, UserRepositoryImpl.class})
@ExtendWith(MockitoExtension.class)
class UserRepositoryTest extends DatabaseTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() throws SQLException {
        clearDatabase(databaseClient);
    }

    @Test
    void shouldFindUserById() {
        //given
        User userInDatabase = aUser().build();
        addToDatabase(userInDatabase, databaseClient);

        User expectedUser = copyOf(userInDatabase).withPassword(null).build();

        //when
        Mono<User> actualUser = userRepository.findUserById(userInDatabase.id());

        //then
        StepVerifier.create(actualUser)
                .expectNext(expectedUser)
                .verifyComplete();
    }

//    @Test
//    void shouldReturnEmptyMovieWhenNoMovieFoundById() {
//        //given
//        Movie expectedMovie = aMovie().build();
//
//        //when
//        Mono<Movie> movie = movieRepository.findMovieBy(expectedMovie.id());
//
//        //then
//        StepVerifier.create(movie).verifyComplete();
//    }
//
//    @Test
//    void shouldHandleExceptionWhenFindMovieByIdFails() throws SQLException {
//        //given
//        Movie expectedMovie = aMovie().build();
//        User user = aUser().build();
//        addToDatabase(expectedMovie, dataSource, user.id(), ADD_USER);
//
//        //when
//        UUID movieId = expectedMovie.id();
//        DatabaseException exception = catchThrowableOfType(() -> buggyUserRepository.findMovieBy(movieId),
//                DatabaseException.class);
//
//        //then
//        assertThat(exception).isNotNull();
//    }
//
//    @Test
//    void shouldFindMovieByName() throws SQLException {
//        //given
//        Movie expectedMovie = aMovie().build();
//        User user = aUser().build();
//        addToDatabase(expectedMovie, dataSource, user.id(), ADD_USER);
//
//        //when
//        Mono<Movie> movie = movieRepository.findMovieBy(user.id(), expectedMovie.name());
//
//        //then
//        StepVerifier.create(movie).expectNext(expectedMovie).verifyComplete();
//    }
//
//    @Test
//    void shouldRetrieveRatingCorrectlyWithAppropriateScale() throws SQLException {
//        //given
//        Movie expectedMovie = aMovie().withRating(BigDecimal.valueOf(7.8)).build();
//
//        User user = aUser().build();
//        addToDatabase(expectedMovie, dataSource, user.id(), ADD_USER);
//
//        //when
//        Mono<Movie> movie = movieRepository.findMovieBy(user.id(), expectedMovie.name());
//
//        //then
//        StepVerifier.create(movie)
//                .expectNext(expectedMovie)
//                .verifyComplete();
//    }
//
//    @Test
//    void shouldNotFindMovieByNameForADifferentUser() throws SQLException {
//        //given
//        Movie expectedMovie = aMovie().build();
//        User user = aUser().build();
//        addToDatabase(expectedMovie, dataSource, user.id(), ADD_USER);
//
//        //when
//        Mono<Movie> movie = movieRepository.findMovieBy(UUID.randomUUID(), expectedMovie.name());
//
//        //then
//        StepVerifier.create(movie).verifyComplete();
//    }
//
//    @Test
//    void shouldHandleExceptionWhenFindMovieByNameFails() throws SQLException {
//        //given
//        Movie expectedMovie = aMovie().build();
//        User user = aUser().build();
//        UUID userId = user.id();
//        addToDatabase(expectedMovie, dataSource, userId, ADD_USER);
//
//        //when
//        String name = expectedMovie.name();
//        DatabaseException exception = catchThrowableOfType(() -> buggyUserRepository.findMovieBy(userId, name),
//                DatabaseException.class);
//
//        //then
//        assertThat(exception).isNotNull();
//    }
//
//    @Test
//    void shouldReturnEmptyMovieWhenNoMovieFoundByName() {
//        //given
//        Movie expectedMovie = aMovie().build();
//        User user = aUser().build();
//
//        //when
//        Mono<Movie> movie = movieRepository.findMovieBy(user.id(), expectedMovie.name());
//
//        //then
//        StepVerifier.create(movie)
//                .verifyComplete();
//    }
//
//    @Test
//    void shouldGetAllMoviesForAUser() throws SQLException {
//        //given
//        User user = aUser().build();
//        Movie expectedMovieA = aMovie().build();
//        Movie expectedMovieB = aMovie().build();
//        addToDatabase(expectedMovieA, dataSource, user.id(), ADD_USER);
//        addToDatabase(expectedMovieB, dataSource, user.id(), ADD_USER);
//
//        //when
//        Flux<Movie> allMovies = movieRepository.getAllMoviesForUser(user.id());
//
//        //then
//        StepVerifier.create(allMovies)
//                .expectNext(expectedMovieA)
//                .expectNext(expectedMovieB)
//                .verifyComplete();
//    }
//
//    @Test
//    void shouldHandleExceptionWhenGetAllMoviesFails() throws SQLException {
//        //given
//        User user = aUser().build();
//        Movie expectedMovieA = aMovie().build();
//        UUID userId = user.id();
//        addToDatabase(expectedMovieA, dataSource, userId, ADD_USER);
//        Movie expectedMovieB = aMovie().build();
//        addToDatabase(expectedMovieB, dataSource, userId, ADD_USER);
//
//        //when & then
//        assertThatThrownBy(() -> buggyUserRepository.getAllMoviesForUser(userId))
//                .isInstanceOf(DatabaseException.class);
//    }
//
//    @Test
//    void shouldReturnEmptyListWhenNoMoviesFoundForUser() {
//        //given
//        User user = aUser().build();
//
//        //when
//        Flux<Movie> allMovies = movieRepository.getAllMoviesForUser(user.id());
//
//        //then
//        StepVerifier.create(allMovies).verifyComplete();
//    }
//
//    @Test
//    void shouldDeleteMovieForUser() throws SQLException {
//        //given
//        User user = aUser().build();
//        Movie expectedMovieA = aMovie().build();
//        addToDatabase(expectedMovieA, dataSource, user.id(), ADD_USER);
//
//        //when
//        movieRepository.delete(expectedMovieA.id());
//
//        //then
//        assertThat(getMovie(expectedMovieA.id(), dataSource, SELECT_MOVIE_BY_ID)).isEmpty();
//    }
//
//    @Test
//    void shouldHandleExceptionWhenDeleteMovieFails() throws SQLException {
//        //given
//        User user = aUser().build();
//        Movie expectedMovieA = aMovie().build();
//        UUID movieId = expectedMovieA.id();
//        addToDatabase(expectedMovieA, dataSource, user.id(), ADD_USER);
//
//        //when
//        DatabaseException databaseException =
//                catchThrowableOfType(() -> buggyUserRepository.delete(movieId), DatabaseException.class);
//
//        //then
//        assertThat(databaseException).isNotNull();
//    }
//
//    @Test
//    void shouldUpdateMovie() throws SQLException {
//        //given
//        User user = aUser().build();
//        Movie expectedMovieA = aMovie().build();
//        addToDatabase(expectedMovieA, dataSource, user.id(), ADD_USER);
//
//        //when
//        Movie updatedMovie = copyOf(expectedMovieA).withName("test")
//                .withGenre(Genre.SUSPENSE)
//                .withAgeRating(AgeRating.EIGHTEEN)
//                .withContentType(ContentType.TV_SERIES)
//                .withIsShareable(false)
//                .build();
//        Mono<Void> update = movieRepository.update(updatedMovie);
//
//        //then
//        StepVerifier.create(update).verifyComplete();
//        Optional<Movie> movie = getMovie(updatedMovie.id(), dataSource, SELECT_MOVIE_BY_ID);
//        assertThat(movie).isNotEmpty();
//        assertThat(movie.get().name()).isEqualTo("test");
//        assertThat(movie.get().genre()).isEqualTo(updatedMovie.genre());
//        assertThat(movie.get().ageRating()).isEqualTo(updatedMovie.ageRating());
//        assertThat(movie.get().isShareable()).isEqualTo(updatedMovie.isShareable());
//        assertThat(movie.get().contentType()).isEqualTo(updatedMovie.contentType());
//    }
//
//    @Test
//    void shouldHandleExceptionWhenUpdateMovieFails() throws SQLException {
//        //given
//        User user = aUser().build();
//        Movie expectedMovieA = aMovie().build();
//        addToDatabase(expectedMovieA, dataSource, user.id(), ADD_USER);
//
//        //when
//        Movie updatedMovie = copyOf(expectedMovieA).withName("test").build();
//        DatabaseException databaseException =
//                catchThrowableOfType(() -> buggyUserRepository.update(updatedMovie), DatabaseException.class);
//
//        //then
//        assertThat(databaseException).isNotNull();
//    }
//
//    @Test
//    void shouldCreateMovie() throws SQLException {
//        //given
//        User currentUser = aUser().build();
//        Movie expectedMovie = aMovie().build();
//
//        //when
//        movieRepository.create(currentUser.id(), expectedMovie);
//
//        //then
//        Optional<Movie> movie = getMovie(expectedMovie.id(), dataSource, SELECT_MOVIE_BY_ID);
//        assertThat(movie).isNotEmpty().hasValue(expectedMovie);
//    }
//
//    @Test
//    void shouldHandleExceptionWhenCreateUserFails() {
//        //given
//        Movie expectedMovie = aMovie().build();
//
//        //when
//        DatabaseException databaseException =
//                catchThrowableOfType(() -> buggyUserRepository.update(expectedMovie), DatabaseException.class);
//
//        //then
//        assertThat(databaseException).isNotNull();
//    }


}