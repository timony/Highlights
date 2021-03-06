package com.atlasgroup.tmika.highlights.domain;

import com.atlasgroup.tmika.highlights.api.events.SegmentAddedEvent;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Entity
@Table(name = "highlight", uniqueConstraints = @UniqueConstraint(columnNames = {"documentId", "username"}))
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class Highlight extends AbstractAggregateRoot<Highlight> {

    @Id
    @GeneratedValue
    private Long id;

    private Long documentId;

    private String username;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private List<HighlightSegment> segments = new ArrayList<>();

    public Highlight addSegment(HighlightSegment newSegment) {

        //Prevents duplicate elements
        Set<HighlightSegment> result = new TreeSet<>();

        var addNewSegment = new AtomicBoolean(true);
        segments.stream()
                .sorted()
                .forEach(segment -> {
                    switch (segment.getInteraction(newSegment)) {
                        case NONE:
                            result.add(segment);
                            break;
                        case EQUAL:
                        case ATE:
                            result.add(segment);
                            addNewSegment.set(false);
                            break;
                        case TOUCHING:
                            newSegment.merge(segment);
                            break;
                        case OVERLAPPING_LEFT:
                            segment.setStart(newSegment.getEnd() + 1);
                            result.add(segment);
                            break;
                        case OVERLAPPING_RIGHT:
                            segment.setEnd(newSegment.getStart() - 1);
                            result.add(segment);
                            break;
                        default:
                            break;
                    }
                });

        if (addNewSegment.get()) {
            result.add(newSegment);
        }

        this.segments = new ArrayList<>(result);

        return andEvent(
                SegmentAddedEvent.builder()
                        .username(username)
                        .build()
        );
    }

    public List<HighlightSegment> getAffectedSegments(long start, long end) {
        final var temporal = new HighlightSegment(start, end);
        return segments.stream()
                .filter(segment -> segment.getInteraction(temporal) != HighlightSegment.Interaction.NONE)
                .sorted()
                .collect(Collectors.toList());
    }

}
