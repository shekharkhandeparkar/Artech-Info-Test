package co.artechinfo.shekhar.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import co.artechinfo.shekhar.R
import co.artechinfo.shekhar.model.Fact
import co.artechinfo.shekhar.viewmodel.FactsRepository
import co.artechinfo.shekhar.viewmodel.FactsViewModel
import co.artechinfo.shekhar.viewmodel.FactsViewModelFactory
import kotlinx.android.synthetic.main.fragment_facts.*

/**
 * A simple [Fragment] subclass.
 */
class FactsFragment : Fragment() {

    private val mFactsList: ArrayList<Fact> = arrayListOf()
    private var mFactsViewModel: FactsViewModel? = null
    private var mFactsAdapter: FactsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mFactsViewModel =
            ViewModelProviders.of(this, FactsViewModelFactory(FactsRepository(activity!!)))
                .get(FactsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_facts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchFacts()

        swipeToRefreshFacts.setOnRefreshListener {
            mFactsViewModel!!.refreshFacts()
            fetchFacts()
        }

        initRecyclerView()
    }

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

    private fun fetchFacts() {
        swipeToRefreshFacts.isRefreshing = false
        showLoader()

        mFactsViewModel!!.fetchFactsData(activity!!)
            .observe(this, Observer { factsList -> prepareList(factsList) })
        mFactsViewModel!!.isRequestTimedOut()
            .observe(this, Observer { t -> if (t) hideLoaderWithMessage() })
    }

    private fun prepareList(factsList: List<Fact>?) {
        mFactsList.clear()
        if (factsList != null) {
            mFactsList.addAll(factsList)
        }
        mFactsAdapter?.notifyDataSetChanged()
        hideLoader()
    }

    private fun showLoader() {
        progressBarFacts.visibility = View.VISIBLE
        textViewEmpty.visibility = View.GONE
    }

    private fun hideLoader() {
        progressBarFacts.visibility = View.GONE
    }

    private fun hideLoaderWithMessage() {
        textViewEmpty.visibility = View.VISIBLE
        Toast.makeText(activity!!, getString(R.string.some_error_occurred), Toast.LENGTH_LONG).show()
        hideLoader()
    }
}
