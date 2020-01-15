package co.artechinfo.shekhar.model

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import co.artechinfo.shekhar.networking.NoConnectivityException
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception

open class FactDatabaseRepository(context: Context) {

    private val DB_NAME = "db_facts"

    private val factDatabase: FactDatabase

    init {
        factDatabase = Room.databaseBuilder(context, FactDatabase::class.java, DB_NAME).build()
    }

    fun getAllFacts(): List<Fact> = runBlocking {
        val jobA = async { getAllFactsDB() }
        jobA.await()
    }

    suspend fun getAllFactsDB(): List<Fact> {
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



    fun insertFact(fact: Fact) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                factDatabase.daoAccess().insertFact(fact)
                return null
            }
        }.execute()
    }

    fun updateFact(fact: Fact) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                factDatabase.daoAccess().updateFact(fact)
                return null
            }
        }.execute()
    }

    fun deleteFact(id: Int) {
        val fact = getFact(id)
        if (fact != null) {
            object : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg voids: Void): Void? {
                    factDatabase.daoAccess().deleteFact(fact)
                    return null
                }
            }.execute()
        }
    }

    fun deleteFact(fact: Fact) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                factDatabase.daoAccess().deleteFact(fact)
                return null
            }
        }.execute()
    }

    fun deleteAllFacts() {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg voids: Void): Void? {
                factDatabase.daoAccess().deleteAllFacts()
                return null
            }
        }.execute()
    }

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

    fun getFact(id: Int): Fact {
        return factDatabase.daoAccess().getFact(id)
    }
}