package com.billyindrai.aplikasigithubuser

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.billyindrai.aplikasigithubuser.database.Database
import com.billyindrai.aplikasigithubuser.database.DatabaseDAO
import com.billyindrai.aplikasigithubuser.database.DatabaseRepo
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val users = MutableLiveData<ArrayList<User>>()
    private val userFollowing = MutableLiveData<ArrayList<User>>()
    private val userFollowers = MutableLiveData<ArrayList<User>>()
    private val userDetail = MutableLiveData<User>()
    private val databaseUser: LiveData<List<User>>
    private val repository: DatabaseRepo
    private var findDatabase: LiveData<User>? = null

    init {
        val databaseDao: DatabaseDAO = Database.getDatabase(application.applicationContext)!!.databaseDAO()
        repository = DatabaseRepo(databaseDao)
        databaseUser = repository.users
    }

    fun insert(user: User) = viewModelScope.launch { repository.insert(user) }

    fun findUser(username: String) : LiveData<User>? {
        findDatabase = repository.findUser(username)
        return findDatabase
    }

    fun delete(user: User) = viewModelScope.launch { user.username.let { repository.delete(it) } }

    fun getAllusers() : LiveData<List<User>> = databaseUser

    fun searchUser(query : String){
        val request = DataRepo.create()
        request.searchUser(query).enqueue(object : Callback<Result> {

            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                users.postValue(response.body()!!.result)
                Log.d("onResponse", "searchUser: ${response.body()!!.result.size} - ${users.value?.size}")
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                Log.e("onFailure", t.message.toString())
            }
        })

    }

    fun loadSearchUser() : LiveData<ArrayList<User>>{
        Log.d("loadSearchUser", "users:${users.value?.size}")
        return users
    }

    fun getUserDetail(user : User) {
        val request = DataRepo.create()
        request.getUser(user.username).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                userDetail.postValue(response.body())
                Log.d("onResponse", "getUserDetail: ${response.body()!!.name} - ${userDetail.value?.name}")
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("onFailure", t.message.toString())
            }

        })
    }

    fun loadUserDetail() : LiveData<User> {
        Log.d("userDetail", "userDetail:${userDetail.value}")
        return userDetail
    }

    fun getUserFollowing(user: User){
        val request = DataRepo.create()
        request.getFollowing(user.username).enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                userFollowing.postValue(response.body() as ArrayList<User>)
                Log.d("onResponse", "getUserFollowing: ${response.body()!!.size} - ${userFollowing.value?.size}")
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("onFailure", t.message.toString())
            }

        })
    }

    fun loadUserFollowing() : LiveData<ArrayList<User>> {
        Log.d("loadUserFollowing", "loadUserFollowing:${userFollowing.value?.size}")
        return userFollowing
    }

    fun getUserFollowers(user: User){
        val request = DataRepo.create()
        request.getFollowers(user.username).enqueue(object : Callback<List<User>> {
            override fun onResponse(
                call: Call<List<User>>,
                response: Response<List<User>>
            ) {
                userFollowers.postValue(response.body() as ArrayList<User>)
                Log.d("onResponse", "getUserFollowers: ${response.body()!!.size} - ${userFollowers.value?.size}")
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("onFailure", t.message.toString())
            }

        })
    }

    fun loadUserFollowers() : LiveData<ArrayList<User>> {
        Log.d("loadUserFollowers", "loadUserFollowers:${userFollowers.value?.size}")
        return userFollowers
    }
}