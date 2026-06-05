package edu.ucsb.cs.scaffold;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("development")
class FrontendProxyControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rootPathProxiesToFrontendInDevelopment() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Failed to connect to the frontend server")));
    }

    @Test
    void nonApiPathProxiesToFrontendInDevelopment() throws Exception {
        mockMvc.perform(get("/some/client/route"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Failed to connect to the frontend server")));
    }

    @Test
    void apiPathIsNotProxied() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void swaggerUiPathIsNotProxied() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Swagger UI")));
    }

    @Test
    void h2ConsolePathIsNotProxied() throws Exception {
        mockMvc.perform(get("/h2-console"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Failed to connect to the frontend server"))));
    }

    @Test
    void oauth2PathIsNotProxied() throws Exception {
        mockMvc.perform(get("/oauth2/authorization/google"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Failed to connect to the frontend server"))));
    }
}
