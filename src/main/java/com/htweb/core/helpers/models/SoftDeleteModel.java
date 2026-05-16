package com.htweb.core.helpers.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
//@Filter(name = "activeFilter", condition = "deleted_at IS NULL")
public class SoftDeleteModel extends BaseModel {
    @Column(name = "deleted_at")
    protected Instant deletedAt;

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
