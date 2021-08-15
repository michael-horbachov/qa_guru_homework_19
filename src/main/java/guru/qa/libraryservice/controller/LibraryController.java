package guru.qa.libraryservice.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import guru.qa.libraryservice.domain.Author;
import guru.qa.libraryservice.domain.AuthorInfo;
import guru.qa.libraryservice.domain.Book;
import guru.qa.libraryservice.domain.BookInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LibraryController {

    private Map<String, BookInfo> books = Map.of(
            "Stranger in a Strange Land",
            BookInfo.builder()
                    .bookTitle("Stranger in a Strange Land")
                    .author(Author.builder().authorFirstName("Robert").authorLastName("Heinlein").build())
                    .build(),
            "Brave New World",
            BookInfo.builder()
                    .bookTitle("Brave New World")
                    .author(Author.builder().authorFirstName("Aldous").authorLastName("Huxley").build())
                    .build(),
            "White Fang",
            BookInfo.builder()
                    .bookTitle("White Fang")
                    .author(Author.builder().authorFirstName("Jack").authorLastName("London").build())
                    .build(),
            "The Call of the Wild",
            BookInfo.builder()
                    .bookTitle("The Call of the Wild")
                    .author(Author.builder().authorFirstName("Jack").authorLastName("London").build())
                    .build()
    );

    @PostMapping("api/author")
    @ApiOperation("Add author to library")
    public AuthorInfo addAuthor(@RequestBody Author author) {
        if (author.getAuthorFirstName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (author.getAuthorLastName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            return AuthorInfo.builder()
                             .addingDate(new Date())
                             .authorFirstName(author.getAuthorFirstName())
                             .authorLastName(author.getAuthorLastName())
                             .build();
        }
    }

    @PostMapping("api/book")
    @ApiOperation("Add book to library")
    public BookInfo addBook(@RequestBody Book book) {
        return BookInfo.builder()
                       .addingDate(new Date())
                       .bookTitle(book.getBookTitle())
                       .author(Author.builder().authorFirstName(book.getAuthor().getAuthorFirstName())
                                     .authorLastName(book.getAuthor().getAuthorLastName()).build())
                       .build();
    }

    @GetMapping("api/books/getAll")
    @ApiOperation("Get all books")
    public List<BookInfo> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    @GetMapping("api/books/getByAuthor")
    @ApiOperation("Get all books by author")
    public List<BookInfo> getBooksByAuthor(String authorFirstName, String authorLastName) {
        List<BookInfo> foundBooks = books.values()
                                         .stream()
                                         .filter(bookInfo -> bookInfo.getAuthor().getAuthorFirstName().equals(authorFirstName) &&
                                                 bookInfo.getAuthor().getAuthorLastName().equals(authorLastName))
                                         .collect(Collectors.toList());
        if (foundBooks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return foundBooks;
        }
    }

    @GetMapping("api/books/getByTitleAndAuthor")
    @ApiOperation("Get book by title and author")
    public List<BookInfo> getBookByAuthor(String bookTitle, String authorFirstName, String authorLastName) {
        List<BookInfo> foundBooks = books.values()
                                         .stream()
                                         .filter(bookInfo -> bookInfo.getBookTitle().equals(bookTitle) &&
                                                 bookInfo.getAuthor().getAuthorLastName().equals(authorLastName) &&
                                                 bookInfo.getAuthor().getAuthorFirstName().equals(authorFirstName))
                                         .collect(Collectors.toList());
        if (foundBooks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return foundBooks;
        }
    }
}