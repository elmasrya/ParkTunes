package io.parkmobile.parktunes.view.fragments

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.parkmobile.parktunes.R
import io.parkmobile.parktunes.databinding.SongDetailsFragmentBinding
import io.parkmobile.parktunes.models.Song
import io.parkmobile.parktunes.viewmodels.SharedViewModel
import java.io.IOException

private const val TAG = "SongDetailsFragment"

class SongDetailsFragment: Fragment(R.layout.song_details_fragment) {
    private lateinit var sViewModel: SharedViewModel
    private var _binding: SongDetailsFragmentBinding? = null
    private lateinit var song: Song
    private  val binding get() = _binding!!
    val mediaPlayer = MediaPlayer();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SongDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        binding.tvSongTitleDetail.text = arguments?.getString("track_name")
        binding.tvArtistNameDetail.text = arguments?.getString("artist_name")
        binding.tvKindDetail.text = arguments?.getString("explicit_status")
        binding.tvReleaseDate.text = arguments?.getString("release_date")
        var musicUrl = arguments?.getString("preview_url")
        var artistUrl = arguments?.getString("artist_url")

        var explicitStatus = arguments?.getString("explicit_status");
        if (explicitStatus?.contains("not") == true) {
            explicitStatus = "Not Explicit"
        }
        binding.tvExplicitDetail.text = explicitStatus

        binding.tvArtistInfo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(artistUrl)
            startActivity(intent)
        }

        binding.tvMediaControl.setOnClickListener {
            binding.tvMediaControl.text = "Playing..."
            binding.tvMediaControl.isEnabled = false
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                mediaPlayer.setDataSource(musicUrl)
                mediaPlayer.prepare()
                mediaPlayer.start()
                mediaPlayer.release()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            Log.v(TAG, "Music is streaming")
        }

    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying || mediaPlayer.isLooping) {
            mediaPlayer.stop()
            mediaPlayer.release()
            binding.tvMediaControl.text = getString(R.string.play)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}