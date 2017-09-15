package com.book.resource;

import com.book.controller.TagController;
import com.book.model.Tag;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class TagResource extends ResourceSupport {
    @JsonUnwrapped
    private Tag tag;

    public TagResource(Tag tag) {
        this.tag = tag;
        add(linkTo(methodOn(TagController.class).findOne(tag.getId())).withSelfRel());
    }

    public Tag getTag() {
        return tag;
    }
}
