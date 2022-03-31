package com.wallet.app.dto

import java.time.ZonedDateTime
import java.util.*

data class GetWalletBalanceResponseDto(
    val walletId: UUID,
    val balance: Long,
    var createdAt: ZonedDateTime
)