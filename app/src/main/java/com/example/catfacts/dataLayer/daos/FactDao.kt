package com.example.catfacts.dataLayer.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catfacts.dataLayer.entities.Fact

@Dao
interface FactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFact(fact: Fact)

    @Query("SELECT * FROM facts WHERE id = :id")
    fun getFactById(id: Int): Fact?

    @Query("SELECT * FROM facts WHERE id = :id-1")
    fun getLastFact(id: Int): Fact?
}