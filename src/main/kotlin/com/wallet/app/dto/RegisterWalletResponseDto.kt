package com.wallet.app.dto

import java.util.*

data class RegisterWalletResponseDto(
    val walletId: UUID,
    val playerId: String,
    val balance: Long
)