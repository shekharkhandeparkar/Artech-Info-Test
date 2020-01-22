package co.artechinfo.shekhar.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.artechinfo.shekhar.model.Fact
import co.artechinfo.shekhar.model.FactDatabaseRepository
import co.artechinfo.shekhar.networking.RestApiService
import kotlinx.coroutines.*
import retrofit2.HttpException

/*
* FactsRepository class
* repository for fetching data
* */
open class FactsRepository(context: Context) {

    // variable declaration & initialisation
    private var mContext: Context = context
    private var facts = mutableListOf<Fact>()
    private var mutableFactsLiveData = MutableLiveData<List<Fact>>()
    private val mRequestTimeout = MutableLiveData<Boolean>()
    private var completableJob = Job()
    var factDatabaseRepository = FactDatabaseRepository(context)
    private var coroutinesScope = CoroutineScope(Dispatchers.IO + completableJob)

    init {
        mRequestTimeout.value = false
    }

    private val thisApiCorService by lazy {
        // lazy instance of retrofit client
        RestApiService.createService(mContext)
    }

    fun isRequestTimedOut(): LiveData<Boolean> {
        return mRequestTimeout
    }

    fun fetchFactsLiveDataFromDB(): MutableLiveData<List<Fact>> {       // fetch data from db
        val factsDb: List<Fact>
        if (factDatabaseRepository != null) {
            factsDb = factDatabaseRepository.getAllFacts()
            facts = factsDb.toMutableList()
            mutableFactsLiveData.value = facts
        }

        return mutableFactsLiveData
    }

    fun fetchFactsLiveDataFromServer(): MutableLiveData<List<Fact>> {   // fetch data from server
        if (coroutinesScope == null) {
            if (completableJob == null) {
                completableJob = Job()
            }
            coroutinesScope = CoroutineScope(Dispatchers.IO + completableJob)
        }
        val serverJob = coroutinesScope.async {
            val request = thisApiCorService.fetchFactsAsync()
            withContext(Dispatchers.IO) {
                try {
                    val response = request.await()
                    if (response.rows != null) {
                        facts = response.rows as MutableList<Fact>
                        GlobalScope.launch(Dispatchers.Main) {
                            // make operations on main thread
                            mutableFactsLiveData.value = facts
                            factDatabaseRepository.refreshFacts(facts)
                        }
                    }
                } catch (e: HttpException) {
                    e.printStackTrace()
                    errorOccurred()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    errorOccurred()
                }
            }
        }

        return mutableFactsLiveData
    }

    private fun errorOccurred() {                                       // for error handling
        GlobalScope.launch(Dispatchers.Main) {
            mRequestTimeout.value = true
        }
    }
}