package com.example.kodetrainee.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.kodetrainee.R
import com.example.kodetrainee.databinding.FragmentUserBinding
import com.example.kodetrainee.entity.User
import com.example.kodetrainee.repository.SharedViewModel




import android.os.Build
import android.view.*

import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat

import java.util.*






class UserFragment : Fragment() {


    private lateinit var binding: FragmentUserBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        statusBarColor()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        sharedViewModel.userInfo.observe(viewLifecycleOwner, {
            setinfo(it)
        })

        binding.backBtn.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentManager?.popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        return binding.root
    }



    fun setinfo(user: User) {
        Glide.with(requireContext())
            .load(user.avatarUrl)
            .into(binding.cardUserImg)

        val pattern = "d MMMM yyyy"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date: String = simpleDateFormat.format(user.birthday)
        binding.pageUserBirthdate.text = date
        binding.pageUserName.text = user.firstName + " " + user.lastName
        binding.pageUserTitle.text = user.position
        binding.pageUserPhone.text = user.phone
        val c1 = Calendar.getInstance()
        c1.time = user.birthday!!

        val c2 = Calendar.getInstance()

        val years = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)
        binding.pageUserYears.text = "$years years"

    }

    fun statusBarColor(){
        val window: Window = requireActivity().window

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.user_header)
    }

}


