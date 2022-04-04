package com.wallet.app.dto

import java.time.ZonedDateTime
import java.util.*

data class GetWalletBalanceResponseDto(
    val walletId: UUID,
    val playerId: String,
    val balance: Long,
    val createdAt: ZonedDateTime
)