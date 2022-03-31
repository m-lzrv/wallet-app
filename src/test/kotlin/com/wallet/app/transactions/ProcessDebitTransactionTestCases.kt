package com.wallet.app.transactions

import com.wallet.app.abstracttest.AbstractTestCase
import com.wallet.app.api.CustomHttpErrorCodes.DUPLICATE_TRANSACTION_HTTP_CODE
import com.wallet.app.api.CustomHttpErrorCodes.NOT_ENOUGH_FUNDS_HTTP_CODE
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
class ProcessDebitTransactionTestCases : AbstractTestCase() {

    @Test
    fun `regular case - process DEBIT transaction`() {

        val creditTransactionId: UUID = UUID.randomUUID()
        val amount: Long = 10000

        val creditRequest = mapper.writeValueAsString(ProcessTransactionDto(WALLET_ID, creditTransactionId, amount))

        mockMvc.post("/wallet/transaction/{operationType}", OperationType.CREDIT) {
            contentType = MediaType.APPLICATION_JSON
            content = creditRequest
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status {
                isAccepted()
            }
        }

        val debitTransactionId: UUID = UUID.randomUUID()

        val request = mapper.writeValueAsString(ProcessTransactionDto(WALLET_ID, debitTransactionId, amount))

        mockMvc.post("/wallet/transaction/{operationType}", OperationType.DEBIT) {
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
    }


    @Test
    fun `error case - process DEBIT transaction with zero balance`() {

        val transactionId: UUID = UUID.randomUUID()
        val amount: Long = 10000

        val walletId = createWallet()

        val request = mapper.writeValueAsString(ProcessTransactionDto(walletId, transactionId, amount))

        mockMvc.post("/wallet/transaction/{operationType}", OperationType.DEBIT) {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status {
                isEqualTo(NOT_ENOUGH_FUNDS_HTTP_CODE)
            }
        }
    }

    @Test
    fun `error case - process DEBIT transaction with NO_FUNDS exception`() {

        val creditTransactionId: UUID = UUID.randomUUID()
        val amount: Long = 10000

        val creditRequest = mapper.writeValueAsString(ProcessTransactionDto(WALLET_ID, creditTransactionId, amount))

        mockMvc.post("/wallet/transaction/{operationType}", OperationType.CREDIT) {
            contentType = MediaType.APPLICATION_JSON
            content = creditRequest
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status {
                isAccepted()
            }
        }

        val debitTransactionId: UUID = UUID.randomUUID()


        val amountGreaterThanHas = amount * 2

        val request = mapper.writeValueAsString(
            ProcessTransactionDto(
                WALLET_ID, debitTransactionId, amountGreaterThanHas
            )
        )

        mockMvc.post("/wallet/transaction/{operationType}", OperationType.DEBIT) {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status {
                isEqualTo(NOT_ENOUGH_FUNDS_HTTP_CODE)
            }
        }
    }


    @Test
    fun `error case - process CREDIT then DEBIT transactions with the same transaction_id field`() {

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


        mockMvc.post("/wallet/transaction/{operationType}", OperationType.DEBIT) {
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
