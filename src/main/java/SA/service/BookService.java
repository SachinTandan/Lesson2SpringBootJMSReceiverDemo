package SA.service;

import SA.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.List;


@Service
public class BookService {
    @Autowired
    private RestOperations restTemplate;
    private final String BOOK_API_URL = "http://localhost:8080/books";

    public Book getBook(String isbn) {
        return restTemplate.getForObject(BOOK_API_URL + "/" + isbn, Book.class);
    }


    public List<Book> getAllBooks() {
        ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(BOOK_API_URL, HttpMethod.GET, null, new ParameterizedTypeReference<List<Book>>(){});
        return responseEntity.getBody();
    }


    public Book addBook( Book book) {
        return restTemplate.postForObject(BOOK_API_URL, book, Book.class);
    }


    public void updateBook( String isbn,  Book book) {
        restTemplate.put(BOOK_API_URL + "/" + isbn, book);
    }


    public void deleteBook( String isbn) {
        restTemplate.delete(BOOK_API_URL + "/" + isbn);
    }
    public void callRestServer() {

        var greeting= restTemplate.getForObject(BOOK_API_URL+"/hello", String.class);
        System.out.println(greeting);
    }


}
