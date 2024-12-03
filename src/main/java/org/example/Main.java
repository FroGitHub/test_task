package org.example;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        DocumentManager manager = new DocumentManager();

        // save
        DocumentManager.Document doc1 = DocumentManager.Document.builder()
                .title("First Document")
                .content("This is a test document.")
                .author(DocumentManager.Author.builder().id("1").name("John Doe").build())
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDoc1 = manager.save(doc1);
        System.out.println("Saved Document: " + savedDoc1);

        // findById
        Optional<DocumentManager.Document> foundDoc = manager.findById(savedDoc1.getId());
        System.out.println("Found Document: " + foundDoc.orElse(null));

        // search
        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Second Document")
                .content("Another document for testing.")
                .author(DocumentManager.Author.builder().id("2").name("Jane Doe").build())
                .created(Instant.now())
                .build();

        manager.save(doc2);

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("First"))
                .authorIds(List.of("1"))
                .build();

        List<DocumentManager.Document> searchResults = manager.search(searchRequest);
        System.out.println("Search Results: " + searchResults);
    }
}