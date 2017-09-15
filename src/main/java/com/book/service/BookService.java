package com.book.service;

import com.book.model.Book;
import com.book.model.Tag;
import com.book.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;
import java.util.List;

public class BookService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Resource
    private BookRepository repository;

    public Book findOne(int bookId) {
        return repository.findOne(bookId);
    }

    public Page<Book> findAll(Pageable pagable) {
        return repository.findAll(pagable);
    }


    public Book save(Book book) {
        return repository.save(book);
    }

    public Book addTags(int bookId, List<Tag> tags) {
        Book book = findOne(bookId);
        book.replaceTags(tags);
        return repository.save(book);
    }

    public Book update(int bookId, Book source) {
        Book target = findOne(bookId);
        Book.copy(source, target);
        return repository.save(target);
    }

    public void delete(int bookId) {
        repository.delete(bookId);
    }
}
