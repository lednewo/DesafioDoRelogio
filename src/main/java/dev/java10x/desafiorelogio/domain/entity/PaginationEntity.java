package dev.java10x.desafiorelogio.domain.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationEntity<T> {

    private int page;
    private int size;
    private int totalPages;
    private List<T> items;

}
