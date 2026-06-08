package com.hmeclazcke.simplewebservicetest;

import com.hmeclazcke.simplewebservicetest.entity.Account;
import com.hmeclazcke.simplewebservicetest.entity.Transfer;
import com.hmeclazcke.simplewebservicetest.entity.TransferStatus;
import com.hmeclazcke.simplewebservicetest.repository.AccountRepository;
import com.hmeclazcke.simplewebservicetest.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class SimpleWebservicePostgresIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransferRepository transferRepository;

    @BeforeEach
    void cleanDatabase() {
        transferRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configurePostgres(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.h2.console.enabled", () -> "false");
    }

    @Test
    void createsAccount() throws Exception {
        mockMvc.perform(post("/api/account")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "holder": "Silvia",
                                  "balance": 1000.00
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.holder").value("Silvia"))
                .andExpect(jsonPath("$.balance").value(comparesEqualTo(1000.00)));

        assertThat(accountRepository.findAll()).hasSize(1);
    }

    @Test
    void completesTransferAndUpdatesBalances() throws Exception {
        Account origin = accountRepository.save(new Account("Silvia", new BigDecimal("1000.00")));
        Account destination = accountRepository.save(new Account("John", new BigDecimal("500.00")));

        mockMvc.perform(post("/api/transfer")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "originId": %d,
                                  "destinationId": %d,
                                  "amount": 100.00
                                }
                                """.formatted(origin.getId(), destination.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originAccountId").value(origin.getId()))
                .andExpect(jsonPath("$.destinationAccountId").value(destination.getId()))
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        Account updatedOrigin = accountRepository.findById(origin.getId()).orElseThrow();
        Account updatedDestination = accountRepository.findById(destination.getId()).orElseThrow();

        assertThat(updatedOrigin.getBalance()).isEqualByComparingTo("900.00");
        assertThat(updatedDestination.getBalance()).isEqualByComparingTo("600.00");
        assertThat(transferRepository.findAll())
                .extracting(Transfer::getStatus)
                .containsExactly(TransferStatus.COMPLETED);
    }

    @Test
    void marksTransferAsFailedWhenFundsAreInsufficient() throws Exception {
        Account origin = accountRepository.save(new Account("Silvia", new BigDecimal("1000.00")));
        Account destination = accountRepository.save(new Account("John", new BigDecimal("500.00")));

        mockMvc.perform(post("/api/transfer")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "originId": %d,
                                  "destinationId": %d,
                                  "amount": 10000.00
                                }
                                """.formatted(origin.getId(), destination.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INSUFFICIENT_FUNDS"))
                .andExpect(jsonPath("$.details.accountId").value(origin.getId()));

        Account updatedOrigin = accountRepository.findById(origin.getId()).orElseThrow();
        Account updatedDestination = accountRepository.findById(destination.getId()).orElseThrow();

        assertThat(updatedOrigin.getBalance()).isEqualByComparingTo("1000.00");
        assertThat(updatedDestination.getBalance()).isEqualByComparingTo("500.00");
        assertThat(transferRepository.findAll())
                .extracting(Transfer::getStatus)
                .containsExactly(TransferStatus.FAILED);
    }
}
