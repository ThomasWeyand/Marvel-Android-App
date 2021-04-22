package br.com.thomas.weyandmarvel.presentation.adapter

import android.util.Pair
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType
import br.com.thomas.weyandmarvel.databinding.FavoriteCharacterItemLayoutBinding
import br.com.thomas.weyandmarvel.model.CharacterClickData
import br.com.thomas.weyandmarvel.model.ResultItem
import br.com.thomas.weyandmarvel.presentation.utils.CharacterClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class FavoriteCharacterViewHolder(
    private val binding: FavoriteCharacterItemLayoutBinding,
    private val clickListener: CharacterClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ResultItem.Character) {
        binding.apply {
            name.text = item.name
            loadImageFromUrl(item.thumbnail, thumbnail)
            container.setOnClickListener {
                clickListener.clickedHeroe(CharacterClickData(item, ClickCharacterType.OPEN_DETAILS, thumbnail))
            }
        }
    }

    private fun loadImageFromUrl(url: String, target: AppCompatImageView?) {
        target?.apply {
            context?.let { safeContext ->
                Glide.with(safeContext)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
            }
        }
    }

}