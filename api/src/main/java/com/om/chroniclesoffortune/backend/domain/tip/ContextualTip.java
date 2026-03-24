package com.om.chroniclesoffortune.backend.domain.tip;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.NullMarked;

import java.time.LocalDateTime;
import java.util.UUID;

@NullMarked
@Entity
@Table(name = "contextual_tips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContextualTip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipContext context;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipCategory category;

    @Column(nullable = false)
    private boolean active;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
