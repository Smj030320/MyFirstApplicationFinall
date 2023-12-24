package com.jnu.student.data;

import java.io.Serializable;

public class BookItem implements Serializable{
    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    private String name;
    private final int imageId;

    public BookItem(String name_, int bookId_) {
        this.name=name_;
        this.imageId=bookId_;
    }
    public void setName(String name) {
        this.name = name;
    }
}