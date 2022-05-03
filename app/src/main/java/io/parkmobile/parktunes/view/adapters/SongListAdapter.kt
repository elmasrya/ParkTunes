package io.parkmobile.parktunes.view.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.parkmobile.parktunes.R
import io.parkmobile.parktunes.databinding.SongDetailsFragmentBinding
import io.parkmobile.parktunes.databinding.SongListItemBinding
import io.parkmobile.parktunes.models.Song
import io.parkmobile.parktunes.view.fragments.SongSearchFragment
import io.parkmobile.parktunes.view.fragments.SongSearchFragmentDirections

private const val TAG = "SongListAdapter"
class  SongListAdapter(private val songList: ArrayList<Song>, private val fragment: Fragment) : RecyclerView
.Adapter<SongListAdapter.SongsViewHolder>() {


    class SongsViewHolder(val binding: SongListItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
        val context = parent.context;
        val binding = SongListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
       val song = songList[position]
        with (holder) {
            //with(loadImage(binding.ivAlbumCover,song.artworkUrl100,binding))
            binding.tvSongTitle.text = song.trackName
            binding.tvArtistName.text = song.artistName
            binding.tvMediaType.text = song.kind
            binding.bMoreInfo.setOnClickListener {
                val bundle = bundleOf(
                    "track_name" to song.trackName,
                    "artist_name" to song.artistName,
                    "explicit_status" to song.trackExplicitness,
                    "release_date" to song.releaseDate,
                    "artist_url" to song.artistViewUrl,
                    "preview_url" to song.previewUrl,
                    "track_kind" to song.kind
                )
                fragment.findNavController().navigate(R.id
                    .action_songSearchFragment_to_songDetailsFragment, bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return songList.size
    }


    fun updateSongList(updatedSongList: ArrayList<Song>) {
        songList.clear()
        songList.addAll(updatedSongList)
        notifyDataSetChanged();

    }

    fun loadImage(view: ImageView, url: String, error: Drawable) {
        Picasso.get().load(url).error(error).into(view)
    }
}