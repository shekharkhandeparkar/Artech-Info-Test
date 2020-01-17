package co.artechinfo.shekhar.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.artechinfo.shekhar.R
import co.artechinfo.shekhar.model.Fact
import co.artechinfo.shekhar.viewmodel.FactsRepository
import co.artechinfo.shekhar.viewmodel.FactsViewModel
import co.artechinfo.shekhar.viewmodel.FactsViewModelFactory
import kotlinx.android.synthetic.main.fragment_facts.*

/*
* FactsFragment Fragment class
* Fragment for loading and showing the fact data from api in recyclerview
* */

class FactsFragment : Fragment() {

    // param declarations
    private val mFactsList: ArrayList<Fact> = arrayListOf()     // for list
    private var mFactsAdapter: FactsAdapter? = null             // adapter for the recyclerview
    private var mFactsViewModel: FactsViewModel? = null         // viewmodel instance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFactsViewModel =
            ViewModelProviders.of(this, FactsViewModelFactory(FactsRepository(activity!!)))
                .get(FactsViewModel::class.java)                // create instance of viewmodel with the help of viewporvider attached to UI
        return inflater.inflate(R.layout.fragment_facts, container, false)  // return inflated view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()                                      // initialise all the views other view work required

        observeViewModel(mFactsViewModel!!)                     // add observer for the viewmodel data (for any data change)

        fetchFacts()                                            // fetch facts data call

        swipeToRefreshFacts.setOnRefreshListener {
            fetchFacts()                                        // fetch facts data call on swipe to refresh
        }
    }

    /*
    * Method for initialisation of views and adapter
    * */
    private fun initRecyclerView() {
        mFactsAdapter = FactsAdapter(mFactsList)
        recyclerViewFacts.layoutManager = LinearLayoutManager(activity!!)
        recyclerViewFacts.itemAnimator = DefaultItemAnimator()
        val dividerItemDecoration = DividerItemDecoration(
            recyclerViewFacts.context,
            (recyclerViewFacts.layoutManager as LinearLayoutManager).orientation
        )
        recyclerViewFacts.addItemDecoration(dividerItemDecoration)
        recyclerViewFacts.adapter = mFactsAdapter
    }

    /*
     * Method for fetching the fact data
     * */
    private fun fetchFacts() {
        swipeToRefreshFacts.isRefreshing = false
        showLoader()

        mFactsViewModel!!.fetchFactsData(activity!!)
    }

    /*
     * Method to observe the changes in the livedata and react on the changes
     * */
    private fun observeViewModel(mFactsViewModel: FactsViewModel) {
        mFactsViewModel.facts.observe(this,
            Observer<List<Fact>> { factList ->
                if (factList != null && factList.isNotEmpty()) {
                    prepareList(factList)
                }
            })

        mFactsViewModel.isRequestTimedOut()
            .observe(this, Observer { t -> if (t) hideLoaderWithMessage() })
    }

    /*
     * Method to refresh the data in the recyclerview
     * */
    private fun prepareList(factsList: List<Fact>?) {
        mFactsList.clear()
        if (factsList != null) {
            mFactsList.addAll(factsList)
        }
        mFactsAdapter?.notifyDataSetChanged()
        hideLoader()
    }

    /*
     * Method to show the loader
     * */
    private fun showLoader() {
        progressBarFacts.visibility = View.VISIBLE
        textViewEmpty.visibility = View.GONE
    }

    /*
     * Method to show the loader with message
     * */
    private fun hideLoaderWithMessage() {
        textViewEmpty.visibility = View.VISIBLE
        Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_LONG)
            .show()
        hideLoader()
    }

    /*
     * Method to hide the loader
     * */
    private fun hideLoader() {
        progressBarFacts.visibility = View.GONE
    }
}
