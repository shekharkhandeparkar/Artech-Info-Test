package co.artechinfo.shekhar.model

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import kotlinx.coroutines.*

/*
* Database repository class
* for interacting with db
* */
open class FactDatabaseRepository(context: Context) {

    private val DB_NAME = "db_facts"
    var factDatabase: FactDatabase

    init {
        factDatabase = Room.databaseBuilder(context, FactDatabase::class.java, DB_NAME).build()
    }

    /*
    * Method for fetching all the facts async
    * */
    fun getAllFacts(): List<Fact> = runBlocking {
        val jobA = async { getAllFactsDB() }
        jobA.await()
    }

    /*
     * Method for fetching all the facts from database
     * */
    private suspend fun getAllFactsDB(): List<Fact> {
        var facts = emptyList<Fact>()
        val completableJob = Job()
        val coroutinesScope = CoroutineScope(Dispatchers.IO + completableJob)
        val job = coroutinesScope.async {
            withContext(Dispatchers.IO) {
                try {
                    facts = factDatabase.daoAccess().fetchAllFacts()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        job.await()
        return facts
    }

    /*
     * Method for refresh the contents of  database
     * */
    fun refreshFacts(facts: MutableList<Fact>) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                factDatabase.daoAccess().deleteAllFacts()
                return null
            }
        }.execute()

        for (fact in facts) {
            object : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg voids: Void): Void? {
                    factDatabase.daoAccess().insertFact(fact)
                    return null
                }
            }.execute()
        }
    }
}