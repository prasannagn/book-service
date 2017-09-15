package com.book.model;


import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;

    private String description;

    @OneToMany(targetEntity = Tag.class, fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Tag> tags;

    private Status status;

    private String href;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void replaceTags(List<Tag> tags) {
        if (null == this.getTags()) {
            this.setTags(tags);
        } else {
            this.getTags().clear();
            if (!CollectionUtils.isEmpty(tags)) {
                this.getTags().addAll(tags);
            }
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    public static void copy(Book source, Book target) {
        BeanUtils.copyProperties(source, target, "id", "tags");
        target.replaceTags(source.getTags());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (id != book.id) return false;
        if (!title.equals(book.title)) return false;
        if (description != null ? !description.equals(book.description) : book.description != null) return false;
        if (tags != null ? !tags.equals(book.tags) : book.tags != null) return false;
        if (status != book.status) return false;
        return href != null ? href.equals(book.href) : book.href == null;
    }

    @Override
    public int hashCode() {
        return id;
    }
}