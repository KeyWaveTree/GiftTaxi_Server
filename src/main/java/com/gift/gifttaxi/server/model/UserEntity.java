package com.gift.gifttaxi.server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="user")
public class UserEntity {
    @Id
    @GeneratedValue
    public Long id;
    public String name;
    public String phone;
    public String password;
}
