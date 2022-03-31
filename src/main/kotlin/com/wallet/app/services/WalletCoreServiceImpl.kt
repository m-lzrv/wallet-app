package com.wallet.app.services

import com.wallet.app.db.entities.TransactionHistoryEntity
import com.wallet.app.db.entities.WalletEntity
import com.wallet.app.db.entities.enums.OperationType
import com.wallet.app.db.entities.enums.OperationType.CREDIT
import com.wallet.app.db.entities.enums.OperationType.DEBIT
import com.wallet.app.db.entities.enums.TransactionStatus
import com.wallet.app.db.repositories.TransactionHistoryRepository
import com.wallet.app.db.repositories.WalletRepository
import com.wallet.app.dto.CoreTransactionDto
import com.wallet.app.dto.GetWalletBalanceResponseDto
import com.wallet.app.dto.RegisterWalletResponseDto
import com.wallet.app.dto.TransactionHistoryDto
import com.wallet.app.exceptions.DuplicateTransactionException
import com.wallet.app.exceptions.NotEnoughFundsException
import com.wallet.app.exceptions.WalletNotFoundException
import mu.KotlinLogging
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import java.util.*

@Service
@Transactional
class WalletCoreServiceImpl(
    private val walletRepository: WalletRepository,
    private val transactionHistoryRepository: TransactionHistoryRepository,
    private val coreResponseMapper: CoreResponseMapper,
) : WalletCoreService {

    override fun registerWallet(playerId: String): RegisterWalletResponseDto {

        val walletEntity = WalletEntity(
            walletId = UUID.randomUUID(), playerId = playerId, balance = 0, createdAt = ZonedDateTime.now()
        )
        val savedWallet = walletRepository.save(walletEntity)
        log.info { "wallet was created: $savedWallet" }

        return savedWallet.let {
            coreResponseMapper.mapRegisterResponse(savedWallet)
        }

    }

    override fun getWalletBalance(walletId: UUID): GetWalletBalanceResponseDto {
        val walletEntity = walletRepository.findByIdOrNull(walletId) ?: throw WalletNotFoundException()
        return coreResponseMapper.mapWalletBalanceResponse(walletEntity)
    }

    override fun getTransactionHistoryByWalletId(walletId: UUID): List<TransactionHistoryDto> {
        val historyEntityList = transactionHistoryRepository.findAllByWalletId(walletId)
        return coreResponseMapper.mapHistoriesToResponse(historyEntityList)
    }

    override fun processTransaction(transaction: CoreTransactionDto) {

        val walletEntity = walletRepository.findByIdOrNull(transaction.walletId) ?: throw WalletNotFoundException()

        try {
            var balanceAfterOperation: Long = walletEntity.balance

            if (transaction.operationType == DEBIT) {
                balanceAfterOperation = walletEntity.balance - transaction.amount
                checkPositiveBalanceAfterWithdrawal(
                    balanceAfterOperation, transaction.transactionId, transaction.operationType
                )
            } else if (transaction.operationType == CREDIT) {
                balanceAfterOperation = walletEntity.balance + transaction.amount
            }

            walletEntity.balance = balanceAfterOperation

        } catch (err: NotEnoughFundsException) {
            val saveTransactionHistory = saveTransactionHistory(
                transaction.transactionId, transaction.operationType, walletEntity, transaction.amount
            )
            log.warn("processTransaction was failed : $saveTransactionHistory")
            throw err
        }

        val savedWallet = walletRepository.save(walletEntity)
        log.info("wallet was updated: $savedWallet")

        val saveTransactionHistory = saveTransactionHistory(
            transaction.transactionId, transaction.operationType, walletEntity, transaction.amount
        )
        log.info("processTransaction was executed: $saveTransactionHistory")
    }

    private fun checkPositiveBalanceAfterWithdrawal(
        result: Long, transactionId: UUID, operationType: OperationType
    ) {
        if (result < 0) {
            throw NotEnoughFundsException(transactionId, operationType)
        }
    }


    private fun saveTransactionHistory(
        transactionId: UUID, operationType: OperationType, walletEntity: WalletEntity, amount: Long
    ): TransactionHistoryEntity {
        try {
            val transactionHistoryEntity = TransactionHistoryEntity(
                transactionId = transactionId,
                walletId = walletEntity.walletId,
                operationType = operationType,
                transactionStatus = TransactionStatus.DEBIT_DECLINED,
                amount = amount,
                executedAt = ZonedDateTime.now()
            )
            return transactionHistoryRepository.save(transactionHistoryEntity)

        } catch (e: DataIntegrityViolationException) {
            log.error("Cannot perform save to db: ${e.message}", e)
            throw DuplicateTransactionException(transactionId, operationType)
        }
    }

    companion object {
        private val log = KotlinLogging.logger {}
    }
}