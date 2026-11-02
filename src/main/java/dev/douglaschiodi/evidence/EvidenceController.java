package dev.douglaschiodi.evidence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
public class EvidenceController {
    public record Question(@NotBlank @Size(min=3,max=400) String query, @Min(1) @Max(3) int limit) {}
    public record DocumentView(String id, String title, String text) {}
    private final RetrievalService retrieval;
    private final DocumentCatalog catalog;
    public EvidenceController(RetrievalService retrieval, DocumentCatalog catalog) { this.retrieval = retrieval; this.catalog = catalog; }
    @GetMapping("/workspaces/{workspace}/documents")
    public List<DocumentView> documents(@PathVariable String workspace) {
        return catalog.forWorkspace(workspace).stream().map(d -> new DocumentView(d.id(),d.title(),d.text())).toList();
    }
    @PostMapping("/workspaces/{workspace}/answers")
    public RetrievalService.Answer answer(@PathVariable String workspace, @Valid @RequestBody Question question) {
        return retrieval.answer(workspace, question.query(), question.limit());
    }
}
