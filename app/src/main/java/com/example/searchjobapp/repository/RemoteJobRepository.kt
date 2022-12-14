package com.example.searchjobapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.searchjobapp.api.RetrofitInstance
import com.example.searchjobapp.db.FavoriteJobDatabase
import com.example.searchjobapp.models.JobToSave
import com.example.searchjobapp.models.RemoteJob
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author : Mingaleev D
 * @data : 20/08/2022
 */

class RemoteJobRepository(
    private val db: FavoriteJobDatabase
) {
    private val remoteJobService = RetrofitInstance.api
    private val remoteJobResponseLiveData: MutableLiveData<RemoteJob?> = MutableLiveData()
    private val searchRemoteJobLiveData: MutableLiveData<RemoteJob?> = MutableLiveData()

    init {
        getRemoteJobResponse()
    }


    private fun getRemoteJobResponse() {
        remoteJobService.getRemoteJob().enqueue(object : Callback<RemoteJob> {
            override fun onResponse(call: Call<RemoteJob>, response: Response<RemoteJob>) {
                remoteJobResponseLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<RemoteJob>, t: Throwable) {
                remoteJobResponseLiveData.postValue(null)
                Log.d("test", "onFailure: ${t.message}")
            }
        })
    }

    fun remoteJobResult(): MutableLiveData<RemoteJob?> {
        return remoteJobResponseLiveData
    }

    fun searchJobResponse(query: String?){
        remoteJobService.searchRemoteJob(query).enqueue(object :Callback<RemoteJob>{
            override fun onResponse(call: Call<RemoteJob>, response: Response<RemoteJob>) {
                searchRemoteJobLiveData.postValue(response.body())
            }

            override fun onFailure(call: Call<RemoteJob>, t: Throwable) {
               searchRemoteJobLiveData.postValue(null)
            }

        })
    }

    fun searchJobResult(): MutableLiveData<RemoteJob?> {
        return searchRemoteJobLiveData
    }

    suspend fun addFavoriteJob(job: JobToSave) = db.getFavJobDao().addFavoriteJob(job)
    suspend fun deleteJob(job: JobToSave) = db.getFavJobDao().deleteFavJob(job)
    fun getAllFavJobs() = db.getFavJobDao().getAllFavJob()


}