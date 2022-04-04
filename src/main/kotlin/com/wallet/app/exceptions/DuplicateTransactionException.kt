package com.wallet.app.exceptions

import com.wallet.app.db.entities.enums.OperationType
import java.util.*

class DuplicateTransactionException(
    val transactionId: UUID,
    val operationType: OperationType
) : RuntimeException() {
    override fun toString(): String {
        return "DuplicateTransactionException(transactionId=$transactionId, operationType=$operationType)"
    }
}
