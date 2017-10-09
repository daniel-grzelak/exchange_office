package com.daniel.exchangeoffice.classes;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;
    private String password;
    private BigDecimal pln;
    private BigDecimal gbp;
    private BigDecimal eur;
    private BigDecimal usd;



    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


}
