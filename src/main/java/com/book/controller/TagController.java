package com.book.controller;

import com.book.model.Tag;
import com.book.repository.TagRepository;
import com.book.resource.PageResource;
import com.book.resource.TagResource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class TagController {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Resource
    private TagRepository repository;

    //Create
    @RequestMapping(value = "/tags", method = POST, produces = "application/hal+json")
    public ResponseEntity<TagResource> create(@RequestBody Tag tag) {
        return ResponseEntity.ok(new TagResource(repository.save(tag)));
    }

    //Update
    @RequestMapping(value = "/tags/{id}", produces = "application/hal+json", method = PUT)
    public ResponseEntity<TagResource> update(@PathVariable("id") int tagId, @RequestBody Tag source) {
        Tag target = repository.findOne(tagId);
        BeanUtils.copyProperties(source, target, "id");
        return ResponseEntity.ok(new TagResource(repository.save(target)));
    }

    //Read
    @RequestMapping(value = "/tags", produces = "application/hal+json", method = GET)
    public ResponseEntity<PageResource<TagResource>> list(@PageableDefault(size = 1, page = 0) Pageable pageable) {
        Page<Tag> tags = repository.findAll(pageable);
        Page<TagResource> tagResource = tags.map(TagResource::new);
        return ResponseEntity.ok(new PageResource<>(tagResource, "page", "size"));
    }

    @RequestMapping(value = "/tags/{id}", produces = "application/hal+json", method = GET)
    public ResponseEntity<TagResource> findOne(@PathVariable("id") int tagId) {
        return ResponseEntity.ok(new TagResource(repository.findOne(tagId)));
    }

    //Delete
    @RequestMapping(value = "/tags/{id}", produces = "application/hal+json", method = DELETE)
    public HttpEntity<String> delete(@PathVariable("id") int tagId) {
        repository.delete(tagId);
        return new HttpEntity<String>("Deleted");
    }


}
