# Laboratório de evidências com Spring Boot

Demonstração autoral de backend Java com interface web para buscar documentos e apresentar trechos com fontes verificáveis.

## Executar

Use JDK 17+ e Maven 3.6.3+. Confira se `JAVA_HOME` aponta para o JDK correto.

```sh
mvn verify
mvn spring-boot:run
```

Abra http://localhost:8093. Não requer banco externo, conta, chave de API ou serviço pago.

## O que avaliar

- API REST em Java/Spring Boot, contratos validados e erros padronizados.
- Busca por termos normalizados, ranking determinístico e limite de evidências.
- Respostas extrativas com identificadores de fontes e abstenção sem evidência suficiente.
- Interface responsiva com estados de carregamento, falha e ausência de resultados.
- Testes de integração HTTP simulada com MockMvc e pipeline de CI.

## Limites

É a base de recuperação e montagem de evidências de um fluxo RAG. **Não usa LLM, embeddings nem banco vetorial**; não produz síntese generativa. A cobertura de termos não é uma medida de confiança factual. A busca lexical pode perder sinônimos ou retornar documentos pouco relevantes. O limiar de abstenção é heurístico.

As coleções são exemplos públicos selecionáveis, não controle de acesso. A aplicação fica em loopback por padrão. Uma versão produtiva exigiria autenticação, autorização, ingestão, avaliação de relevância, integração do modelo e observabilidade apropriada.

## Proveniência

Criado para o portfólio de Douglas Chiodi com assistência de IA. Todos os documentos e exemplos são fictícios. Não contém dados de clientes, segredos ou configurações de produção e não representa implantação comercial anterior.

[Detalhes em inglês](README.md) · [Arquitetura](ARCHITECTURE.md)
