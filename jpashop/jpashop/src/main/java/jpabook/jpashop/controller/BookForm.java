package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private int price;
    private Long id;
    private String name;
    private int stockQuantity;
    private String author;
    private String isbn;

}
