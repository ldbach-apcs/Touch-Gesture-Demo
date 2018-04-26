package com.example.cpu02351_local.touchgesturedemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onBackPressed() {
        this.finish()
        overridePendingTransition(0, R.anim.from_left_out)
    }
}
