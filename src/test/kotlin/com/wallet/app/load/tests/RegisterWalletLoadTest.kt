package com.wallet.app.load.tests

import com.wallet.app.AbstractTestCase
import com.wallet.app.dto.RegisterWalletRequestDto
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class RegisterWalletLoadTest : AbstractTestCase() {


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
            jsonPath("$.result.playerId"){
                value(playerId)
            }
            jsonPath("$.result.balance"){
                value("0")
            }
            jsonPath("$.result.walletId"){
                exists()
            }
        }
    }


    companion object {
        @JvmStatic
        fun factory(): List<Arguments> = (1..100).map { Arguments.of("player-$it") }
    }
}
