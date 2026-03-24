package com.om.chroniclesoffortune.backend.domain.narrative;

import com.om.chroniclesoffortune.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;

import java.time.LocalDateTime;
import java.util.UUID;

@NullMarked
@Entity
@Table(
        name = "resolved_events",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_resolved_events_user_event",
                columnNames = {"user_id", "narrative_event_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResolvedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "narrative_event_id", nullable = false)
    private NarrativeEvent narrativeEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "choice_id", nullable = false)
    private Choice choice;

    @Column(name = "resolved_at", nullable = false)
    private LocalDateTime resolvedAt;
}
