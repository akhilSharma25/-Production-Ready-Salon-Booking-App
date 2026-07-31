package com.akhil.DTO;

import com.akhil.domain.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    private String paymentLinkId;
    @Column(nullable = false)

    private Long userId;
    @Column(nullable = false)
    private Long bookingId;
    @Column(nullable = false)
    private Long salonId;
}
