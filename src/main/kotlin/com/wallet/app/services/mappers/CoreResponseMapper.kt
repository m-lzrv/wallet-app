package com.wallet.app.services.mappers

import com.wallet.app.db.entities.TransactionHistoryEntity
import com.wallet.app.db.entities.WalletEntity
import com.wallet.app.dto.GetTransactionHistoryResponseDto
import com.wallet.app.dto.GetWalletBalanceResponseDto
import com.wallet.app.dto.RegisterWalletResponseDto
import java.util.*

interface CoreResponseMapper {

    fun mapRegisterResponse(walletEntity: WalletEntity): RegisterWalletResponseDto

    fun mapWalletBalanceResponse(walletEntity: WalletEntity): GetWalletBalanceResponseDto
    fun mapHistoriesToResponse(walletId: UUID, historyEntityList: List<TransactionHistoryEntity>): GetTransactionHistoryResponseDto

}
