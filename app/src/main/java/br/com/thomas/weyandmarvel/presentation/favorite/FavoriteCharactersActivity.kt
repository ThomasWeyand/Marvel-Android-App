package br.com.thomas.weyandmarvel.presentation.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType
import br.com.thomas.weyandmarvel.R
import br.com.thomas.weyandmarvel.databinding.FavoriteCharactersLayoutBinding
import br.com.thomas.weyandmarvel.extension.switchVisibility
import br.com.thomas.weyandmarvel.model.CharacterClickData
import br.com.thomas.weyandmarvel.presentation.BaseActivity
import br.com.thomas.weyandmarvel.presentation.utils.CharacterClickListener
import br.com.thomas.weyandmarvel.presentation.detail.CharacterDetailActivity
import br.com.thomas.weyandmarvel.presentation.adapter.FavoriteCharacterAdapter
import br.com.thomas.weyandmarvel.presentation.mapper.mapToDetail
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteCharactersActivity : BaseActivity(),
    CharacterClickListener {

    private lateinit var binding: FavoriteCharactersLayoutBinding
    private val adapter by lazy { FavoriteCharacterAdapter(this) }
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.favorite_characters_layout)

        setupAdapter()
        fetchFavoriteData()
        observeClickType()

        addAdapterListener()
        supportActionBar?.title = "Favorites"
    }

    private fun setupAdapter() {
        binding.recycler.apply {
            layoutManager =
                GridLayoutManager(
                    this@FavoriteCharactersActivity,
                    2,
                    GridLayoutManager.VERTICAL,
                    false
                )
            itemAnimator = DefaultItemAnimator()
            adapter = this@FavoriteCharactersActivity.adapter
        }
    }

    private fun addAdapterListener() {
        adapter.addLoadStateListener { loadState ->
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyMessage(isListEmpty)
        }
    }

    private fun showEmptyMessage(listEmpty: Boolean) {
        binding.emptyResult.switchVisibility(listEmpty)
    }

    private fun fetchFavoriteData() {
        lifecycleScope.launch {
            favoriteViewModel.getFavoriteList().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun observeClickType() {
        val dataObserver = Observer<CharacterClickData> { data ->
            if (data.clickType == ClickCharacterType.OPEN_DETAILS) {
                startDetailActivity(data)
            }
        }

        favoriteViewModel.clickData.observe(this, dataObserver)
    }

    private fun startDetailActivity(clickData: CharacterClickData) {
        intent =
            CharacterDetailActivity.getIntent(
                this,
                clickData.character.mapToDetail(),
                TAG
            )
        startActivity(intent)

    }

    companion object {
        const val TAG = "FavoriteCharactersActivity"
        fun getIntent(context: Context) = Intent(context, FavoriteCharactersActivity::class.java)
    }

    override fun clickedHeroe(selectedItem: CharacterClickData) {
        favoriteViewModel.characterSelected(selectedItem)
    }

}