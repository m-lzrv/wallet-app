package com.wallet.app.services.adapters

import com.wallet.app.api.CustomHttpErrorCodes.DUPLICATE_TRANSACTION_HTTP_CODE
import com.wallet.app.api.CustomHttpErrorCodes.NOT_ENOUGH_FUNDS_HTTP_CODE
import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.*
import com.wallet.app.exceptions.DuplicateTransactionException
import com.wallet.app.exceptions.NotEnoughFundsException
import com.wallet.app.exceptions.WalletCoreValidationException
import com.wallet.app.exceptions.WalletNotFoundException
import com.wallet.app.services.WalletCoreService
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class WalletRestAdapterImpl(
    val walletCoreService: WalletCoreService
) : WalletRestAdapter {

    override fun registerWallet(registerWalletRequestDto: RegisterWalletRequestDto): ResponseEntity<ExecutionResult<RegisterWalletResponseDto>> {
        return try {
            val registerWallet = walletCoreService.registerWallet(registerWalletRequestDto.playerId)
            ResponseEntity.ok(ExecutionResult(registerWallet))
        } catch (e: WalletCoreValidationException) {
            log.error("Error occurred while validation: ${e.message}")
            ResponseEntity.badRequest().build()
        }
    }

    override fun getWalletBalance(walletId: UUID): ResponseEntity<ExecutionResult<GetWalletBalanceResponseDto>> {
        return try {
            val walletBalance = walletCoreService.getWalletBalance(walletId)
            ResponseEntity.ok(ExecutionResult(walletBalance))
        } catch (e: WalletNotFoundException) {
            log.error("Error occurred: ${e.message}")
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
            val transaction = CoreTransactionDto(
                transactionDto.walletId,
                transactionDto.transactionId,
                transactionDto.amount,
                operationType
            )
            walletCoreService.processTransaction(transaction)
            return ResponseEntity.accepted().build<Any>()
        } catch (e: NotEnoughFundsException) {
            log.error("Error occurred: $e")
            return ResponseEntity.status(NOT_ENOUGH_FUNDS_HTTP_CODE)
                .header("transaction_id", e.transactionId.toString())
                .body(ExecutionResult("Could not perform operation: ${e.operationType}"))
        } catch (e: DuplicateTransactionException) {
            log.error("Error occurred: $e")
            return ResponseEntity.status(DUPLICATE_TRANSACTION_HTTP_CODE)
                .header("transaction_id", e.transactionId.toString())
                .body(ExecutionResult("Could not perform operation: ${e.operationType}"))
        } catch (e: WalletNotFoundException) {
            log.error("Error occurred: $e")
            return ResponseEntity.badRequest().build<Any>()
        } catch (e: WalletCoreValidationException) {
            log.error("Error occurred while validation: ${e.message}")
            return ResponseEntity.badRequest().body(e.message)
        }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}