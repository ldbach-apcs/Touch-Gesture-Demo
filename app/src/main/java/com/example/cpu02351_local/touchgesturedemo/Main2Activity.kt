package com.example.cpu02351_local.touchgesturedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class Main2Activity : AppCompatActivity() {

    private val data = ArrayList<String>()

    init {
        for (i in 1..101) {
            data.add("Test number $i")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val rv = findViewById<RecyclerView>(R.id.scrollContainer)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = SimpleStringAdapter(data)
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
