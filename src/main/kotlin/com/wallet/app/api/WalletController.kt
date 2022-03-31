package com.wallet.app.api

import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.dto.ExecutionResult
import com.wallet.app.dto.ProcessTransactionDto
import com.wallet.app.dto.RegisterWalletRequestDto
import com.wallet.app.dto.RegisterWalletResponseDto
import com.wallet.app.services.ProcessingAdapter
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.info.Info
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@OpenAPIDefinition(
    info = Info(
        title = "Wallet API",
        version = "0.1",
        description = "A simple wallet microservice that manages credit/debit transactions on behalf of players."
    )
)
@RestController
@RequestMapping("/wallet")
class WalletController(
    val processingAdapter: ProcessingAdapter
) {

    @Operation(summary = "Create your own wallet")
    @PostMapping
    fun registerWallet(@RequestBody registerWalletRequestDto: RegisterWalletRequestDto): ResponseEntity<ExecutionResult<RegisterWalletResponseDto>> {
        log.info { "registerWallet: $registerWalletRequestDto" }
        return processingAdapter.registerWallet(registerWalletRequestDto)
    }

    @Operation(summary = "See your balance by walletId")
    @GetMapping("/{walletId}")
    fun getWalletBalance(@PathVariable walletId: UUID) = processingAdapter.getWalletBalance(walletId)

    @Operation(summary = "Watch your transactions history by walletId")
    @GetMapping("/transactions/{walletId}")
    fun getTransactionsHistoryByWalletId(@PathVariable walletId: UUID) =
        processingAdapter.getTransactionHistoryByWalletId(walletId)

    @Operation(summary = "Process deposit/credit transaction")
    @PostMapping("transaction/{operationType}")
    fun processTransaction(
        @PathVariable operationType: OperationType,
        @RequestBody transactionDto: ProcessTransactionDto
    ) = processingAdapter.processTransaction(transactionDto, operationType)

    companion object {
        private val log = KotlinLogging.logger {}
    }

}