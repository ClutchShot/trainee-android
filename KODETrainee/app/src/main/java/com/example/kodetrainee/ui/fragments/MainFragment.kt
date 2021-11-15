package com.example.kodetrainee.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.RadioGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.kodetrainee.R
import com.example.kodetrainee.databinding.FragmentMainBinding
import com.example.kodetrainee.repository.SharedViewModel
import com.example.kodetrainee.ui.adapters.SectionsPagerAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import android.net.ConnectivityManager
import android.provider.Settings

/**
 *  Главный фрагмен. В нем нахрдятся viewPager для
 *  фрагментов по департаментам
 */

class MainFragment : Fragment() {


    private lateinit var binding: FragmentMainBinding
    private lateinit var sharedViewModel : SharedViewModel
    private lateinit var sectionsPagerAdapter : SectionsPagerAdapter
    private lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        if (isAirplaneModeOn(requireContext()) || !isNetworkAvailable(requireContext())){
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.placeholder_main, ErrorFragment(), "ErrorFragment")
                ?.addToBackStack(null)
                ?.commit()
        }else{
            // отправляем запрос
            sharedViewModel.getAllUsers()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        errorView()

        binding = FragmentMainBinding.inflate(inflater, container, false)
        sectionsPagerAdapter = SectionsPagerAdapter(
            requireContext(),
            childFragmentManager
        )
        viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs

        tabs.setupWithViewPager(viewPager)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val searchView = binding.searchView
        searchView.setOnQueryTextListener( object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                sharedViewModel.filterByNameAndLastName(newText)
                return true
            }
        })

        val fillter_btn = binding.filterBtn
        fillter_btn.setOnClickListener {
            bottomSheetDialog()
        }

        return  binding.root
    }


    fun bottomSheetDialog(){
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetDialogView = layoutInflater.inflate(R.layout.bottom_fllter_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetDialogView)

        val radioGroup = bottomSheetDialogView.findViewById<RadioGroup>(R.id.filter_radio_group)
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { _, checkedId ->

            if (checkedId == R.id.filter_alphabet_btn){
                sharedViewModel.sortByName()
            }
            if (checkedId == R.id.filter_birthdate_btn){
                sharedViewModel.sortByBirthdate()
            }

        })

        bottomSheetDialog.show()

    }

    override fun onResume() {
        super.onResume()
        statusBarColor()
    }

    fun statusBarColor(){
        val window: Window? = activity?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.white)

    }


    fun errorView(){
        sharedViewModel.response.observe(viewLifecycleOwner,{
            if (!it.isSuccessful){
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.placeholder_main, ErrorFragment(), "ErrorFragment")
                    ?.addToBackStack(null)
                    ?.commit()
            }
        })
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

    fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.System.getInt(
            context.contentResolver,
            Settings.System.AIRPLANE_MODE_ON,
            0
        ) != 0
    }
}