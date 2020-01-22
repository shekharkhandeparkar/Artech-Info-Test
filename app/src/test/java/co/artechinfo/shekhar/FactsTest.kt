package co.artechinfo.shekhar

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.artechinfo.shekhar.model.Fact
import co.artechinfo.shekhar.model.FactDaoAccess
import co.artechinfo.shekhar.model.FactDatabase
import co.artechinfo.shekhar.model.FactDatabaseRepository
import co.artechinfo.shekhar.viewmodel.FactsRepository
import co.artechinfo.shekhar.viewmodel.FactsViewModel
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FactsTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    lateinit var vm:FactsViewModel
    @Mock
    var context: Context? = null
    lateinit var repo: FactsRepository
    var factDatabaseRepository:FactDatabaseRepository = mock()
    @Mock
    var appDatabase: FactDatabase = mock()
    var facts = ArrayList<Fact>()
    @Mock
    lateinit var factInfoDao: FactDaoAccess

    @Before
    fun setup() {
        context = mock(Context::class.java)
        repo = FactsRepository(context!!)
        repo.factDatabaseRepository = factDatabaseRepository
        repo.factDatabaseRepository.factDatabase = appDatabase
        vm = FactsViewModel(repo)
    }

    @Test
    fun testResponseSize() {
        initDummy()
        Mockito.`when`(appDatabase.daoAccess())
            .thenReturn(factInfoDao)
        Mockito.`when`(appDatabase.daoAccess().fetchAllFacts())
            .thenReturn(facts)
        vm.fetchFactsData(context!!)
        Assert.assertNotNull(vm.facts)
        Assert.assertEquals(2, vm.facts.value!!.size)
    }

    @Test
    fun testTileShown(){
        initDummy()
        Mockito.`when`(appDatabase.daoAccess())
            .thenReturn(factInfoDao)
        Mockito.`when`(appDatabase.daoAccess().fetchAllFacts())
            .thenReturn(facts)
        vm.fetchFactsData(context!!)
        Assert.assertEquals("Fact Title", vm.facts.value!![0].title)
        Assert.assertEquals("Fact Description", vm.facts.value!![0].description)
    }

    private fun initDummy() {
        val fact = Fact()
        fact.title = "Fact Title"
        fact.description = "Fact Description"
        facts.add(fact)
        facts.add(Fact())
    }

    @Test
    fun testFactList1(){
        vm.factsRepository = repo
        assert(vm.facts.value?.size!=0)
    }

    @Test
    fun testFactList2(){
        assert(vm.facts.value?.size!=0)
    }


}