package br.com.thomas.weyandmarvel.presentation.adapter

import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType
import br.com.thomas.weyandmarvel.databinding.CharacterItemLayoutBinding
import br.com.thomas.weyandmarvel.model.CharacterClickData
import br.com.thomas.weyandmarvel.model.ResultItem
import br.com.thomas.weyandmarvel.presentation.utils.CharacterClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class CharacterViewHolder(
    private val binding: CharacterItemLayoutBinding,
    private val clickListener: CharacterClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ResultItem.Character) {
        binding.apply {

            thumbnail.setOnClickListener {
                clickListener.clickedHeroe(CharacterClickData(item, ClickCharacterType.OPEN_DETAILS))
            }

            name.setOnClickListener {
                clickListener.clickedHeroe(CharacterClickData(item, ClickCharacterType.OPEN_DETAILS))
            }

            name.text = item.name
            loadImageFromUrl(item.thumbnail, thumbnail)

            heartIcon.apply {
                transitionName = item.id.toString()
                isSelected = item.isFavorite
                setOnClickListener {
                    selectHeartState(item)
                }
            }

            heartBackground.setOnClickListener {
                selectHeartState(item)
            }
        }
    }

    private fun selectHeartState(item: ResultItem.Character) {
        item.isFavorite = !item.isFavorite
        clickListener.clickedHeroe(CharacterClickData(item, ClickCharacterType.CHANGE_FAVORITE))
        binding.heartIcon?.isSelected = item.isFavorite
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