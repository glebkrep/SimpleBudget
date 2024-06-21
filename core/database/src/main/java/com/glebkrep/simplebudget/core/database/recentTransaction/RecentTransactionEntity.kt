package com.glebkrep.simplebudget.core.database.recentTransaction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recents")
data class RecentTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val uid: Int = 0,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "sum") val sum: Double,
    @ColumnInfo(name = "isPlusOperation") val isPlusOperation: Boolean,
    @ColumnInfo(name = "comment") val comment: String?
)
