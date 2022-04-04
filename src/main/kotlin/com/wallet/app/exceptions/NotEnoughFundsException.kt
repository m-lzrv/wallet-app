package com.wallet.app.exceptions

import com.wallet.app.db.entities.enums.OperationType
import java.util.*

class NotEnoughFundsException(
    val transactionId: UUID,
    val operationType: OperationType
) : RuntimeException() {
    override fun toString(): String {
        return "NotEnoughFundsException(transactionId=$transactionId, operationType=$operationType)"
    }
}
