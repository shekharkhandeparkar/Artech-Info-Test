package co.artechinfo.shekhar.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.artechinfo.shekhar.R

/*
* FactsActivity activity class
* for the main UI (nav host fragment)
* */
class FactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facts)
    }
}
