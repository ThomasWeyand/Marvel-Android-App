package br.com.thomas.weyandmarvel.presentation.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import br.com.thomas.weyandmarvel.R
import br.com.thomas.weyandmarvel.databinding.CharacterDetailLayoutBinding
import br.com.thomas.weyandmarvel.model.CharacterDetailVO
import br.com.thomas.weyandmarvel.presentation.BaseActivity
import br.com.thomas.weyandmarvel.presentation.favorite.FavoriteCharactersActivity
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
        setupTransition()

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
            if (!it.isFavorite && isFromFavoriteScreen())
                cancelExitTransition()

            lifecycleScope.launch {
                detailViewModel.updateFavoriteItemDatabase(it)
            }
        }
    }

    private fun cancelExitTransition() {
        window.run {
            sharedElementExitTransition = null
            sharedElementReturnTransition = null
            sharedElementReenterTransition = null
        }
        binding.heroeImage.transitionName = null
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

    private fun setupTransition() {
        getIntentExtra()?.let {
            binding.heroeImage.transitionName = it.id.toString()
        }
    }

    private fun isFromFavoriteScreen() =
        getPreviousActivitynameExtra()?.let {
            it == FavoriteCharactersActivity.TAG
        } ?: run {
            false
        }

    private fun getIntentExtra() = intent?.extras?.getParcelable<CharacterDetailVO>(
        EXTRA_CHARACTER
    )

    private fun getPreviousActivitynameExtra() = intent?.extras?.getString(
        EXTRA_PREVIOUS_ACTIVITY
    )

    companion object {
        private const val EXTRA_CHARACTER = "extra_character"
        private const val EXTRA_PREVIOUS_ACTIVITY = "extra_previous_activity"
        fun getIntent(
            context: Context,
            characterDetailVO: CharacterDetailVO,
            previousActivityName: String? = null
        ) =
            Intent(context, CharacterDetailActivity::class.java).apply {
                putExtra(EXTRA_CHARACTER, characterDetailVO)
                putExtra(EXTRA_PREVIOUS_ACTIVITY, previousActivityName)
            }
    }
}