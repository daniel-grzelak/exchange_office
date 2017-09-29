package com.daniel.exchangeoffice.classes;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String password;
    private Long pln;
    private Long gbp;
    private Long eur;
    private Long usd;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getPln() {
        return pln;
    }

    public void setPln(Long pln) {
        this.pln = pln;
    }

    public Long getGbp() {
        return gbp;
    }

    public void setGbp(Long gbp) {
        this.gbp = gbp;
    }

    public Long getEur() {
        return eur;
    }

    public void setEur(Long eur) {
        this.eur = eur;
    }

    public Long getUsd() {
        return usd;
    }

    public void setUsd(Long usd) {
        this.usd = usd;
    }
}
