package com.htweb.core.pojo;

import com.htweb.core.helpers.models.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category extends BaseModel {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Job> jobs = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void validateCategoryLevel() {
        if (this.parent != null) {
            if (this.parent.getParent() != null) {
                throw new IllegalStateException("Hệ thống chỉ cho phép tối đa 2 cấp (Cha -> Con).");
            }
        }

        if (this.parent != null && this.id != null && !this.children.isEmpty()) {
            throw new IllegalStateException("Danh mục này đang có danh mục con bên trong, không thể chuyển thành danh mục con.");
        }
    }
}