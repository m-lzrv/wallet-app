package com.wallet.app.services

import com.wallet.app.db.entities.TransactionHistoryEntity
import com.wallet.app.db.entities.WalletEntity
import com.wallet.app.dto.GetWalletBalanceResponseDto
import com.wallet.app.dto.RegisterWalletResponseDto
import com.wallet.app.dto.TransactionHistoryDto

interface CoreResponseMapper {

    fun mapRegisterResponse(walletEntity: WalletEntity): RegisterWalletResponseDto

    fun mapWalletBalanceResponse(walletEntity: WalletEntity): GetWalletBalanceResponseDto
    fun mapHistoriesToResponse(historyEntityList: List<TransactionHistoryEntity>): List<TransactionHistoryDto>

}
