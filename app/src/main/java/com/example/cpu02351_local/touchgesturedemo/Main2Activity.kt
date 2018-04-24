package com.example.cpu02351_local.touchgesturedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.from_left_out)
    }

    fun finishNoAnim() {
        this.finish()
        overridePendingTransition(0, 0)
    }
}
