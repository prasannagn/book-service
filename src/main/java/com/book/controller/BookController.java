package com.book.controller;

import com.book.model.Book;
import com.book.model.Tag;
import com.book.resource.BookResource;
import com.book.resource.PageResource;
import com.book.resource.TagResource;
import com.book.service.BookService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin(origins = "*")
@RestController
public class BookController {
    @Resource
    private BookService bookService;

    //Create
    @RequestMapping(value = "/books", method = POST, produces = "application/hal+json")
    public ResponseEntity<BookResource> create(@RequestBody Book book) {
            return ResponseEntity.ok(new BookResource(bookService.save(book)));
    }

    //Upload
    @RequestMapping(value = "/upload", method = POST, produces = "application/hal+json")
    public void create(@RequestParam("data") MultipartFile file, @RequestParam("bookId") String id) throws IOException {
        Map <String,Object> map = new HashMap<>();
        map.put("data",file.getBytes());
        map.put("bookId", id);



        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map <String,Object>> entity = new HttpEntity<Map <String,Object>>(map, headers);

        Object res = restTemplate.exchange("http://localhost:8888", HttpMethod.POST, entity, Object.class);

    }

    //Update
    @RequestMapping(value = "/books/{id}", method = PUT, produces = "application/hal+json")
    public ResponseEntity<BookResource> update(@PathVariable("id") int bookId, @RequestBody Book book) {
        return ResponseEntity.ok(new BookResource(bookService.update(bookId, book)));
    }

    @RequestMapping(value = "/books/{id}/tags", method = PUT, produces = "application/hal+json")
    public ResponseEntity<BookResource> addTags(@PathVariable("id") int bookId, @RequestBody List<Tag> tags) {
        return ResponseEntity.ok(new BookResource(bookService.addTags(bookId, tags)));
    }

    //Read
    @RequestMapping(value = "/books", produces = "application/hal+json", method = GET)
    public ResponseEntity<PageResource<BookResource>> list(@PageableDefault(size = 1, page = 0) Pageable pageable, @RequestParam(required = false) Integer memberId) {
        Page<Book> books = bookService.findAll(pageable);
        Page<BookResource> booksResource = books.map(book -> new BookResource(book, memberId));
        return ResponseEntity.ok(new PageResource<BookResource>(booksResource, "page", "size"));
    }

    @RequestMapping(value = "/books/{id}", produces = "application/hal+json", method = GET)
    public ResponseEntity<BookResource> findOne(@PathVariable("id") int bookId, @RequestParam(required = false) Integer memberId) {
        return ResponseEntity.ok(new BookResource(bookService.findOne(bookId), memberId));
    }

    @RequestMapping(value = "/books/{id}/tags", produces = "application/hal+json", method = GET)
    public ResponseEntity<Resources> findTags(@PathVariable("id") int bookId) {
        Resources resources = Resources.wrap(bookService.findOne(bookId).getTags().stream().map(TagResource::new).collect(Collectors.toList()));
        final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resources.add(new Link(uriString, "self"));
        return ResponseEntity.ok(resources);
    }

    //Delete
    @RequestMapping(value = "/books/{id}", method = DELETE, produces = "application/hal+json")
    public HttpEntity<String> delete(@PathVariable("id") int bookId) {
        bookService.delete(bookId);
        return new HttpEntity<String>("Deleted");
    }
}
