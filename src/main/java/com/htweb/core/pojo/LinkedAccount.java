//package com.htweb.core.pojo;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.io.Serial;
//
//@Entity
//@Table(name = "linked_accounts", uniqueConstraints = {
//        @UniqueConstraint(name = "uq_provider_name_provider_id", columnNames = {"provider_name", "provider_id"})
//})
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class LinkedAccount extends BaseModel {
//    @Serial
//    private static final long serialVersionUID = 1L;
//
//    @Column(name = "email", nullable = false)
//    private String email;
//
//    @Column(name = "provider_id", nullable = false)
//    private String providerId;
//
//    @Column(name = "provider_name", nullable = false, length = 30)
//    private String providerName;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;
//}
