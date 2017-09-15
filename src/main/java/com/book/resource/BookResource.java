package com.book.resource;

import com.book.controller.BookController;
import com.book.controller.MemberController;
import com.book.model.Book;
import com.book.model.Status;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class BookResource extends ResourceSupport {
    @JsonUnwrapped
    private Book book;

    public BookResource(Book book) {
        this.book = book;
        add(linkTo(methodOn(BookController.class).findOne(book.getId(),null)).withSelfRel());
        add(linkTo(methodOn(BookController.class).findOne(book.getId(),null)).slash("tags").withRel("tags"));
    }

    public BookResource(Book book, Integer memberId) {
        this.book = book;
        add(linkTo(methodOn(BookController.class).findOne(book.getId(),null)).withSelfRel());
        add(linkTo(methodOn(BookController.class).findOne(book.getId(),null)).slash("tags").withRel("tags"));
        if (null != memberId && book.getStatus() == Status.AVAILABLE) {
            add(linkTo(methodOn(MemberController.class).takeBook(memberId, book.getId())).withRel("take"));
        }
    }

    public Book getBook() {
        return book;
    }
}
