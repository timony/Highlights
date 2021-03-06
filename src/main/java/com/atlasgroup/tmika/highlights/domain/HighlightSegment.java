package com.atlasgroup.tmika.highlights.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Comparator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HighlightSegment implements Comparable<HighlightSegment> {

    @Builder
    public HighlightSegment(long start, long end) {
        this.start = start;
        this.end = end;
        id = "s" + hashCode();  //nasty but out of scope of the assignment
    }

    @EqualsAndHashCode.Exclude
    private String id;

    @NotEmpty
    private long start;

    @NotEmpty
    private long end;

    @JsonIgnore
    public long getLength() {
        return end - start;
    }

    /**
     * Determines the type of interaction between two segments.
     * Null segment or segment with different element id is resolved as NONE interaction
     * @param other the other segment
     * @return the interaction type
     */
    public Interaction getInteraction(HighlightSegment other) {
        if (other == null || start > other.end || end < other.start) {
            return Interaction.NONE;
        } else if (start == other.start && end == other.end) {
            return Interaction.EQUAL;
        } else if (start >= other.start && end <= other.end && getLength() < other.getLength()) {
            return Interaction.EATEN;
        } else if (start <= other.start && end >= other.end && getLength() > other.getLength()) {
            return Interaction.ATE;
        } else if (start == other.end || end == other.start) {
            return Interaction.TOUCHING;
        } else if (start > other.start) {
            return Interaction.OVERLAPPING_LEFT;
        } else if (end < other.end){
            return Interaction.OVERLAPPING_RIGHT;
        } else {
            throw new IllegalArgumentException(String.format("Segment interaction not recognized: %s and %s", this, other));
        }
    }

    public void merge(HighlightSegment other) {
        if (getInteraction(other).equals(Interaction.TOUCHING)) {
            start = Math.min(start, other.start);
            end = Math.max(end, other.end);
        }
    }

    @Override
    public int compareTo(@NotNull HighlightSegment other) {
        return Comparator.comparing(HighlightSegment::getStart)
                .thenComparing(HighlightSegment::getEnd)
                .compare(this, other);
    }

    public boolean contains(long index) {
        return start <= index && end >= index;
    }

    public enum Interaction {
        NONE, EQUAL, EATEN, ATE, TOUCHING, OVERLAPPING_LEFT, OVERLAPPING_RIGHT
    }
}
