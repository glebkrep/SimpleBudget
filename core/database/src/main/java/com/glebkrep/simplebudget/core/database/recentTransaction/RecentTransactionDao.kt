package com.glebkrep.simplebudget.core.database.recentTransaction

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentTransactionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(recentTransaction: RecentTransactionEntity): Long

    @Query("SELECT * FROM recents order by date desc limit 100")
    fun getAllLatestFirstFlow(): Flow<List<RecentTransactionEntity>>

    @Query("DELETE FROM recents WHERE id=:deleteId")
    fun deleteById(deleteId: Int)

    @Query("DELETE FROM recents WHERE id IN (SELECT id FROM recents order by id desc LIMIT 1)")
    fun deleteLatest()

    @Query("SELECT * FROM recents where id=:id limit 1")
    suspend fun getById(id: Int): RecentTransactionEntity?

    @Query("SELECT COUNT(*) FROM recents")
    fun getTotalNumberOfItems(): Flow<Int>
}
