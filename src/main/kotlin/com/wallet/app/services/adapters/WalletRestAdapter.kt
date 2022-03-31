package com.wallet.app.services.adapters

import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.*
import org.springframework.http.ResponseEntity
import java.util.*

interface WalletRestAdapter {

    fun registerWallet(registerWalletRequestDto: RegisterWalletRequestDto): ResponseEntity<ExecutionResult<RegisterWalletResponseDto>>

    fun getWalletBalance(walletId: UUID): ResponseEntity<ExecutionResult<GetWalletBalanceResponseDto>>

    fun getTransactionHistoryByWalletId(walletId: UUID): ResponseEntity<ExecutionResult<GetTransactionHistoryResponseDto>>

    fun processTransaction(transactionDto: ProcessTransactionDto, operationType: OperationType): ResponseEntity<*>


}
