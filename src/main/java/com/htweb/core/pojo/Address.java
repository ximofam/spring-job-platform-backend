package com.htweb.core.pojo;

import com.htweb.core.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class Address extends BaseModel {

    @Column(name = "street_address", nullable = false, length = 255)
    private String streetAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false, referencedColumnName = "id")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false, referencedColumnName = "id")
    private District district;

    @OneToMany(mappedBy = "address")
    private Set<Company> companies;
}