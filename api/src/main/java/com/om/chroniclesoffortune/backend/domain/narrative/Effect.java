package com.om.chroniclesoffortune.backend.domain.narrative;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NullMarked
@Entity
@Table(name = "effects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Effect {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_id", nullable = false)
    private Choice choice;

    @Enumerated(EnumType.STRING)
    @Column(name = "effect_type", nullable = false, length = 30)
    private EffectType effectType;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal value;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
