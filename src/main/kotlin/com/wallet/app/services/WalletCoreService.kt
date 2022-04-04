package com.wallet.app.services

import com.wallet.app.dto.CoreTransactionDto
import com.wallet.app.dto.GetTransactionHistoryResponseDto
import com.wallet.app.dto.GetWalletBalanceResponseDto
import com.wallet.app.dto.RegisterWalletResponseDto
import com.wallet.app.exceptions.DuplicateTransactionException
import com.wallet.app.exceptions.NotEnoughFundsException
import com.wallet.app.exceptions.WalletCoreValidationException
import com.wallet.app.exceptions.WalletNotFoundException
import java.util.*

interface WalletCoreService {

    @Throws(WalletCoreValidationException::class)
    fun registerWallet(playerId: String): RegisterWalletResponseDto

    @Throws(WalletNotFoundException::class)
    fun getWalletBalance(walletId: UUID): GetWalletBalanceResponseDto

    fun getTransactionHistoryByWalletId(walletId: UUID): GetTransactionHistoryResponseDto

    @Throws(
        NotEnoughFundsException::class,
        DuplicateTransactionException::class,
        WalletNotFoundException::class,
        WalletCoreValidationException::class
    )
    fun processTransaction(transaction: CoreTransactionDto)


}