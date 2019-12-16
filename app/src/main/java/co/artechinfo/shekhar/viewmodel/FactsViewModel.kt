package co.artechinfo.shekhar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import co.artechinfo.shekhar.model.Fact

class FactsViewModel(private val factsRepository: FactsRepository) : ViewModel() {

    private val reloadTrigger = MutableLiveData<Boolean>()
    private val users: LiveData<List<Fact>> = Transformations.switchMap(reloadTrigger) {
        factsRepository.fetchFactsLiveData()
    }

    init {
        refreshFacts()
    }

    fun fetchFactsData(): LiveData<List<Fact>> = users

    fun refreshFacts() {
        reloadTrigger.value = true
    }

    fun isRequestTimedOut(): LiveData<Boolean> {
        return factsRepository.isRequestTimedOut()
    }
}