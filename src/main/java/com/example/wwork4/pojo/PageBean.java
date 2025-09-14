package com.example.wwork4.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class PageBean {
    private Long total;
    private List items;
}
