package com.wallet.app.common

import com.wallet.app.abstracttest.AbstractTestCase
import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.db.entities.enums.TransactionStatus
import com.wallet.app.dto.ProcessTransactionDto
import com.wallet.app.dto.RegisterWalletRequestDto
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class CommonWalletApiTestCase : AbstractTestCase() {

    @ParameterizedTest
    @MethodSource("factory")
    fun `register wallet`(playerId: String) {

        val request = mapper.writeValueAsString(RegisterWalletRequestDto(playerId))

        mockMvc.post("/wallet/") {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.result.playerId") {
                value(playerId)
            }
            jsonPath("$.result.balance") {
                value("0")
            }
            jsonPath("$.result.walletId") {
                exists()
            }
        }
    }

    @ParameterizedTest
    @MethodSource("factoryWithErrorPlayerPayload")
    fun `error case - register wallet with blank or empty playerId`(playerId: String) {

        val request = mapper.writeValueAsString(RegisterWalletRequestDto(playerId))

        mockMvc.post("/wallet/") {
            contentType = MediaType.APPLICATION_JSON
            content = request
            accept = MediaType.APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isBadRequest() }
        }
    }


    @Nested
    inner class GetBalance {

        @Test
        fun `get balance wallet`() {

            mockMvc.get("/wallet/{walletId}", WALLET_ID) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status {
                    isOk()
                }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.result.playerId") {
                    value(PLAYER_ID)
                }
                jsonPath("$.result.balance") {
                    exists()
                }
                jsonPath("$.result.walletId") {
                    value(WALLET_ID.toString())
                }
            }
        }

        @Test
        fun `get balance wallet - not found`() {

            mockMvc.get("/wallet/{walletId}", UUID.randomUUID()) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
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

            val walletId = createNewWallet()

            val request = mapper.writeValueAsString(ProcessTransactionDto(walletId, transactionId, amount))

            val operationType = OperationType.CREDIT
            mockMvc.post("/wallet/transaction/{operationType}", operationType) {
                contentType = MediaType.APPLICATION_JSON
                content = request
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status { isAccepted() }
            }

            mockMvc.get("/wallet/transactions/{walletId}", walletId) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status {
                    isOk()
                }
                content { contentType(MediaType.APPLICATION_JSON) }
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
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andExpect {
                status {
                    isOk()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun factory(): List<Arguments> = (1..100).map { Arguments.of("player-$it") }

        @JvmStatic
        fun factoryWithErrorPlayerPayload(): List<Arguments> = listOf(
            Arguments.of(""), Arguments.of(" "), Arguments.of("                 ")
        )
    }
}
