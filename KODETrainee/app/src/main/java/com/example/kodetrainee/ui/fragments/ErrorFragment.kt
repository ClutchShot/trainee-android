package com.example.kodetrainee.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.kodetrainee.R
import com.example.kodetrainee.databinding.FragmentErrorBinding
import com.example.kodetrainee.databinding.NotFoundUserViewBinding
import com.example.kodetrainee.repository.SharedViewModel


class ErrorFragment : Fragment() {


    lateinit var binding: FragmentErrorBinding
    lateinit var sharedViewModel: SharedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentErrorBinding.inflate(inflater, container, false)

        binding.errorText3.setOnClickListener {
            sharedViewModel.response = MutableLiveData()
            sharedViewModel.getAllUsers()
            fragmentManager?.popBackStack()
        }
        return binding.root
    }


}