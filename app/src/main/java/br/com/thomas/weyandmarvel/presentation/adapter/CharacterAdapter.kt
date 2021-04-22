package br.com.thomas.weyandmarvel.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import br.com.thomas.weyandmarvel.databinding.CharacterItemLayoutBinding
import br.com.thomas.weyandmarvel.model.ResultItem
import br.com.thomas.weyandmarvel.presentation.utils.CharacterClickListener

class CharacterAdapter(private val clickListener: CharacterClickListener) :
    PagingDataAdapter<ResultItem.Character, CharacterViewHolder>(repositoryComparator) {

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            CharacterItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            clickListener
        )
    }

    companion object {
        private val repositoryComparator = object : DiffUtil.ItemCallback<ResultItem.Character>() {
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
