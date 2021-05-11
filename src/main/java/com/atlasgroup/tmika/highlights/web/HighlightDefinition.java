package com.atlasgroup.tmika.highlights.web;

import com.atlasgroup.tmika.highlights.domain.HighlightSegment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HighlightDefinition {

    private String startElementId;
    private long offset;
    private long length;

    public HighlightSegment toEntity() {
        return HighlightSegment.builder()
                .startElementId(startElementId)
                .start(offset)
                .end(offset + length)
                .build();
    }
}
