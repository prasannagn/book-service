package com.book;

import com.book.service.BookService;
import com.book.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LibraryConfiguration {

    @Bean
    BookService bookService() {
        return new BookService();
    }

    @Bean
    MemberService memberService(){
        return new MemberService();
    }
}
