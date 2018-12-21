package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="activation_codes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivationCode {

    @Id
    private String activationCode;

    @NotNull
    private String userName;


}
