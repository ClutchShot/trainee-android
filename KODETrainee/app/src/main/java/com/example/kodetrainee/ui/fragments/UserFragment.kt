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
import android.Manifest.permission.CALL_PHONE


import android.os.Build
import android.view.*

import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat

import java.util.*
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

/**
 * Фрагмент детальной информации сотрудника
 */

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



        binding.pageUserPhone.setOnClickListener {

            val phone_numb = sharedViewModel.userInfo.value?.phone
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phone_numb")
            if (ContextCompat.checkSelfPermission(requireContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            } else {
                requestPermissions(arrayOf(CALL_PHONE), 1);
            }

        }

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

        val nickname = (user.firstName?.get(0) ?: "").toString()  +
                (user.lastName?.get(0) ?: "").toString()
        binding.pageUserNickname.text = nickname.lowercase()
        binding.pageUserPhone.text = user.phone

        val c1 = Calendar.getInstance()
        c1.time = user.birthday!!
        val c2 = Calendar.getInstance()
        val years = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)
        binding.pageUserYears.text = "$years years"



    }
    //Более точный метод нахождения разницы даты
    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateUserAge(birthdate: Date) : String{
        val now_date = LocalDate.now()
        val local_birthdate = birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        val period = Period.between(now_date,local_birthdate)
        return period.years.toString()
    }


    fun statusBarColor(){
        val window: Window = requireActivity().window

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.user_header)
    }

}


