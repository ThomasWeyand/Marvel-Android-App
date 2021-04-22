package br.com.thomas.weyandmarvel.presentation.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType
import br.com.thomas.weyandmarvel.R
import br.com.thomas.weyandmarvel.databinding.HomeActivityLayoutBinding
import br.com.thomas.weyandmarvel.extension.switchVisibility
import br.com.thomas.weyandmarvel.model.CharacterClickData
import br.com.thomas.weyandmarvel.presentation.*
import br.com.thomas.weyandmarvel.presentation.adapter.CharacterAdapter
import br.com.thomas.weyandmarvel.presentation.detail.CharacterDetailActivity
import br.com.thomas.weyandmarvel.presentation.favorite.FavoriteCharactersActivity
import br.com.thomas.weyandmarvel.presentation.mapper.mapToDetail
import br.com.thomas.weyandmarvel.presentation.search.SearchHeroeActivity
import br.com.thomas.weyandmarvel.presentation.utils.CharacterClickListener
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity : BaseActivity(),
    CharacterClickListener {

    private val homeViewModel: HomeViewModel by viewModel()
    private val adapter by lazy { CharacterAdapter(this) }
    private lateinit var binding: HomeActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity_layout)

        setupRecycler()
        loadListData()
        observeClickType()
        setupAdapterLoadingListeners()

        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun setupAdapterLoadingListeners() {
        adapter.addLoadStateListener { loadState ->
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            showEmptyList(isListEmpty)

            binding.recycler.switchVisibility(loadState.mediator?.refresh is LoadState.NotLoading)
            binding.loadingSpinner.switchVisibility(loadState.mediator?.refresh is LoadState.Loading)
            binding.retryButton.switchVisibility(loadState.mediator?.refresh is LoadState.Error)

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun showEmptyList(isEmpty: Boolean) {
        binding.apply {
            recycler.switchVisibility(!isEmpty)
            emptyResult.switchVisibility(isEmpty)
        }
    }

    private fun observeClickType() {
        val dataObserver = Observer<CharacterClickData> { data ->
            if (data.clickType == ClickCharacterType.OPEN_DETAILS) {
                startDetailActivity(data)
            }
        }

        homeViewModel.clickData.observe(this, dataObserver)
    }

    private fun loadListData() {
        lifecycleScope.launch {
            homeViewModel.getCharacters().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun setupRecycler() {
        binding.recycler.apply {
            layoutManager =
                GridLayoutManager(this@HomeActivity, 2, GridLayoutManager.VERTICAL, false)
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            adapter = this@HomeActivity.adapter
        }
    }

    override fun clickedHeroe(selectedItem: CharacterClickData) {
        homeViewModel.characterSelected(selectedItem)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return when (id) {
            R.id.action_search -> {
                startSearchActivity()
                true
            }
            R.id.action_favortie -> {
                startFavoriteActivity()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    }

    private fun startDetailActivity(clickData: CharacterClickData) {
        intent =
            CharacterDetailActivity.getIntent(
                this,
                clickData.character.mapToDetail()
            )

        startActivity(intent)

    }

    private fun startFavoriteActivity() {
        startActivity(
            FavoriteCharactersActivity.getIntent(
                this
            )
        )
    }

    private fun startSearchActivity() {
        startActivity(SearchHeroeActivity.getIntent(this))
    }
}
