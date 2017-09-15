package com.book.resource;

import com.book.controller.MemberController;
import com.book.model.Member;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.util.CollectionUtils;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class MemberResource extends ResourceSupport {
    @JsonUnwrapped
    private Member member;

    public MemberResource(Member member) {
        this.member = member;
        add(linkTo(methodOn(MemberController.class).findOne(member.getId())).withSelfRel());
        if(!CollectionUtils.isEmpty(member.getBooks())){
            member.getBooks().forEach(book -> {
                add(linkTo(methodOn(MemberController.class).returnBook(member.getId(),book.getId())).withRel("return"));
            });
        }
        //add(linkTo(methodOn(MemberController.class).takeBook(member.getId(),0)).withRel("take"));
    }

    public Member getMember() {
        return member;
    }
}
