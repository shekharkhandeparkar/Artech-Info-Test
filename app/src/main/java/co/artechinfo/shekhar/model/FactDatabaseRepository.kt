package co.artechinfo.shekhar.model

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room

class FactRepository(context: Context) {

    private val DB_NAME = "db_facts"

    private val factDatabase: FactDatabase

    val facts: List<Fact>
        get() = factDatabase.daoAccess().fetchAllFacts()

    init {
        factDatabase = Room.databaseBuilder(context, FactDatabase::class.java, DB_NAME).build()
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
                    factDatabase.daoAccess().deleteFact(fact!!)
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

    fun getFact(id: Int): Fact {
        return factDatabase.daoAccess().getFact(id)
    }
}