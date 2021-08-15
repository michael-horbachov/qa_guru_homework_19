package guru.qa.libraryservice;

import guru.qa.libraryservice.domain.Author;
import guru.qa.libraryservice.domain.AuthorInfo;
import guru.qa.libraryservice.domain.Book;
import guru.qa.libraryservice.domain.BookInfo;
import guru.qa.libraryservice.specs.LibrarySpecs;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class LibraryControllerTests {

    private static final int BOOKS_QUANTITY = 4;
    private static final String AUTHOR_FIRST_NAME = "Jack";
    private static final String AUTHOR_LAST_NAME = "London";
    private static final String BOOK_TITLE_1 = "White Fang";
    private static final String BOOK_TITLE_2 = "The Call of the Wild";

    @Test
    void verifyAuthorAdding() {
        var author = given()
                .spec(LibrarySpecs.postAuthorRequest)
                .body(Author.builder().authorFirstName(AUTHOR_FIRST_NAME).authorLastName(AUTHOR_LAST_NAME).build())
                .when()
                .post("/")
                .then()
                .spec(LibrarySpecs.postAuthorResponse)
                .extract().as(AuthorInfo.class);

        assertThat(author.getAuthorFirstName()).isEqualTo(AUTHOR_FIRST_NAME);
        assertThat(author.getAuthorLastName()).isEqualTo(AUTHOR_LAST_NAME);
    }

    @Test
    void verifyAuthorAddingBadRequest() {
        given()
                .spec(LibrarySpecs.postAuthorRequest)
                .body(Author.builder().authorFirstName(AUTHOR_FIRST_NAME).authorLastName("").build())
                .when()
                .post("/")
                .then()
                .spec(LibrarySpecs.postAuthorBadRequest)
                .body("error", is("Bad Request"));
    }

    @Test
    void verifyBookAdding() {
        var book = given()
                .spec(LibrarySpecs.postBookRequest)
                .body(Book.builder().bookTitle(BOOK_TITLE_1)
                          .author(Author.builder().authorFirstName(AUTHOR_FIRST_NAME)
                                        .authorLastName(AUTHOR_LAST_NAME).build()).build())
                .when()
                .post("/")
                .then()
                .spec(LibrarySpecs.postBookResponse)
                .extract().as(BookInfo.class);

        assertThat(book.getBookTitle()).isEqualTo(BOOK_TITLE_1);
        assertThat(book.getAuthor().getAuthorFirstName()).isEqualTo(AUTHOR_FIRST_NAME);
        assertThat(book.getAuthor().getAuthorLastName()).isEqualTo(AUTHOR_LAST_NAME);
    }

    @Test
    void verifyBooksQuantity() {
        var books = given()
                .spec(LibrarySpecs.getBooksRequest)
                .when()
                .get("/getAll")
                .then()
                .spec(LibrarySpecs.getBooksResponse)
                .extract().response().as(BookInfo[].class);

        assertThat(books.length).isEqualTo(BOOKS_QUANTITY);
    }

    @Test
    void verifyBookInLibrary() {
        given()
                .spec(LibrarySpecs.getBooksRequest)
                .when()
                .get("/getAll")
                .then()
                .spec(LibrarySpecs.getBooksResponse)
                .body("findAll{it.book_title}.book_title.flatten()",
                      hasItem(BOOK_TITLE_1));
    }

    @Test
    void verifyGettingBooksByAuthor() {
        given()
                .spec(LibrarySpecs.getBooksRequest)
                .param("authorFirstName", AUTHOR_FIRST_NAME)
                .param("authorLastName", AUTHOR_LAST_NAME)
                .when()
                .get("/getByAuthor")
                .then()
                .spec(LibrarySpecs.getBooksResponse)
                .body("findAll{it.book_title}.book_title.flatten()",
                      hasItems(BOOK_TITLE_1, BOOK_TITLE_2));
    }

    @Test
    void verifyNotFoundBooksByAuthor() {
        given()
                .spec(LibrarySpecs.getBooksRequest)
                .param("authorFirstName", "")
                .param("authorLastName", "")
                .when()
                .get("/getByAuthor")
                .then()
                .spec(LibrarySpecs.getBooksNotFoundResponse)
                .body("error", is("Not Found"));
    }

    @Test
    void verifyGettingBooksByTitleAndAuthor() {
        given()
                .spec(LibrarySpecs.getBooksRequest)
                .param("bookTitle", BOOK_TITLE_1)
                .param("authorFirstName", AUTHOR_FIRST_NAME)
                .param("authorLastName", AUTHOR_LAST_NAME)
                .when()
                .get("/getByTitleAndAuthor")
                .then()
                .spec(LibrarySpecs.getBooksResponse)
                .body("findAll{it.book_title}.book_title.flatten()",
                      hasItem(BOOK_TITLE_1));
    }

    @Test
    void verifyNotFoundBooksByTitleAndAuthor() {
        given()
                .spec(LibrarySpecs.getBooksRequest)
                .param("bookTitle", "")
                .param("authorFirstName", "")
                .param("authorLastName", "")
                .when()
                .get("/getByTitleAndAuthor")
                .then()
                .spec(LibrarySpecs.getBooksNotFoundResponse)
                .body("error", is("Not Found"));
    }
}