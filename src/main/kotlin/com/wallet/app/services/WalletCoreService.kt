package com.wallet.app.services

import com.wallet.app.dto.*
import com.wallet.app.exceptions.DuplicateTransactionException
import com.wallet.app.exceptions.NotEnoughFundsException
import com.wallet.app.exceptions.WalletNotFoundException
import java.util.*

interface WalletCoreService {


    fun registerWallet(playerId: String): RegisterWalletResponseDto

    @Throws(WalletNotFoundException::class)
    fun getWalletBalance(walletId: UUID): GetWalletBalanceResponseDto

    fun getTransactionHistoryByWalletId(walletId: UUID): GetTransactionHistoryResponseDto

    @Throws(
        NotEnoughFundsException::class,
        DuplicateTransactionException::class,
        WalletNotFoundException::class
    )
    fun processTransaction(transaction: CoreTransactionDto)


}