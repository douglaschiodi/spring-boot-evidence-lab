# Architecture and trade-offs

```text
Browser (same origin)
  -> EvidenceController: validated REST contract
  -> DocumentCatalog: allowlisted collection selection
  -> RetrievalService: tokenize -> overlap -> threshold -> stable rank -> bounded evidence
  -> JSON response: status, mode, cited excerpts and retrieval metadata
```

The browser uses `textContent`, never inserts retrieved text as HTML, and ignores stale results after collection changes. The corpus is immutable and process-local; there is no persistence, upload or ingestion endpoint. Request validation bounds the query and result count; unknown JSON fields and unknown workspaces are rejected.

## Retrieval v1

Normalize Unicode, lowercase with ROOT locale, split non-alphanumeric characters and remove a short English stop-word list. Each distinct query term contributes at most once. Score by matched query terms, then document ID for deterministic ties. Keep documents with overlap > 0 and query coverage >= 0.25, up to the requested 1–3 sources. All excerpts are bounded by the fixed fixture corpus.

The response is an extract, not a model assertion. It exposes `mode=extractive-local` and the algorithm version. No implicit action or instruction is executed. Collection separation is a selection property, **not tenant authorization**.

## Alternatives deliberately deferred

BM25, embeddings and rerankers could improve relevance but introduce additional evaluation and dependencies. An LLM could synthesize evidence but requires factuality/citation tests and a provider boundary. A vector store and durable corpus are unnecessary for five inspectable fixtures. A JS framework is unnecessary for this one-screen client; a dedicated frontend can reuse the same API.

## Security and operational boundary

Local-only default, no authentication, no CORS exceptions, no secret-bearing integration. No claim of production hardening, comprehensive prompt-injection defense or complete RAG evaluation. Only synthetic data is appropriate. CI and local integration tests validate the implemented contracts, not future model quality.
