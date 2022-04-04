package com.wallet.app.transactions

import com.wallet.app.abstracttest.AbstractTestCase
import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.ProcessTransactionDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.post
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class CommonProcessTransactionTestCases : AbstractTestCase() {


    @Test
    fun `error case - process transaction - wallet not found`() {
        val wrongWalletId: UUID = UUID.randomUUID()
        val transactionId: UUID = UUID.randomUUID()
        val amount: Long = 10000

        val request = mapper.writeValueAsString(ProcessTransactionDto(wrongWalletId, transactionId, amount))
        val opType = OperationType.CREDIT.toString()

        mockMvc.post("/wallet/transaction/{operationType}", opType) {
            contentType = APPLICATION_JSON
            content = request
            accept = APPLICATION_JSON
        }.andExpect {
            status {
                isBadRequest()
            }
        }
    }

}
