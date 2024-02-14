package com.simme.lektion_5_java_ee.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "todo")
public class ToDo {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public ToDo( String subject, String content) {

        this.subject = subject;
        this.content = content;
    }

    private String subject;
    private String content;
    private boolean isDone;

    public ToDo() {
    }



}
