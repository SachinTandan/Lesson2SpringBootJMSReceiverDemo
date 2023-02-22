package SA.controller;


import SA.entity.Book;
import SA.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.jms.Queue;
import java.util.List;


@RestController
public class BookController {

    private final String BOOK_API_URL = "http://localhost:8080/books";
    private RestTemplate restTemplate;
    @Autowired
    BookService bookService;
    private final JmsTemplate jmsTemplate;
    private final Queue queue;
    public BookController(RestTemplate restTemplate, JmsTemplate jmsTemplate, Queue queue) {
        this.restTemplate = restTemplate;
        this.jmsTemplate = jmsTemplate;
        this.queue = queue;
    }



    @GetMapping("/books/{isbn}")
    public Book getBook(@PathVariable String isbn) {
        return bookService.getBook(isbn);
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        ResponseEntity<List<Book>> responseEntity = (ResponseEntity<List<Book>>) bookService.getAllBooks();
        return responseEntity.getBody();
    }

    @PostMapping("/books")
    public Book addBook(@RequestBody Book book) {
        var savedBook= bookService.addBook(book);
        jmsTemplate.convertAndSend(queue, savedBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook).getBody();
    }

    @PutMapping("/books/{isbn}")
    public void updateBook(@PathVariable String isbn, @RequestBody Book book) {
        jmsTemplate.convertAndSend(queue, book);
        bookService.updateBook(isbn,book);
    }

    @DeleteMapping("/books/{isbn}")
    public void deleteBook(@PathVariable String isbn) {
        jmsTemplate.convertAndSend(queue, isbn);
        bookService.deleteBook(isbn);
    }

}
