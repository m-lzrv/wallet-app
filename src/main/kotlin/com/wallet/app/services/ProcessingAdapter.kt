package com.wallet.app.services

import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.*
import org.springframework.http.ResponseEntity
import java.util.*

interface ProcessingAdapter {

    fun registerWallet(registerWalletRequestDto: RegisterWalletRequestDto): ResponseEntity<ExecutionResult<RegisterWalletResponseDto>>

    fun getWalletBalance(walletId: UUID): ResponseEntity<ExecutionResult<GetWalletBalanceResponseDto>>

    fun getTransactionHistoryByWalletId(walletId: UUID): ResponseEntity<ExecutionResult<List<TransactionHistoryDto>>>

    fun processTransaction(transactionDto: ProcessTransactionDto, operationType: OperationType): ResponseEntity<*>


}
