package com.om.chroniclesoffortune.backend.domain.narrative;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NullMarked
@Entity
@Table(name = "narrative_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NarrativeEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Nullable
    @Column(name = "narrative_phase", length = 50)
    private String narrativePhase;

    @Column(name = "required_level", nullable = false)
    private int requiredLevel;

    @Column(nullable = false)
    private int priority;

    @Builder.Default
    @OneToMany(mappedBy = "narrativeEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "narrativeEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TriggerRule> triggerRules = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
