package com.floriantoenjes.instateam.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@Entity
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Length(min = 5, max = 30)
    private String name;

    @ManyToOne
    private Role role;

    public Collaborator() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
