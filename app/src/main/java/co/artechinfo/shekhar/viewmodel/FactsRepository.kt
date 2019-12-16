package co.artechinfo.shekhar.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import co.artechinfo.shekhar.model.Fact
import co.artechinfo.shekhar.networking.NoConnectivityException
import co.artechinfo.shekhar.networking.RestApiService
import kotlinx.coroutines.*
import retrofit2.HttpException
import androidx.lifecycle.LiveData

class FactsRepository(context: Context) {

    private var mContext: Context = context

    private var facts = mutableListOf<Fact>()
    private var mutableFactsLiveData = MutableLiveData<List<Fact>>()
    private val mRequestTimeout = MutableLiveData<Boolean>()
    private val completableJob = Job()
    private val coroutinesScope = CoroutineScope(Dispatchers.IO + completableJob)

    private val thisApiCorService by lazy {
        RestApiService.createService(mContext)
    }

    fun isRequestTimedOut(): LiveData<Boolean> {
        return mRequestTimeout
    }

    fun fetchFactsLiveData(): MutableLiveData<List<Fact>> {
        mRequestTimeout.value = false
        coroutinesScope.launch {
            val request = thisApiCorService.fetchFactsAsync()
            withContext(Dispatchers.Main) {
                try {
                    val response = request.await()
                    if (response.rows != null) {
                        facts = response.rows as MutableList<Fact>
                        mutableFactsLiveData.value = facts
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

        return mutableFactsLiveData
    }

    private fun errorOccurred() {
        GlobalScope.launch(Dispatchers.Main) {
            mRequestTimeout.value = true
        }
    }
}