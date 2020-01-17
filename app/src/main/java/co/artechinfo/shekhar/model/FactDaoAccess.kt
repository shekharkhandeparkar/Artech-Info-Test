package co.artechinfo.shekhar.model

import androidx.room.*

/*
* FactDaoAccess class
* for accessing the data from db
* */
@Dao
interface FactDaoAccess {
    @Insert
    fun insertFact(fact: Fact): Long?                        // insert a fact in database

    @Query("SELECT * FROM Fact ORDER BY id asc")      // fetch all facts from database
    fun fetchAllFacts(): List<Fact>

    @Query("DELETE FROM Fact")                        // delete all facts from database
    fun deleteAllFacts()
}