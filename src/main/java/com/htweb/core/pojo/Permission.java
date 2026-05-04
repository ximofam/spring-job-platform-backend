package com.htweb.core.pojo;

import com.htweb.core.helpers.models.SoftDeleteModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;

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
public class Permission extends SoftDeleteModel {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "module", nullable = false, length = 50)
    private String module;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "deleted_at IS NULL")
    private Set<Role> roles;

}