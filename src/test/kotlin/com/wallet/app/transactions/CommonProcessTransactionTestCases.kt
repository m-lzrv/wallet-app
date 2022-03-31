package com.wallet.app.transactions

import com.wallet.app.abstracttest.AbstractTestCase
import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.db.entities.enums.TransactionStatus
import com.wallet.app.dto.ProcessTransactionDto
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.get
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

    @Nested
    inner class GetBalance {

        @Test
        fun `get balance wallet`() {

            mockMvc.get("/wallet/{walletId}", WALLET_ID) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status {
                    isOk()
                }
                //
            }
        }

        @Test
        fun `get balance wallet - not found`() {

            mockMvc.get("/wallet/{walletId}", UUID.randomUUID()) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status {
                    isBadRequest()
                }
            }
        }
    }

    @Nested
    inner class GetTransactionHistory {

        @Test
        fun `get transaction history`() {
            val transactionId: UUID = UUID.randomUUID()
            val amount: Long = 10000

            val walletId = createWallet()

            val request = mapper.writeValueAsString(ProcessTransactionDto(walletId, transactionId, amount))

            val operationType = OperationType.CREDIT
            mockMvc.post("/wallet/transaction/{operationType}", operationType) {
                contentType = APPLICATION_JSON
                content = request
                accept = APPLICATION_JSON
            }.andExpect {
                status { isAccepted() }
            }

            mockMvc.get("/wallet/transactions/{walletId}", walletId) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status {
                    isOk()
                }
                content { contentType(APPLICATION_JSON) }
                jsonPath("$.result.walletId") {
                    value(walletId.toString())
                }
                jsonPath("$.result.transactions") {
                    exists()
                }
                jsonPath("$.result.transactions[0].transactionStatus") {
                    value(TransactionStatus.COMPLETED.toString())
                }
                jsonPath("$.result.transactions[0].amount") {
                    value(amount)
                }
                jsonPath("$.result.transactions[0].transactionId") {
                    value(transactionId.toString())
                }
                jsonPath("$.result.transactions[0].operationType") {
                    value(operationType.toString())
                }
            }
        }

        @Test
        fun `get transaction history - not found`() {
            val wrongWalletId = UUID.randomUUID()
            mockMvc.get("/wallet/transactions/{walletId}", wrongWalletId) {
                contentType = APPLICATION_JSON
                accept = APPLICATION_JSON
            }.andExpect {
                status {
                    isOk()
                }
            }
        }
    }
}
