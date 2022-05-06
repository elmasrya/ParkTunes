package io.parkmobile.parktunes.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import io.parkmobile.parktunes.R
import io.parkmobile.parktunes.databinding.SongSearchFragmentBinding
import io.parkmobile.parktunes.view.adapters.SongListAdapter
import io.parkmobile.parktunes.viewmodels.SongSharedViewModel


class SongSearchFragment : Fragment(R.layout.song_search_fragment) {
    private lateinit var handler:Handler
    private lateinit var searchRunnable:Runnable
    val sViewModelSong: SongSharedViewModel by activityViewModels()
    private var _binding: SongSearchFragmentBinding? = null
    private val songsListAdapter = SongListAdapter(arrayListOf(), this)
    private  val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SongSearchFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSongs.layoutManager = LinearLayoutManager(view.context)
        binding.rvSongs.adapter = songsListAdapter
        handler = Handler(Looper.getMainLooper())
        searchRunnable = Runnable {
            val queryText:String = binding.etSongFragment.text.toString()
            sViewModelSong.limitSearchItunesApiCall(view.context, queryText, "25")
        }

        binding.etSongFragment.doAfterTextChanged {
            handler.removeCallbacks(searchRunnable)
            if (binding.etSongFragment.text.toString().isNotEmpty()) {
                handler.postDelayed(searchRunnable, 600L);
            }
        }

        binding.etSongFragment.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                //Perform Code
                handler.removeCallbacks(searchRunnable)
                val queryText:String = binding.etSongFragment.text.toString()
                sViewModelSong.limitSearchItunesApiCall(view.context, queryText, "25")
                return@OnKeyListener false
            }
            false
        })
        observeViewModel()
    }

    private fun observeViewModel() {
        sViewModelSong.iTunesLiveData.observe(viewLifecycleOwner) {
            songsListAdapter.updateSongList(it)
        }

        sViewModelSong.iTunesNoResultsLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.tvNoResults.visibility = View.VISIBLE
                binding.rvSongs.visibility = View.GONE
            } else {
                binding.tvNoResults.visibility = View.GONE
                binding.rvSongs.visibility = View.VISIBLE
            }
        }

        sViewModelSong.iTunesLoadErrorLiveData.observe(viewLifecycleOwner) {
            binding.tvError.visibility = if (it) View.VISIBLE else View.GONE
        }

        sViewModelSong.loadingLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.rvSongs.visibility = View.GONE
                binding.tvNoResults.visibility = View.GONE
                binding.pbSongFragment.visibility = View.VISIBLE
            } else {
                binding.rvSongs.visibility = View.VISIBLE
                binding.pbSongFragment.visibility = View.GONE
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}