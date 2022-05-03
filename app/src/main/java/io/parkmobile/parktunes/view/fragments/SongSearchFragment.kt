package io.parkmobile.parktunes.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.compose.ui.input.key.KeyEvent
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.parkmobile.parktunes.R
import io.parkmobile.parktunes.databinding.SongSearchFragmentBinding
import io.parkmobile.parktunes.models.Song
import io.parkmobile.parktunes.view.adapters.SongListAdapter
import io.parkmobile.parktunes.viewmodels.SharedViewModel


class SongSearchFragment : Fragment(R.layout.song_search_fragment) {
    private lateinit var handler:Handler;
    private lateinit var searchRunnable:Runnable;
    private lateinit var sViewModel: SharedViewModel
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
        sViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        binding.rvSongs.layoutManager = LinearLayoutManager(view.context)
        binding.rvSongs.adapter = songsListAdapter
        handler = Handler(Looper.getMainLooper())
        searchRunnable = Runnable {
            val queryText:String = binding.etSongFragment.text.toString()
            sViewModel.limitSearchItunesApiCall(view.context, queryText, "25")
        }

        binding.etSongFragment.doAfterTextChanged {
            handler.removeCallbacks(searchRunnable)
            if (binding.etSongFragment.text.toString().isNotEmpty()) {
                handler.postDelayed(searchRunnable, 600L);
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        sViewModel.iTunesLiveData.observe(viewLifecycleOwner) {
            songsListAdapter.updateSongList(it)
        }
        sViewModel.iTunesLoadErrorLiveData.observe(viewLifecycleOwner) {
            binding.tvError.visibility = if (it) View.VISIBLE else View.GONE
        }

        sViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.rvSongs.visibility = View.GONE
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