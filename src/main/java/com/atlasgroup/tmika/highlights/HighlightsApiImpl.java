package com.atlasgroup.tmika.highlights;

import com.atlasgroup.tmika.highlights.api.DocumentApi;
import com.atlasgroup.tmika.highlights.api.HighlightsApi;
import com.atlasgroup.tmika.highlights.api.exception.NotFoundException;
import com.atlasgroup.tmika.highlights.domain.Highlight;
import com.atlasgroup.tmika.highlights.domain.HighlightRepository;
import com.atlasgroup.tmika.highlights.web.HighlightDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class HighlightsApiImpl implements HighlightsApi {

    @Autowired
    HighlightRepository repository;

    @Autowired
    DocumentApi documentApi;

    @Override
    public Highlight getHighlight(String username, long documentId) {
        return repository.findByUsernameAndDocumentId(username, documentId)
                .orElseThrow(() -> new NotFoundException("Highlights not found"));
    }

    @Override
    public Highlight addHighlight(String username, long documentId, HighlightDefinition highlightDefinition) {
        final var highlight = repository.findByUsernameAndDocumentId(username, documentId)
                .orElseGet(() -> Highlight.builder()
                        .username(username)
                        .documentId(documentId)
                        .build());
        return repository.save(highlight.addSegment(highlightDefinition.toEntity()));
    }

    @Override
    public String getHighlightedDocument(String username, long documentId) {
        try (Reader reader = new InputStreamReader(documentApi.getDocumentById(documentId).getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
