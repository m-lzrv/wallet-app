package com.wallet.app.validation

import com.wallet.app.dto.CoreTransactionDto
import com.wallet.app.exceptions.WalletCoreValidationException
import org.springframework.stereotype.Component

@Component
class WalletCoreValidatorImpl : WalletCoreValidator {
    override fun validateRegisterWallet(playerId: String) {
        if (playerId.isBlank()) throw WalletCoreValidationException("Field: [playerId] is empty or blank!")
    }

    override fun validateProcessTransaction(transaction: CoreTransactionDto) {
        if (transaction.amount <= 0) throw WalletCoreValidationException("Field: [transaction.amount] is negative or 0!")
    }
}