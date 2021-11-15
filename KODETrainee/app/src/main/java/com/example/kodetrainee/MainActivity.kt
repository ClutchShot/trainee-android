package com.example.kodetrainee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kodetrainee.databinding.ActivityMainBinding
import com.example.kodetrainee.ui.fragments.MainFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.placeholder_main, MainFragment())
        ft.commit()
    }
}