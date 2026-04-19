package com.htweb.core.pojo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.io.Serial;
import java.util.Set;

/**
 * @author PC
 */
@Entity
@Table(name = "permissions", uniqueConstraints = {
        @UniqueConstraint(name = "uq_module_action", columnNames = {"module", "action"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission extends BaseModel {
    @Serial
    private static final long serialVersionUID = 1L;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 50)
    private String module;

    @Column(nullable = false, length = 50)
    private String action;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "is_active = :isActive")
    private Set<Role> roles;
}