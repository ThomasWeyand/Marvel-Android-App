package br.com.thomas.weyandmarvel.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import br.com.thomas.weyandmarvel.databinding.FavoriteCharacterItemLayoutBinding
import br.com.thomas.weyandmarvel.model.ResultItem
import br.com.thomas.weyandmarvel.presentation.utils.CharacterClickListener

class FavoriteCharacterAdapter(private val clickListener: CharacterClickListener) : PagingDataAdapter<ResultItem.Character, FavoriteCharacterViewHolder>(
    favoriteComparator
) {

    override fun onBindViewHolder(holder: FavoriteCharacterViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCharacterViewHolder {
        return FavoriteCharacterViewHolder(
            FavoriteCharacterItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )
    }

    companion object {
        private val favoriteComparator = object : DiffUtil.ItemCallback<ResultItem.Character>() {
            override fun areItemsTheSame(
                oldItem: ResultItem.Character,
                newItem: ResultItem.Character
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ResultItem.Character,
                newItem: ResultItem.Character
            ): Boolean {
                return oldItem == newItem
            }


        }
    }
}