package com.wallet.app.validation

import com.wallet.app.dto.CoreTransactionDto
import com.wallet.app.exceptions.WalletCoreValidationException

interface WalletCoreValidator {

    @Throws(WalletCoreValidationException::class)
    fun validateRegisterWallet(playerId: String)

    @Throws(WalletCoreValidationException::class)
    fun validateProcessTransaction(transaction: CoreTransactionDto)


}
