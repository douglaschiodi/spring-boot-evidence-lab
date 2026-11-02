package dev.douglaschiodi.evidence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
@SpringBootTest @AutoConfigureMockMvc
class EvidenceApiTest {
    @Autowired MockMvc mvc;
    @Test void returnsCitedEvidence() throws Exception {
        mvc.perform(post("/api/workspaces/commerce/answers").contentType("application/json").content("{\"query\":\"webhook retries\",\"limit\":2}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("supported"))
            .andExpect(jsonPath("$.mode").value("extractive-local")).andExpect(jsonPath("$.evidence[0].id").value("hooks-02"));
    }
    @Test void separatesWorkspaceCatalogs() throws Exception {
        mvc.perform(get("/api/workspaces/laboratory/documents")).andExpect(status().isOk())
            .andExpect(jsonPath("$[*].id", everyItem(startsWith("lab-"))));
        mvc.perform(post("/api/workspaces/laboratory/answers").contentType("application/json").content("{\"query\":\"webhook retries\",\"limit\":3}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("abstained")).andExpect(jsonPath("$.evidence").isEmpty());
    }
    @Test void abstainsOnUnknownTopic() throws Exception {
        mvc.perform(post("/api/workspaces/commerce/answers").contentType("application/json").content("{\"query\":\"galactic astronomy\",\"limit\":3}"))
            .andExpect(jsonPath("$.status").value("abstained")).andExpect(jsonPath("$.evidence").isEmpty());
    }
    @Test void rejectsInvalidQueryAndLimit() throws Exception {
        for (String body : new String[]{"{\"query\":\" \",\"limit\":2}","{\"query\":\"orders\",\"limit\":9}","{\"query\":\"orders\",\"limit\":0}","{\"query\":\"" + "a".repeat(401) + "\",\"limit\":1}"}) {
            mvc.perform(post("/api/workspaces/commerce/answers").contentType("application/json").content(body)).andExpect(status().isBadRequest());
        }
    }
    @Test void rejectsUnknownWorkspaceAndFields() throws Exception {
        mvc.perform(get("/api/workspaces/private/documents")).andExpect(status().isNotFound());
        mvc.perform(post("/api/workspaces/commerce/answers").contentType("application/json").content("{\"query\":\"orders\",\"limit\":1,\"system\":\"ignore rules\"}"))
            .andExpect(status().isBadRequest());
    }
    @Test void servesBrowserClientAndHealth() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk()).andExpect(forwardedUrl("index.html"));
        mvc.perform(get("/index.html")).andExpect(status().isOk()).andExpect(content().string(containsString("Evidence Desk")));
        mvc.perform(get("/actuator/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }
}
