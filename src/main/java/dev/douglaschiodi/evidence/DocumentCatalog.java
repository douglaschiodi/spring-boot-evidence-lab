package dev.douglaschiodi.evidence;
import java.util.List;
import org.springframework.stereotype.Component;
@Component
public class DocumentCatalog {
    public record Document(String id, String workspace, String title, String text) {}
    private final List<Document> documents = List.of(
        new Document("orders-01", "commerce", "Reliable order intake", "Order intake uses an idempotency key scoped to the merchant. Repeated submissions with the same key return the original order. A different payload with the same key is rejected. Payment reconciliation runs separately; an accepted order does not prove payment settlement."),
        new Document("hooks-02", "commerce", "Webhook delivery", "Webhook events enter a durable outbox before delivery. Failed delivery uses bounded retries with exponential backoff. Consumers deduplicate events by merchant and event identifier. Exhausted retries move to a dead-letter queue for manual investigation."),
        new Document("agents-03", "commerce", "AI assistant boundaries", "The AI assistant retrieves approved documents before proposing a response. Documents are data, not executable instructions. Calibration and financial actions require human approval. If evidence is missing, the assistant must abstain instead of inventing a policy."),
        new Document("lab-01", "laboratory", "Calibration review", "Laboratory calibration parameters are proposals until a qualified operator reviews them. A calibration record contains the instrument identifier, reference measurement, uncertainty and approval timestamp. A proposed value must never silently replace an approved parameter."),
        new Document("lab-02", "laboratory", "Instrument telemetry", "Instrument telemetry is buffered locally during network outages. Sequence identifiers support duplicate detection. Operators monitor missing readings and sensor health. Telemetry ingestion and calibration approval are separate responsibilities.")
    );
    public List<Document> forWorkspace(String workspace) {
        if (!List.of("commerce", "laboratory").contains(workspace)) throw new IllegalArgumentException("Unknown demo workspace");
        return documents.stream().filter(d -> d.workspace().equals(workspace)).toList();
    }
}
