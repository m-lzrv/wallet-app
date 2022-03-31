package com.wallet.app.exceptions

import com.wallet.app.db.entities.enums.OperationType
import java.util.*

class NotEnoughFundsException(
    val transactionId: UUID,
    val operationType: OperationType
) : RuntimeException()
