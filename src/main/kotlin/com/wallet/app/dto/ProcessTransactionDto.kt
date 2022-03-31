package com.wallet.app.dto

import java.util.*

data class ProcessTransactionDto(
    var walletId: UUID,
    var transactionId: UUID,
    var amount: Long,
)