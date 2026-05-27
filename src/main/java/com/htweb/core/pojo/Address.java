package com.htweb.core.pojo;

import com.htweb.core.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class Address extends BaseModel {

    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false, referencedColumnName = "id")
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_id", nullable = false, referencedColumnName = "id")
    private District district;

    @OneToMany(mappedBy = "address")
    private Set<Company> companies;

    @OneToMany(mappedBy = "address")
    private Set<Job> jobs;

    public String getFullAddress() {
        List<String> parts = new ArrayList<>();

        if (this.streetAddress != null && !this.streetAddress.isBlank()) {
            parts.add(this.streetAddress);
        }

        if (this.district != null && this.district.getName() != null) {
            parts.add(this.district.getName());
        }

        if (this.city != null && this.city.getName() != null) {
            parts.add(this.city.getName());
        }

        return String.join(", ", parts);
    }
}