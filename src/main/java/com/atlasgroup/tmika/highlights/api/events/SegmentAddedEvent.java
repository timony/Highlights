package com.atlasgroup.tmika.highlights.api.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SegmentAddedEvent {

    private String username;

    private long documentId;

}
