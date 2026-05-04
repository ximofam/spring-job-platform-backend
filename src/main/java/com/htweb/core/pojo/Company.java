package com.htweb.core.pojo;

import com.htweb.core.enums.CompanyType;
import com.htweb.core.enums.EmployeeSize;
import com.htweb.core.helpers.models.SoftDeleteModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company extends SoftDeleteModel {

    @Column(name = "logo")
    private String logo;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CompanyType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_size")
    private EmployeeSize employeeSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "description")
    private String description;

    @Column(name = "tax_code")
    private String taxCode;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private Set<EmployerProfile> employerProfiles;

}