package com.atlasgroup.tmika.highlights;

import com.atlasgroup.tmika.highlights.api.HighlightsApi;
import com.atlasgroup.tmika.highlights.domain.Highlight;
import com.atlasgroup.tmika.highlights.domain.HighlightRepository;
import com.atlasgroup.tmika.highlights.web.HighlightDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class HighlightsApiImpl implements HighlightsApi {

    @Autowired
    HighlightRepository repository;

    @Override
    public Highlight addHighlight(String username, long documentId, HighlightDefinition highlightDefinition) {
        final var highlight = repository.findByUsernameAndDocumentId(username, documentId)
                .orElseGet(() -> Highlight.builder()
                        .username(username)
                        .documentId(documentId)
                        .build());
        return repository.save(highlight.addSegment(highlightDefinition.toEntity()));
    }
}
