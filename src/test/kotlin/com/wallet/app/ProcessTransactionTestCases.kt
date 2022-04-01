package com.wallet.app

import com.fasterxml.jackson.module.kotlin.readValue
import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.ExecutionResult
import com.wallet.app.dto.ProcessTransactionDto
import com.wallet.app.dto.RegisterWalletRequestDto
import com.wallet.app.dto.RegisterWalletResponseDto
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
class ProcessTransactionTestCases : AbstractTestCase() {


    @Test
    fun `wallet not found`() {
        val wrongWalletId: UUID = UUID.randomUUID()
        val transactionId: UUID = UUID.randomUUID()
        val amount: Long = 100

        val request = mapper.writeValueAsString(ProcessTransactionDto(wrongWalletId, transactionId, amount))
        val opType = OperationType.CREDIT.toString()

        mockMvc.post("/wallet/transaction/{operationType}", opType) {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `process CREDIT transaction`() {

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
    fun `process DEBIT transaction`() {

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
            status { isAccepted() }

        }
    }

    @Test
    fun `process DEBIT transaction with zero balance`() {

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
            status { isAccepted() }
        }
    }


    companion object {

    }
}
