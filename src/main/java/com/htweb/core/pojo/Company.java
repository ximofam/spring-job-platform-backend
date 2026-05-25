package com.htweb.core.pojo;

import com.htweb.core.enums.CompanyStatus;
import com.htweb.core.enums.CompanyType;
import com.htweb.core.enums.EmployeeSize;
import com.htweb.core.helpers.models.SoftDeleteModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Company extends SoftDeleteModel {
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CompanyStatus status;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CompanyType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_size")
    private EmployeeSize employeeSize;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "description")
    private String description;

    @Column(name = "tax_code")
    private String taxCode;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
}