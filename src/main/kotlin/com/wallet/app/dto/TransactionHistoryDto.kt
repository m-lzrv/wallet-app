package com.wallet.app.dto

import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.db.entities.enums.TransactionStatus
import java.time.ZonedDateTime
import java.util.UUID

data class TransactionHistoryDto(
    var walletId: UUID,
    var transactionId: UUID,
    var operationType: OperationType,
    var transactionStatus: TransactionStatus,
    var amount: Long,
    val executedAt: ZonedDateTime
)