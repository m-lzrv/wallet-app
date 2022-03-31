package com.wallet.app.db.entities

import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "wallet")
data class WalletEntity(
    @Id
    val walletId: UUID,
    @Column val playerId: String,
    @Column var balance: Long,// in cents
    @Column var createdAt: ZonedDateTime,
    @Version var version: Int = 0
)