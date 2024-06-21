package com.glebkrep.simplebudget.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionDao
import com.glebkrep.simplebudget.core.database.recentTransaction.RecentTransactionEntity

@Database(
    entities = [RecentTransactionEntity::class],
    version = 1,
    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
    ],
    exportSchema = true
)
internal abstract class SimpleBudgetRoomDB : RoomDatabase() {
    abstract fun recentTransactionDao(): RecentTransactionDao
}
