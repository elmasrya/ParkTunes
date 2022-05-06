package io.parkmobile.parktunes.view.fragments

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import io.parkmobile.parktunes.R
import io.parkmobile.parktunes.databinding.SongDetailsFragmentBinding
import io.parkmobile.parktunes.models.Song
import io.parkmobile.parktunes.viewmodels.SongSharedViewModel

private const val TAG = "SongDetailsFragment"
class SongDetailsFragment: Fragment(R.layout.song_details_fragment) {
    private val sViewModel: SongSharedViewModel by activityViewModels()
    private var _binding: SongDetailsFragmentBinding? = null
    private lateinit var song: Song
    private  val binding get() = _binding!!
    private lateinit var musicUrl: Uri
    private lateinit var artistUrl: String
    private lateinit var musicPlayerContext: Context;
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

        musicPlayerContext = requireContext()
        observeViewModel()

        binding.tvArtistInfo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(artistUrl)
            startActivity(intent)
        }


        binding.tvMediaControl.setOnClickListener {
            sViewModel.playMusic(musicPlayerContext, musicUrl )
        }

    }

    private fun observeViewModel() {
        Log.d(TAG, "observeViewModel: ")

        sViewModel.isPlaying.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvMediaControl.isEnabled = true
                binding.tvMediaControl.text = getString(R.string.stop)
                binding.tvMediaControl.setOnClickListener {
                    sViewModel.stopMusic()
                }
            } else {
                binding.tvMediaControl.isEnabled = true
                binding.tvMediaControl.text = getString(R.string.play)
                binding.tvMediaControl.setOnClickListener {
                    sViewModel.playMusic(musicPlayerContext, musicUrl )
                }
            }
        }

        sViewModel.selectedSongLiveData.observe(viewLifecycleOwner) {
            /** Release Date Formatting **/
            Log.d(TAG, "observeViewModel: " + it?.releaseDate.toString())
            val releaseDate = it?.releaseDate.toString()
            val reformattedReleaseDate = getString(R.string.published) + releaseDate.substring(5,7) + "/" +
                    releaseDate.substring(8,10) + "/" + releaseDate.substring(0,4)

            /** Track Kind Formatting **/
            var kind = it?.kind.toString()
            if (kind.length > 1) {
                kind = kind.substring(0, 1).uppercase() + kind.substring(1).lowercase();
            }

            kind = getString(R.string.media) + kind + getString(R.string.interpunct) +
                    it?.primaryGenreName

            /** Track Explicit Formatting **/
            var trackExplicitness = it?.trackExplicitness.toString()
            trackExplicitness = when {
                trackExplicitness.contains("not", true) -> {
                    getString(R.string.explicit) + getString(R.string.no)
                }
                trackExplicitness.contains("explicit", true) -> {
                    getString(R.string.explicit) + getString(R.string.yes)
                }
                else -> {
                    getString(R.string.explicit) + getString(R.string.unknown_default)
                }
            }

            val collectionName = getString(R.string.collection) + it?.collectionName

            musicUrl = Uri.parse(it?.previewUrl.toString())
            artistUrl = it?.artistViewUrl.toString()
            binding.tvSongTitleDetail.text = it?.trackName
            binding.tvArtistNameDetail.text = it?.artistName
            binding.tvCollectionNameDetail.text = collectionName
            binding.tvReleaseDate.text = reformattedReleaseDate
            binding.tvKindDetail.text = kind
            binding.tvExplicitDetail.text = trackExplicitness
            sViewModel.loadImage(binding.ivAlbumCover, it?.artworkUrl100.toString()
                .replace("100x100", "300x300"))

        }
    }

    override fun onPause() {
        super.onPause()
        sViewModel.stopMusic()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}