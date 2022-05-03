package io.parkmobile.parktunes.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import io.parkmobile.parktunes.global.utils.ParkTunesToast
import io.parkmobile.parktunes.models.Song
import io.parkmobile.parktunes.R
import io.parkmobile.parktunes.global.network.NetConfig

class SharedViewModel : ViewModel() {
    val iTunesLiveData = MutableLiveData<ArrayList<Song>>()
    val iTunesLoadErrorLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val selected = MutableLiveData<Song>()

    fun select(song: Song) {
        selected.value = song
    }
    val TAG = "volleyTag"
    private var queue: RequestQueue? = null

    /**
     *
     */
    fun limitSearchItunesApiCall(context: Context, searchInput:String, limit:String) {
        queue = Volley.newRequestQueue(context)

        var url: String = NetConfig.BASE_URL + NetConfig.SEARCH_PARAM + searchInput + NetConfig
            .LIMIT_PARAM + limit
        val stringRequest = StringRequest(NetConfig.POST, url,
            {
                val sType = object : TypeToken<JsonObject>() {}.type
                val itunesJsonArray = Gson().fromJson<JsonObject>(it, sType)
                    .get("results")
                val pType = object :TypeToken<ArrayList<Song>>() {}.type
                val results = Gson().fromJson<ArrayList<Song>>(itunesJsonArray, pType)
                iTunesLiveData.value = results
                loadingLiveData.value = false
                ParkTunesToast(context, R.string.success_status).showShort();
            },{
                ParkTunesToast(context, R.string.api_error).showLong()
                loadingLiveData.value = false
                iTunesLoadErrorLiveData.value = true
                ParkTunesToast(context, R.string.api_error).showShort();
            }).apply {
            tag = "TAG"
        }
        queue?.add(stringRequest)
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}
