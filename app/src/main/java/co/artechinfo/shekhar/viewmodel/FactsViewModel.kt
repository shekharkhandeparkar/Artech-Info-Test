package co.artechinfo.shekhar.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import co.artechinfo.shekhar.model.Fact

/*
* FactsViewModel class
* */
open class FactsViewModel(var factsRepository: FactsRepository) : ViewModel() {

    lateinit var context: Context
    var reloadTrigger = MutableLiveData<Boolean>()
    var facts: LiveData<List<Fact>> = Transformations.switchMap(reloadTrigger) {
        //fetch default data
        factsRepository.fetchFactsLiveDataFromDB()
    }

    init {
        reloadTrigger.value = true
    }

    /*
    * called from fragment for reloading the data
    * */
    fun fetchFactsData(activity: Context): LiveData<List<Fact>> {
        context = activity
        facts = factsRepository.fetchFactsLiveDataFromDB()              //fetch db data
        facts = factsRepository.fetchFactsLiveDataFromServer()          //fetch server data
        return facts
    }

    fun isRequestTimedOut(): LiveData<Boolean> {
        return factsRepository.isRequestTimedOut()
    }
}