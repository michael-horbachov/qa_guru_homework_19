package guru.qa.libraryservice.specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class LibrarySpecs {

    public static RequestSpecification postAuthorRequest = with()
            .baseUri("http://localhost:8080")
            .basePath("api/author")
            .log().method()
            .log().uri()
            .log().body()
            .contentType(ContentType.JSON);

    public static ResponseSpecification postAuthorResponse = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(LogDetail.STATUS)
            .build();

    public static ResponseSpecification postAuthorBadRequest = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .log(LogDetail.STATUS)
            .build();

    public static RequestSpecification postBookRequest = with()
            .baseUri("http://localhost:8080")
            .basePath("api/book")
            .log().method()
            .log().uri()
            .log().body()
            .contentType(ContentType.JSON);

    public static ResponseSpecification postBookResponse = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(LogDetail.STATUS)
            .build();

    public static RequestSpecification getBooksRequest = with()
            .baseUri("http://localhost:8080")
            .basePath("/api/books")
            .log().method()
            .log().uri();

    public static ResponseSpecification getBooksResponse = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(LogDetail.STATUS)
            .build();

    public static ResponseSpecification getBooksNotFoundResponse = new ResponseSpecBuilder()
            .expectStatusCode(404)
            .log(LogDetail.STATUS)
            .build();
}