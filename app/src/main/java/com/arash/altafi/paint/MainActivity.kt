package com.arash.altafi.paint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arash.altafi.paint.databinding.ActivityMainBinding
import com.arash.altafi.paint.sample1.Sample1Activity
import com.arash.altafi.paint.sample2.Sample2Activity
import com.arash.altafi.paint.sample3.Sample3Activity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.apply {
            btnSample1.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample1Activity::class.java))
            }
            btnSample2.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample2Activity::class.java))
            }
            btnSample3.setOnClickListener {
                startActivity(Intent(this@MainActivity, Sample3Activity::class.java))
            }
        }
    }

}