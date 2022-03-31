package com.wallet.app.dto

import com.wallet.app.db.entities.enums.OperationType
import java.util.*

data class CoreTransactionDto(
    val walletId: UUID,
    val transactionId: UUID,
    val amount: Long,
    val operationType: OperationType
)