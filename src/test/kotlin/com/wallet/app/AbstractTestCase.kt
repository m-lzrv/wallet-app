package com.wallet.app

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wallet.app.db.entities.WalletEntity
import com.wallet.app.db.repositories.TransactionHistoryRepository
import com.wallet.app.db.repositories.WalletRepository
import com.wallet.app.dto.ExecutionResult
import com.wallet.app.dto.RegisterWalletRequestDto
import com.wallet.app.dto.RegisterWalletResponseDto
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.support.TransactionTemplate
import java.time.ZonedDateTime
import java.util.*

abstract class AbstractTestCase {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var walletRepository: WalletRepository

    @Autowired
    lateinit var transactionHistoryRepository: TransactionHistoryRepository


    @Autowired
    lateinit var transaction: TransactionTemplate
//
//    @BeforeAll
//    fun preConditions() {
//        transaction.execute {
//            val walletEntity = WalletEntity(WALLET_ID, USER_1, 100, ZonedDateTime.now())
//            walletRepository.save(walletEntity)
//        }
//    }

    @AfterAll
    fun releaseDevices() {
        transaction.execute {
            walletRepository.deleteAll()
            transactionHistoryRepository.deleteAll()
        }
    }

    fun createWallet(): UUID {
        val request2 = mapper.writeValueAsString(RegisterWalletRequestDto(USER_1))

        val response = mockMvc.post("/wallet/") {
            contentType = MediaType.APPLICATION_JSON
            content = request2
            accept = MediaType.APPLICATION_JSON
        }.andReturn().response.contentAsString

        val registerWalletResponseDto = mapper.readValue<ExecutionResult<RegisterWalletResponseDto>>(response)

        val walletId = registerWalletResponseDto.result.walletId
        return walletId
    }

    companion object {
        const val USER_1 = "user-1"
        val WALLET_ID: UUID = UUID.fromString("b1a3d12d-ded8-4dbb-882a-4dbf93604888")!!
    }
}
