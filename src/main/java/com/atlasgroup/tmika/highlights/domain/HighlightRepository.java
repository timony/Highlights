package com.atlasgroup.tmika.highlights.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HighlightRepository extends CrudRepository<Highlight, Long> {

    Optional<Highlight> findByUsernameAndDocumentId(String username, long documentId);
}
