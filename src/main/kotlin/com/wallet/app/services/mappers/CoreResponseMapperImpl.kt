package com.wallet.app.services.mappers

import com.wallet.app.db.entities.TransactionHistoryEntity
import com.wallet.app.db.entities.WalletEntity
import com.wallet.app.dto.GetTransactionHistoryResponseDto
import com.wallet.app.dto.GetWalletBalanceResponseDto
import com.wallet.app.dto.RegisterWalletResponseDto
import com.wallet.app.dto.TransactionHistoryDto
import org.springframework.stereotype.Component
import java.util.*

@Component
class CoreResponseMapperImpl : CoreResponseMapper {

    override fun mapRegisterResponse(walletEntity: WalletEntity): RegisterWalletResponseDto {
        return RegisterWalletResponseDto(walletEntity.walletId, walletEntity.playerId, walletEntity.balance)
    }

    override fun mapWalletBalanceResponse(walletEntity: WalletEntity): GetWalletBalanceResponseDto {
        return GetWalletBalanceResponseDto(
            walletId = walletEntity.walletId,
            playerId = walletEntity.playerId,
            balance = walletEntity.balance,
            createdAt = walletEntity.createdAt
        )
    }

    override fun mapHistoriesToResponse(
        walletId: UUID, historyEntityList: List<TransactionHistoryEntity>
    ): GetTransactionHistoryResponseDto {
        return GetTransactionHistoryResponseDto(
            walletId = walletId,
            transactions = historyEntityList.map {
                TransactionHistoryDto(
                    transactionId = it.transactionId,
                    operationType = it.operationType,
                    transactionStatus = it.transactionStatus,
                    amount = it.amount,
                    executedAt = it.executedAt
                )
            }.sortedBy { it.executedAt }
        )
    }
}