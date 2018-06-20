package com.cgi.dentistapp.dao.entity;

import javax.persistence.*;

@Entity
@Table(name = "doctor")
public class DoctorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    public DoctorEntity() {
    }

    public DoctorEntity(String type, String name) {
        this.setType(type);
        this.setName(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
