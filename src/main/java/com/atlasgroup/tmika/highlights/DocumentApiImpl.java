package com.atlasgroup.tmika.highlights;

import com.atlasgroup.tmika.highlights.api.DocumentApi;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.stream.Stream;

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
            final var file = context.getResource(String.format(DOC_LOCATION_TEMPLATE, documentId)).getFile();
            return getDocumentByFile(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Document getDocumentByFile(File file) throws IOException {
        var sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
            stream.forEach(line -> sb.append(line
                    .replace("\n", "")
                    .replace("\r", "")));
            return Jsoup.parse(sb.toString(), StandardCharsets.UTF_8.name());
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
