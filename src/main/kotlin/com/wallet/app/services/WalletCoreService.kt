package com.wallet.app.services

import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.CoreTransactionDto
import com.wallet.app.dto.GetWalletBalanceResponseDto
import com.wallet.app.dto.RegisterWalletResponseDto
import com.wallet.app.dto.TransactionHistoryDto
import com.wallet.app.exceptions.DuplicateTransactionException
import com.wallet.app.exceptions.NotEnoughFundsException
import com.wallet.app.exceptions.WalletNotFoundException
import java.util.*

interface WalletCoreService {


    fun registerWallet(playerId: String): RegisterWalletResponseDto

    @Throws(WalletNotFoundException::class)
    fun getWalletBalance(walletId: UUID): GetWalletBalanceResponseDto

    fun getTransactionHistoryByWalletId(walletId: UUID): List<TransactionHistoryDto>

    @Throws(
        NotEnoughFundsException::class,
        DuplicateTransactionException::class,
        WalletNotFoundException::class
    )
    fun processTransaction(transaction: CoreTransactionDto)


}