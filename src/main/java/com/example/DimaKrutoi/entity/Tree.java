package com.example.DimaKrutoi.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity
@Data
@Accessors(chain = true)
public class Tree {

    @Id
    private String id;
    private String name;
    private Integer height;
    private LocalDateTime creationTS;

}
