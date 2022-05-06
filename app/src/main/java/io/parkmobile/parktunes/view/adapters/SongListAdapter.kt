package io.parkmobile.parktunes.view.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.ui.text.font.Font
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.parkmobile.parktunes.R
import io.parkmobile.parktunes.databinding.SongListItemBinding
import io.parkmobile.parktunes.models.Song
import io.parkmobile.parktunes.view.fragments.SongSearchFragment
import io.parkmobile.parktunes.view.fragments.SongSearchFragmentDirections

private const val TAG = "SongListAdapter"
class  SongListAdapter(private val songList: ArrayList<Song>, private val fragment: SongSearchFragment) : RecyclerView
.Adapter<SongListAdapter.SongsViewHolder>() {


    class SongsViewHolder(val binding: SongListItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongsViewHolder {
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
            song.imageView = binding.ivAlbumCover
            fragment.sViewModelSong.loadImage(binding.ivAlbumCover, song.artworkUrl100.toString().replace("100x100", "200x200"))

            binding.bMoreInfo.setOnClickListener {
                fragment.sViewModelSong.selectedSong(song)
                val action = SongSearchFragmentDirections.actionSongSearchFragmentToSongDetailsFragment()
                fragment.findNavController().navigate(action)
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
}