package com.wallet.app.db.repositories

import com.wallet.app.db.entities.WalletEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface WalletRepository : JpaRepository<WalletEntity, UUID> {

}