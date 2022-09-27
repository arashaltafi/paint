package com.arash.altafi.paint.sample2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arash.altafi.paint.databinding.ActivitySample2Binding

class Sample2Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySample2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySample2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}