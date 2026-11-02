# Spring Boot Evidence Lab

A small, inspectable Java backend and browser client for document retrieval and grounded, cited responses.

**Java 17 · Spring Boot 3.5 · REST · Bean Validation · JUnit · MockMvc · HTML/CSS/JavaScript**

[Português](README.pt-BR.md) · [Architecture & trade-offs](ARCHITECTURE.md)

## Run

Requires JDK 17+ and Maven 3.6.3+. Ensure `JAVA_HOME` points to the JDK used by Maven.

```sh
mvn verify
mvn spring-boot:run
```

Open http://localhost:8093. No database, account, API key or paid service is required. The server binds to loopback by default. Set `PORT` to change its port.

## Try it

1. Select **Commerce operations** and ask `How are webhook retries handled?`.
2. Inspect document identifiers, direct excerpts, matched terms and the raw JSON response.
3. Switch to **Laboratory operations** and ask the same question: the system abstains.
4. Ask about a topic absent from the collection, such as astronomy, to see the no-evidence state.

```sh
curl -X POST http://localhost:8093/api/workspaces/commerce/answers \
  -H 'Content-Type: application/json' \
  -d '{"query":"webhook retries","limit":2}'
```

## Engineering decisions

- Immutable, synthetic document collections; selection happens **before** ranking.
- Unicode normalization, stop-word filtering and deterministic token-overlap ranking.
- Evidence threshold, bounded top-k and stable identifier tie-breaking.
- Extractive responses with source IDs; no unsupported synthesis.
- Validated request contracts and RFC Problem Detail errors.
- A same-origin browser client with accessible form labels, loading/error/empty states and safe text rendering.
- Health endpoint at `/actuator/health` and CI verification on push/PR.

## Tests

`mvn verify` exercises cited results, collection separation, abstention, request validation, unknown fields/workspaces, the browser entrypoint and health endpoint. See `src/test` for the executable checks.

## AI scope — deliberately explicit

This demonstrates the **retrieval and evidence assembly foundation used in a RAG pipeline**, not a complete generative-AI product. It does **not** call an LLM, compute embeddings, use a vector database or fine-tune a model. Answers consist of retrieved excerpts. Lexical coverage is not factual confidence; related words without exact token overlap may be missed, and broad queries may match irrelevant passages. The abstention threshold is a transparent heuristic, not an evaluated safety guarantee.

A production RAG system would need authenticated access control, ingestion and chunking, semantic retrieval/reranking, model integration, citation validation, evaluation datasets, monitoring and human review appropriate to its use case. These are documented extension points, not implemented features.

The two selectable workspaces are **public fixtures, not authorization**. Anyone running this local demo can choose either. No secrets or real customer data belong in this catalog. Do not expose the demo on the internet as a production service.

## Provenance

Created as an original portfolio demonstration for Douglas Chiodi with AI assistance. Documents, rules and identifiers are fictional. It does not contain customer source, production configuration or credentials, and does not represent a historical client deployment or business outcome.

[Douglas Chiodi](https://github.com/douglaschiodi) · [LinkedIn](https://www.linkedin.com/in/douglaschiodi)
