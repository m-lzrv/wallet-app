package com.wallet.app.services

import com.wallet.app.db.entities.TransactionHistoryEntity
import com.wallet.app.db.entities.WalletEntity
import com.wallet.app.dto.GetWalletBalanceResponseDto
import com.wallet.app.dto.RegisterWalletResponseDto
import com.wallet.app.dto.TransactionHistoryDto
import org.springframework.stereotype.Component

@Component
class CoreResponseMapperImpl : CoreResponseMapper {

    override fun mapRegisterResponse(walletEntity: WalletEntity): RegisterWalletResponseDto {
        return RegisterWalletResponseDto(walletEntity.walletId, walletEntity.playerId, walletEntity.balance)
    }

    override fun mapWalletBalanceResponse(walletEntity: WalletEntity): GetWalletBalanceResponseDto {
        return GetWalletBalanceResponseDto(
            walletId = walletEntity.walletId, balance = walletEntity.balance, createdAt = walletEntity.createdAt
        )
    }

    override fun mapHistoriesToResponse(historyEntityList: List<TransactionHistoryEntity>): List<TransactionHistoryDto> {
        return historyEntityList.map {
            TransactionHistoryDto(
                walletId = it.walletId,
                transactionId = it.transactionId,
                operationType = it.operationType,
                transactionStatus = it.transactionStatus,
                amount = it.amount,
                executedAt = it.executedAt
            )
        }.sortedBy { it.executedAt }
    }
}