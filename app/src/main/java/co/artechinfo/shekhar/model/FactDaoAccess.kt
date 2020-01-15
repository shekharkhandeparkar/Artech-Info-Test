package co.artechinfo.shekhar.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FactDaoAccess {
    @Insert
    fun insertFact(fact: Fact): Long?

    @Query("SELECT * FROM Fact ORDER BY id asc")
    fun fetchAllFacts(): List<Fact>

    @Query("SELECT * FROM Fact WHERE id =:factId")
    fun getFact(factId: Int): Fact

    @Update
    fun updateFact(fact: Fact)

    @Delete
    fun deleteFact(fact: Fact)

    @Query("DELETE FROM Fact")
    fun deleteAllFacts()
}