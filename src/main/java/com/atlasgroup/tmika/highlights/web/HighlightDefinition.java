package com.atlasgroup.tmika.highlights.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HighlightDefinition {

    private String sourceDivId;
    private long offset;
    private String text;

}
