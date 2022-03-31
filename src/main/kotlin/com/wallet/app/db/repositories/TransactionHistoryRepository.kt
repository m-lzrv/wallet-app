package com.wallet.app.db.repositories

import com.wallet.app.db.entities.TransactionHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionHistoryRepository : JpaRepository<TransactionHistoryEntity, UUID> {

    fun findAllByWalletId(walletId: UUID): List<TransactionHistoryEntity>
}