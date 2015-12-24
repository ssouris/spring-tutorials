package com.yetanotherdev.domain;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.geo.Point;

import java.io.Serializable;

@Entity
public class Venue implements Serializable {
    private static final long serialVersionUID = 3880527356800211802L;

    @Id
    private String id;

    private String name;

    private Point location;

    @PersistenceConstructor
    public Venue(String name, Point location) {
        super();
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Point getLocation() {
        return location;
    }

}
