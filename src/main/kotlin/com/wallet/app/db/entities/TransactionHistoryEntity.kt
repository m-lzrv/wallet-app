package com.wallet.app.db.entities

import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.db.entities.enums.TransactionStatus
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(
    name = "transaction_history", indexes = [Index(name = "idx_wallet_id", columnList = "walletId", unique = false)]
)
data class TransactionHistoryEntity(
    @Id val transactionId: UUID,
    @Column(updatable = false) val walletId: UUID,
    @Column(updatable = false) @Enumerated(value = EnumType.STRING) val operationType: OperationType,
    @Column(updatable = false) @Enumerated(value = EnumType.STRING) val transactionStatus: TransactionStatus,
    @Column(updatable = false) val amount: Long, // in cents
    @Column(updatable = false) val executedAt: ZonedDateTime,
    @Version var version: Int = 0
)