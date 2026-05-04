package com.htweb.core.pojo;

import com.htweb.core.helpers.models.SoftDeleteModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Country extends SoftDeleteModel {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private Set<User> users;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private Set<Company> companies;

}
