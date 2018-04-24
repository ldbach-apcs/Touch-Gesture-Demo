package com.example.cpu02351_local.touchgesturedemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switch_activity.setOnClickListener {
            // val bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            val intent = Intent(this@MainActivity, Main2Activity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right_in, 0)
        }
    }
}
