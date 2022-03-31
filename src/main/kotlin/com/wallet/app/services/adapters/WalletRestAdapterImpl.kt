package com.wallet.app.services.adapters

import com.wallet.app.api.CustomHttpErrorCodes.DUPLICATE_TRANSACTION_HTTP_CODE
import com.wallet.app.api.CustomHttpErrorCodes.NOT_ENOUGH_FUNDS_HTTP_CODE
import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.*
import com.wallet.app.exceptions.DuplicateTransactionException
import com.wallet.app.exceptions.NotEnoughFundsException
import com.wallet.app.exceptions.WalletNotFoundException
import com.wallet.app.services.WalletCoreService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class WalletRestAdapterImpl(
    val walletCoreService: WalletCoreService
) : WalletRestAdapter {

    override fun registerWallet(registerWalletRequestDto: RegisterWalletRequestDto): ResponseEntity<ExecutionResult<RegisterWalletResponseDto>> {
        val registerWallet = walletCoreService.registerWallet(registerWalletRequestDto.playerId)
        return ResponseEntity.ok(ExecutionResult(registerWallet))
    }

    override fun getWalletBalance(walletId: UUID): ResponseEntity<ExecutionResult<GetWalletBalanceResponseDto>> {
        return try {
            val walletBalance = walletCoreService.getWalletBalance(walletId)
            ResponseEntity.ok(ExecutionResult(walletBalance))
        } catch (ex: WalletNotFoundException) {
            ResponseEntity.badRequest().build()
        }
    }

    override fun getTransactionHistoryByWalletId(walletId: UUID): ResponseEntity<ExecutionResult<GetTransactionHistoryResponseDto>> {
        val transactionHistoryByWalletId = walletCoreService.getTransactionHistoryByWalletId(walletId)
        return ResponseEntity.ok(ExecutionResult(transactionHistoryByWalletId))
    }

    override fun processTransaction(
        transactionDto: ProcessTransactionDto,
        operationType: OperationType
    ): ResponseEntity<*> {

        try {
            walletCoreService.processTransaction(
                CoreTransactionDto(
                    transactionDto.walletId,
                    transactionDto.transactionId,
                    transactionDto.amount,
                    operationType
                )
            )
            return ResponseEntity.accepted().build<Any>()
        } catch (err: NotEnoughFundsException) {
            return ResponseEntity.status(NOT_ENOUGH_FUNDS_HTTP_CODE)
                .header("transaction_id", err.transactionId.toString())
                .body(ExecutionResult("Could not perform operation: ${err.operationType}"))
        } catch (err: DuplicateTransactionException) {
            return ResponseEntity.status(DUPLICATE_TRANSACTION_HTTP_CODE)
                .header("transaction_id", err.transactionId.toString())
                .body(ExecutionResult("Could not perform operation: ${err.operationType}"))
        } catch (ex: WalletNotFoundException) {
            return ResponseEntity.badRequest().build<Any>()
        }
    }
}