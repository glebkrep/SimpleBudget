package com.glebkrep.simplebudget.core.domain.models

import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity

data class RecentTransaction(
    val uid: Int = 0,
    val date: Long,
    val sum: Double,
    val isPlusOperation: Boolean,
    val comment: String?
)

internal fun RecentTransactionEntity.toRecentTransaction(): RecentTransaction {
    return RecentTransaction(
        uid = this.uid,
        date = this.date,
        sum = this.sum,
        isPlusOperation = this.isPlusOperation,
        comment = this.comment
    )
}

internal fun RecentTransaction.toEntity(): RecentTransactionEntity {
    return RecentTransactionEntity(
        uid = this.uid,
        date = this.date,
        sum = this.sum,
        isPlusOperation = this.isPlusOperation,
        comment = this.comment
    )
}
