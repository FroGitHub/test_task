package org.example;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */

public class DocumentManager {

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */

    private final Map<String, Document> storage = new HashMap<>();

    public Document save(Document document) {


        if (document.getId() == null || !storage.containsKey(document.getId())){
            document.setId(UUID.randomUUID().toString());
        }

        storage.put(document.getId(), document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        List<Document> result = new ArrayList<>();

        for (Document doc : storage.values()) {
            if (matchesTitlePrefixes(doc, request.getTitlePrefixes()) &&
                    containsContents(doc, request.getContainsContents()) &&
                    matchesAuthorIds(doc, request.getAuthorIds()) &&
                    matchesCreatedRange(doc, request.getCreatedFrom(), request.getCreatedTo())) {
                result.add(doc);
            }
        }

        return result;
    }

    private boolean matchesTitlePrefixes(Document doc, List<String> prefixes) {
        if (prefixes == null || prefixes.isEmpty()) {
            return true;
        }
        for (String prefix : prefixes) {
            if (doc.getTitle().startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private boolean containsContents(Document doc, List<String> contents) {
        if (contents == null || contents.isEmpty()) {
            return true;
        }
        for (String content : contents) {
            if (doc.getContent().contains(content)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesAuthorIds(Document doc, List<String> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return true;
        }
        return authorIds.contains(doc.getAuthor().getId());
    }

    private boolean matchesCreatedRange(Document doc, Instant from, Instant to) {
        Instant created = doc.getCreated();
        if (from != null && created.isBefore(from)) {
            return false;
        }
        if (to != null && created.isAfter(to)) {
            return false;
        }
        return true;
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}