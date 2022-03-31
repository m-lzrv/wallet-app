package com.wallet.app.transactions

import com.wallet.app.abstracttest.AbstractTestCase
import com.wallet.app.api.CustomHttpErrorCodes.DUPLICATE_TRANSACTION_HTTP_CODE
import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.ProcessTransactionDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class ProcessCreditTransactionTestCases : AbstractTestCase() {


    @Test
    fun `regular case - process CREDIT transaction`() {

        val transactionId: UUID = UUID.randomUUID()
        val amount: Long = 10000

        val walletId = createWallet()

        val request = mapper.writeValueAsString(ProcessTransactionDto(walletId, transactionId, amount))

        mockMvc.post("/wallet/transaction/{operationType}", OperationType.CREDIT) {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isAccepted() }
        }
    }

    @Test
    fun `error case - process CREDIT transaction with the same transaction_id field`() {

        val transactionId: UUID = UUID.randomUUID()
        val amount: Long = 10000

        val request = mapper.writeValueAsString(ProcessTransactionDto(WALLET_ID, transactionId, amount))

        mockMvc.post("/wallet/transaction/{operationType}", OperationType.CREDIT) {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status {
                isAccepted()
            }
        }

        mockMvc.post("/wallet/transaction/{operationType}", OperationType.CREDIT) {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status {
                isEqualTo(DUPLICATE_TRANSACTION_HTTP_CODE)
            }
        }
    }

}
