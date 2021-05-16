package com.atlasgroup.tmika.highlights;

import com.atlasgroup.tmika.highlights.api.DocumentApi;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Service
public class DocumentApiImpl implements DocumentApi {

    private static final String DOC_LOCATION_TEMPLATE = "classpath:documents/%d.html";

    @Autowired
    ApplicationContext context;

    /**
     * Reading document simplified to keep the task in reasonable boundaries
     *
     * @param documentId the id of the document
     * @return document resource
     */
    @Override
    public Document getDocumentById(long documentId) {
        try (Reader reader = new InputStreamReader(context.getResource(String.format(DOC_LOCATION_TEMPLATE, documentId)).getInputStream())) {
            Document.OutputSettings settings = new Document.OutputSettings();
            settings.escapeMode(Entities.EscapeMode.xhtml);
            final String cleanHtml = Jsoup.clean(FileCopyUtils.copyToString(reader), "", Whitelist.relaxed(), settings);
            return Jsoup.parse(cleanHtml, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public long getElementOffset(long documentId, String elementId) {
        var doc = getDocumentById(documentId);
        var body = doc.body();
        var sourceDiv = doc.getElementById(elementId);
        return body.text().indexOf(sourceDiv.text());
    }


}
