package com.uber.rib.sample

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.uber.rib.core.Router
import com.uber.rib.core.SimpleRibActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : SimpleRibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar
                .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun createRouter(): Router<*, *> {
        TODO("not implemented") // To change body of
        // created functions use File | Settings | File Templates.
    }
}
