package com.example.kodetrainee.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kodetrainee.R
import com.example.kodetrainee.databinding.FragmentPlaceHolderBinding
import com.example.kodetrainee.repository.SharedViewModel
import com.example.kodetrainee.ui.adapters.UserRecyclerAdapter


class PlaceholderFragment : Fragment(), UserRecyclerAdapter.onItemListener {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: FragmentPlaceHolderBinding

    private lateinit var adapter: UserRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPlaceHolderBinding.inflate(inflater, container, false)
        binding.usersRecycleview.layoutManager = LinearLayoutManager(requireContext())
        adapter = UserRecyclerAdapter(requireContext(), this)

        binding.shimmerLayout.startShimmer()
        sharedViewModel.response.observe(viewLifecycleOwner,{
            if (it != null){
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.visibility = View.INVISIBLE
                binding.usersRecycleview.visibility = View.VISIBLE
            }
        })

        binding.usersRecycleview.adapter = adapter

        binding.swiperefreshLayout.setOnRefreshListener{
            sharedViewModel.getAllUsers()
        }

        val departmentCode = arguments?.getInt(ARG_SECTION_NUMBER)
        sharedViewModel.allUsers.observe(viewLifecycleOwner, Observer { it ->

            if (departmentCode == 0) {
                if (it.isNotEmpty()){
                    adapter.users = it
                    binding.swiperefreshLayout.isRefreshing = false
                    binding.swiperefreshLayout.visibility = View.VISIBLE
                    binding.notFoundUser.notFoundUserRoot.visibility = View.INVISIBLE
                }
                else{
                    binding.swiperefreshLayout.visibility = View.INVISIBLE
                    binding.notFoundUser.notFoundUserRoot.visibility = View.VISIBLE
                }
            }
            else {
                val departmentName = resources.getStringArray(R.array.departments_list)[departmentCode!!]
                var temp = it.filter {
                    it.department.equals(departmentName)
                }
                if (temp.isNotEmpty()){
                    binding.swiperefreshLayout.visibility = View.VISIBLE
                    binding.notFoundUser.notFoundUserRoot.visibility = View.INVISIBLE
                    adapter.users = temp
                }
                else{
                    binding.swiperefreshLayout.visibility = View.INVISIBLE
                    binding.notFoundUser.notFoundUserRoot.visibility = View.VISIBLE
                }
                binding.swiperefreshLayout.isRefreshing = false
            }
            adapter.notifyDataSetChanged()
        }
        )
        return binding.root
    }


    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onItemClick(position: Int) {
        sharedViewModel.setUser(adapter.users[position])

        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.placeholder_main, UserFragment(), "UserFragment")
            ?.addToBackStack(null)
            ?.commit()
    }





}