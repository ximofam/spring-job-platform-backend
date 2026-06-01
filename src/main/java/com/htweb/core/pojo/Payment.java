package com.htweb.core.pojo;

import com.htweb.core.enums.PaymentMethod;
import com.htweb.core.enums.PaymentStatus;
import com.htweb.core.helpers.models.SoftDeleteModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@SQLRestriction("deleted_at IS NULL")
public class Payment extends SoftDeleteModel {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status = PaymentStatus.PENDING;

    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata")
    private Map<String, Object> metadata;

    @Enumerated(EnumType.STRING)
    @Column(name = "method")
    private PaymentMethod method;

    @Column(name = "transaction_id")
    private String transactionId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "gateway_response")
    private Map<String, Object> gatewayResponse;

    @Column(name = "paid_at")
    private Instant paidAt;
}
