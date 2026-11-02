package dev.douglaschiodi.evidence;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
@Service
public class RetrievalService {
    public record Evidence(String id, String title, String excerpt, int matchedTerms, double coverage) {}
    public record Answer(String mode, String status, String workspace, String answer, List<Evidence> evidence, int queryTerms, String retrieval) {}
    private static final Set<String> STOP = Set.of("the","a","an","and","or","of","to","in","on","for","is","are","how","what","does","do","with","it","can","we","our","be","as","by","this","that","about","please");
    private static final Pattern NON_WORD = Pattern.compile("[^a-z0-9]+");
    private final DocumentCatalog catalog;
    public RetrievalService(DocumentCatalog catalog) { this.catalog = catalog; }
    static Set<String> tokens(String input) {
        String ascii = Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
        var out = new LinkedHashSet<String>();
        for (String word : NON_WORD.split(ascii)) if (word.length() > 1 && !STOP.contains(word)) out.add(word);
        return out;
    }
    public Answer answer(String workspace, String query, int limit) {
        var docs = catalog.forWorkspace(workspace);
        var terms = tokens(query);
        var hits = new ArrayList<Evidence>();
        if (!terms.isEmpty()) for (var doc : docs) {
            var vocabulary = tokens(doc.title() + " " + doc.text());
            int overlap = (int) terms.stream().filter(vocabulary::contains).count();
            double coverage = overlap / (double) terms.size();
            if (overlap > 0 && coverage >= 0.25) hits.add(new Evidence(doc.id(), doc.title(), doc.text(), overlap, Math.round(coverage * 1000.0) / 1000.0));
        }
        hits.sort(Comparator.comparingInt(Evidence::matchedTerms).reversed().thenComparing(Evidence::id));
        var selected = hits.stream().limit(limit).toList();
        String response = selected.isEmpty() ? "No sufficiently matching evidence in this workspace. Try a more specific question about the available documents." : selected.stream().map(e -> "[" + e.id() + "] " + e.excerpt()).reduce((a,b) -> a + "\n\n" + b).orElseThrow();
        return new Answer("extractive-local", selected.isEmpty() ? "abstained" : "supported", workspace, response, selected, terms.size(), "normalized-token-overlap-v1");
    }
}
