package com.om.chroniclesoffortune.backend.domain.kingdom;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NullMarked
@Entity
@Table(name = "kingdom_states")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KingdomState {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kingdom_id", nullable = false, unique = true)
    private Kingdom kingdom;

    @Column(precision = 15, scale = 2)
    private BigDecimal gold;

    @Column(precision = 15, scale = 2)
    private BigDecimal mana;

    @Column(precision = 15, scale = 2)
    private BigDecimal resilience;

    @Column(precision = 5, scale = 2)
    private BigDecimal stability;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
