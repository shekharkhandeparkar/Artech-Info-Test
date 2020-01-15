package co.artechinfo.shekhar.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import co.artechinfo.shekhar.model.Fact

open class FactsViewModel(val factsRepository: FactsRepository) : ViewModel() {

    lateinit var context: Context
    val reloadTrigger = MutableLiveData<Boolean>()
    val facts: LiveData<List<Fact>> = Transformations.switchMap(reloadTrigger) {
        factsRepository.fetchFactsLiveData(context, reloadTrigger.value!!)
    }

    init {
        reloadTrigger.value = false
    }

    fun fetchFactsData(activity: Context): LiveData<List<Fact>> {
        context = activity
        return facts
    }

    fun refreshFacts() {
        reloadTrigger.value = true
    }

    fun isRequestTimedOut(): LiveData<Boolean> {
        return factsRepository.isRequestTimedOut()
    }
}