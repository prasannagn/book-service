package com.book.service;

import com.book.model.Book;
import com.book.model.Member;
import com.book.model.Status;
import com.book.repository.BookRepository;
import com.book.repository.MemberRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.annotation.Resource;

public class MemberService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Resource
    private MemberRepository repository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Resource
    private BookRepository bookRepository;

    public Member save(Member member) {
        return repository.save(member);
    }

    public Member findOne(int memberId) {
        return repository.findOne(memberId);
    }

    public Page<Member> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public Member update(int memberId, Member source) {
        Member target = repository.findOne(memberId);
        BeanUtils.copyProperties(source, target, "id");
        return repository.save(target);
    }

    public Member takeBook(int id, int bookId) {
        Member member = repository.findOne(id);
        Book book = bookRepository.findOne(bookId);

        if (book.getStatus() == Status.AVAILABLE) {
            member.getBooks().add(book);
            book.setStatus(Status.ISSUED);
            bookRepository.save(book);
            return repository.save(member);
        }
        throw new RuntimeException();
    }

    public Member returnBook(int id, int bookId) {
        Member member = repository.findOne(id);
        Book book = bookRepository.findOne(bookId);
        if (book.getStatus() == Status.ISSUED && member.getBooks().contains(book)) {
            member.getBooks().remove(book);
            book.setStatus(Status.AVAILABLE);
            bookRepository.save(book);
            return repository.save(member);
        }
        throw new RuntimeException();
    }
}
