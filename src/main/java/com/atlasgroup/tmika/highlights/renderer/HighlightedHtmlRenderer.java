package com.atlasgroup.tmika.highlights.renderer;

import com.atlasgroup.tmika.highlights.domain.Highlight;
import com.atlasgroup.tmika.highlights.domain.HighlightSegment;
import org.dom4j.DocumentHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.Optional;

public class HighlightedHtmlRenderer implements HtmlRenderer {

    private Highlight highlight;
    private Document template;
    private boolean renderRandomStyles = false;

    private long charsRendered;

    @Override
    public String render() {
        charsRendered = 0;

        var doc = DocumentHelper.createDocument();
        var html = doc.addElement("html");
        if (renderRandomStyles) {
            var head = html.addElement("head");
            renderStyle(head);
        }

        var body = html.addElement("body");
        renderNode(body, template.body());
        return doc.asXML();
    }


    private void renderNode(org.dom4j.Element parentNode, Element fromNode) {
        fromNode.attributes().asList().forEach(attribute -> parentNode.addAttribute(attribute.getKey(), attribute.getValue()));
        for (Node childNode : fromNode.childNodes()) {
            if (childNode instanceof TextNode) {
                renderTextNode(parentNode, (TextNode) childNode);
            } else if (childNode instanceof Element) {
                renderNode(parentNode.addElement(childNode.nodeName()), (Element) childNode);
            }
        }
    }

    private void renderTextNode(org.dom4j.Element parentNode, TextNode fromNode) {
        final var text = fromNode.text();
        final var affectedSegments = highlight.getAffectedSegments(charsRendered, charsRendered + text.length());

        var sb = new StringBuilder();
        Optional<HighlightSegment> current = Optional.empty();

        for (var i = 0; i < text.length(); i++) {
            int finalI = i;
            var theCharacter = text.charAt(i);

            final Optional<HighlightSegment> first = affectedSegments.stream()
                    .filter(hs -> hs.contains(charsRendered + finalI))
                    .findFirst();

            if (!current.equals(first)) {
                flush(parentNode, sb.toString(), current);
                current = first;
                sb = new StringBuilder();
            }
            sb.append(theCharacter);

        }

        flush(parentNode, sb.toString(), current);

        charsRendered += text.length();
    }

    private void flush(org.dom4j.Element parentNode, String text, Optional<HighlightSegment> current) {
        current.map(c -> DocumentHelper.createElement("span")
                .addAttribute("class", c.getId())
                .addText(text))
                .ifPresentOrElse(parentNode::add, () -> parentNode.add(DocumentHelper.createText(text)));
    }

    public HighlightedHtmlRenderer withHighlightDefinition(Highlight highlight) {
        this.highlight = highlight;
        return this;
    }

    public HighlightedHtmlRenderer withDocumentTemplate(Document template) {
        this.template = template;
        return this;
    }

    public HighlightedHtmlRenderer renderRandomStyles(boolean render) {
        this.renderRandomStyles = render;
        return this;
    }

    private void renderStyle(org.dom4j.Element headNode) {
        var sb = new StringBuilder();
        highlight.getSegments().stream()
                .map(HighlightSegment::getId)
                .map(id -> String.format(".%s { background-color: %s; }%n", id, HexColor.random()))
                .forEach(sb::append);

        headNode.addElement("style").addText(sb.toString());
    }

}
