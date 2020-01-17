package co.artechinfo.shekhar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
* FactsViewModelFactory class
* */
class FactsViewModelFactory(private val factsRepository: FactsRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FactsViewModel(factsRepository) as T
    }

}