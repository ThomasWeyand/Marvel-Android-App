package br.com.thomas.weyandmarvel.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import br.com.thomas.weyandmarvel.R
import br.com.thomas.weyandmarvel.databinding.CharacterDetailLayoutBinding
import br.com.thomas.weyandmarvel.model.CharacterDetailVO
import br.com.thomas.weyandmarvel.presentation.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.character_detail_layout.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterDetailActivity : BaseActivity() {

    private lateinit var binding: CharacterDetailLayoutBinding
    private val detailViewModel: CharacterDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.character_detail_layout)

        setupViews()
        setupFavoriteButton()
    }

    private fun setupFavoriteButton() {
        getIntentExtra()?.let { dataVO ->
            binding.heartIcon.run {
                isSelected = dataVO.isFavorite
                setOnClickListener {
                    selectHeartState(dataVO)
                }
            }
            binding.heartBackground.setOnClickListener {
                selectHeartState(dataVO)
            }
        }
    }

    private fun selectHeartState(item: CharacterDetailVO) {
        item.let {
            it.isFavorite = !it.isFavorite
            binding.heartIcon?.isSelected = it.isFavorite

            lifecycleScope.launch {
                detailViewModel.updateFavoriteItemDatabase(it)
            }
        }
    }

    private fun setupViews() {
        getIntentExtra()?.let {
            binding.apply {
                Glide.with(this@CharacterDetailActivity)
                    .load(it.thumbnailPath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(heroeImage)
            }

            name.text = it.name
            description.text = if (it.description.isNotEmpty()) it.description else description.text
        }
    }

    private fun getIntentExtra() = intent?.extras?.getParcelable<CharacterDetailVO>(
        EXTRA_CHARACTER
    )

    companion object {
        private const val EXTRA_CHARACTER = "extra_character"
        fun getIntent(
            context: Context,
            characterDetailVO: CharacterDetailVO
        ) =
            Intent(context, CharacterDetailActivity::class.java).apply {
                putExtra(EXTRA_CHARACTER, characterDetailVO)
            }
    }
}