package io.parkmobile.parktunes.viewmodels

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.net.Uri
import android.os.PowerManager
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import io.parkmobile.parktunes.R
import io.parkmobile.parktunes.global.network.NetConfig
import io.parkmobile.parktunes.global.utils.ParkTunesToast
import io.parkmobile.parktunes.models.Song
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SongSharedViewModel : ViewModel() {
    val iTunesLiveData = MutableLiveData<ArrayList<Song>>()
    val iTunesLoadErrorLiveData = MutableLiveData<Boolean>()
    val isPlaying = MutableLiveData<Boolean>()
    val iTunesNoResultsLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = MutableLiveData<Boolean>()
    val selectedSongLiveData = MutableLiveData<Song>()
    private var mediaPlayer: MediaPlayer? = null
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
                if (results.isEmpty() || results.size == 0) {
                    iTunesNoResultsLiveData.value = true
                } else {
                    iTunesLiveData.value = results
                }
                iTunesNoResultsLiveData.value = false
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

    fun selectedSong(song: Song) {
        Log.d(TAG, "selectedSong: $song")
        selectedSongLiveData.value = song
    }

    fun loadImage(view: ImageView, url: String) {
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.itunes_logo_circle)
            .error(R.drawable.itunes_logo_circle)
            .into(view);
    }

    fun playMusic(context: Context, musicURI: Uri) {
        isPlaying.value = true
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK)
            setDataSource(
                context,
                musicURI
            )
            prepare()
        }

        mediaPlayer!!.setOnPreparedListener {
            it.start()
            isPlaying.value = true
        }
        mediaPlayer!!.setOnErrorListener { mp, what, extra ->
            Log.d(TAG, "playMusic: 3 setOnPreparedListener")

            isPlaying.value = false
            ParkTunesToast(context, R.string.api_error).showShort();
            stopMusic()
            false
        }

        mediaPlayer!!.setOnCompletionListener {
            isPlaying.value = false
            stopMusic()
        }

    }

    fun stopMusic() {
        isPlaying.value = false
        mediaPlayer!!.release();
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}
