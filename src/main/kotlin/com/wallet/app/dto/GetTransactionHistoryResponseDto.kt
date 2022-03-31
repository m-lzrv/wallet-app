package com.wallet.app.dto

import java.util.*

data class GetTransactionHistoryResponseDto(
    val walletId: UUID,
    val transactions: List<TransactionHistoryDto>
)