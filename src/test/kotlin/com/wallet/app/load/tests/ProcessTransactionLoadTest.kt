package com.wallet.app.load.tests

import com.wallet.app.AbstractTestCase
import com.wallet.app.db.entities.WalletEntity
import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.ProcessTransactionDto
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import org.springframework.transaction.support.TransactionTemplate
import java.time.ZonedDateTime
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class ProcessTransactionLoadTest : AbstractTestCase() {

    @BeforeAll
    fun precond(){
        createWallet()
    }

    @ParameterizedTest
    @MethodSource("factory")
    fun `process credit transactions`(transactionId: UUID, amount: Long, operationType: OperationType) {


        val request = mapper.writeValueAsString(ProcessTransactionDto(WALLET_ID, transactionId, amount))

        mockMvc.post("/wallet/transaction/{operationType}", operationType) {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isAccepted() }
        }
    }


    companion object {

        @JvmStatic
        fun factory(): List<Arguments> {
            return (1..1000).map {
                Arguments.of(UUID.randomUUID(), it * 1000, OperationType.CREDIT)
            }
        }
    }
}
