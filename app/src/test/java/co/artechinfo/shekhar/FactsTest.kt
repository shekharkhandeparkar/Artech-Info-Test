package co.artechinfo.shekhar

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import co.artechinfo.shekhar.viewmodel.FactsRepository
import co.artechinfo.shekhar.viewmodel.FactsViewModel
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock


class FactsTest {

    @get: Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    var repo: FactsRepository? = null

    @Mock
    var vm: FactsViewModel? = null

    @Mock
    var context: Context? = null

    @Before
    fun setup() {
        context = mock(Context::class.java)

        repo = mock(FactsRepository::class.java)
        vm = mock(FactsViewModel::class.java)
    }

    @Test
    fun testFactList1() {
        verify(vm)?.fetchFactsData(context!!)
        assert(vm?.facts?.value?.size != 0)
    }

    @Test
    fun testFactList2() {
        verify(vm)?.factsRepository?.fetchFactsLiveDataFromServer()
        assert(vm?.facts?.value?.size != 0)
    }

    @Test
    fun testFactList3() {
        verify(vm)?.facts?.value?.isEmpty()
        assert(vm?.facts?.value?.size != 0)
    }

    @Test
    fun testFactList4() {
        verify(vm)?.factsRepository?.factDatabaseRepository?.getAllFacts()
        assert(vm?.facts?.value?.size != 0)
    }

    @Test
    fun testFactList5() {
        val hasFacts = vm?.factsRepository?.factDatabaseRepository?.getAllFacts()
        assert(hasFacts == null)
    }

    @Test
    fun testFactList6() {
        val count = vm?.factsRepository?.factDatabaseRepository?.getAllFacts()?.size
        assert(count != 0)
    }
}