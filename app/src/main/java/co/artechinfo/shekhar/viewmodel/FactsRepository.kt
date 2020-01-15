package co.artechinfo.shekhar.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import co.artechinfo.shekhar.model.Fact
import co.artechinfo.shekhar.networking.NoConnectivityException
import co.artechinfo.shekhar.networking.RestApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import androidx.lifecycle.LiveData
import co.artechinfo.shekhar.model.FactDatabaseRepository

open class FactsRepository(context: Context) {

    private var mContext: Context = context

    private var facts = mutableListOf<Fact>()
    private var mutableFactsLiveData = MutableLiveData<List<Fact>>()
    val mRequestTimeout = MutableLiveData<Boolean>()
    private val completableJob = Job()
    var factDatabaseRepository = FactDatabaseRepository(context)
    private val coroutinesScope = CoroutineScope(Dispatchers.IO + completableJob)

    init {
        mRequestTimeout.value = false
    }

    private val thisApiCorService by lazy {
        RestApiService.createService(mContext)
    }

    fun isRequestTimedOut(): LiveData<Boolean> {
        return mRequestTimeout
    }

    fun fetchFactsLiveData(context: Context, value: Boolean): MutableLiveData<List<Fact>> {

        println("value $value")
        var factsDb: List<Fact> = arrayListOf()
        if (factDatabaseRepository != null) {
            factsDb = factDatabaseRepository.getAllFacts()
        }
        when {
            value -> fetch(context)
            factsDb.isNotEmpty() -> {
                facts = factsDb.toMutableList()
                mutableFactsLiveData.value = facts
            }
            else -> fetch(context)
        }

        return mutableFactsLiveData
    }

    fun fetch(context: Context) {
        mRequestTimeout.value = false
        coroutinesScope.launch {
            val request = thisApiCorService.fetchFactsAsync()
            withContext(Dispatchers.IO) {
                try {
                    val response = request.await()
                    if (response.rows != null) {
                        facts = response.rows as MutableList<Fact>
                        mutableFactsLiveData.value = facts

                        factDatabaseRepository.refreshFacts(facts)
                    }
                } catch (e: NoConnectivityException) {
                    e.printStackTrace()
                    errorOccurred()
                } catch (e: HttpException) {
                    e.printStackTrace()
                    errorOccurred()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    errorOccurred()
                }
            }
        }
    }

    private fun errorOccurred() {
        GlobalScope.launch(Dispatchers.Main) {
            mRequestTimeout.value = true
        }
    }
}