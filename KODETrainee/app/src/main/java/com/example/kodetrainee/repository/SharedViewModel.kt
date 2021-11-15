package com.example.kodetrainee.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.kodetrainee.api.RetrofitInstance
import com.example.kodetrainee.entity.Item
import com.example.kodetrainee.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class SharedViewModel : ViewModel() {

    val allUsers: MutableLiveData<List<User>> = MutableLiveData()
    var response: MutableLiveData<Response<Item?>> = MutableLiveData()
    val userInfo : MutableLiveData<User> = MutableLiveData()


    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            val _response = RetrofitInstance.api.getAllUsers()
            response.postValue(_response)
            allUsers.postValue(_response.body()?.items)

        }
    }


    fun filterByNameAndLastName(name: String?) {


        val temp = response.value?.body()?.items
        if (temp != null) {
            if (name != null) {
                allUsers.postValue(temp.filter {
                    it.firstName?.lowercase()?.contains(name.lowercase())!! ||
                            it.lastName?.lowercase()?.contains(name.lowercase())!!
                })
            } else {
                allUsers.postValue(temp)
            }
        }

    }

    fun sortByName(){
        val temp = allUsers.value
        allUsers.postValue(temp?.sortedBy {
            it.firstName
        })
    }


    // Сортировка по дате реализована не точно
    fun sortByBirthdate(){
        val temp = allUsers.value
        allUsers.postValue(temp?.sortedByDescending { it.birthday })
    }

    fun setUser(user: User){
        userInfo.postValue(user)
    }

}
