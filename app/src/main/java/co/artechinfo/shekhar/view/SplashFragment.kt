package co.artechinfo.shekhar.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import co.artechinfo.shekhar.R
/*
* SplashFragment Fragment class
* for splashscreen (if required - created to demo navigation component & use of fragment)
* */
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navigate to other fragment
        val navController = Navigation.findNavController(
            activity!!,
            R.id.app_nav_host_fragment
        )
        navController.navigate(
            R.id.action_splashFragment_to_factsFragment,
            null
        )
    }

}
