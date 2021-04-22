package br.com.thomas.weyandmarvel.presentation.search

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import br.com.thomas.weyandmarvel.presentation.utils.ClickCharacterType
import br.com.thomas.weyandmarvel.R
import br.com.thomas.weyandmarvel.databinding.SearchHeroeLayoutBinding
import br.com.thomas.weyandmarvel.extension.switchVisibility
import br.com.thomas.weyandmarvel.model.CharacterClickData
import br.com.thomas.weyandmarvel.presentation.BaseActivity
import br.com.thomas.weyandmarvel.presentation.utils.CharacterClickListener
import br.com.thomas.weyandmarvel.presentation.detail.CharacterDetailActivity
import br.com.thomas.weyandmarvel.presentation.adapter.CharacterAdapter
import br.com.thomas.weyandmarvel.presentation.mapper.mapToDetail
import kotlinx.android.synthetic.main.search_heroe_layout.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchHeroeActivity : BaseActivity(), SearchView.OnQueryTextListener,
    SearchView.OnCloseListener,
    CharacterClickListener {

    private lateinit var binding: SearchHeroeLayoutBinding
    private val searchViewModel: SearchHeroeViewModel by viewModel()
    private var searchJob: Job? = null
    private val adapter: CharacterAdapter by lazy { CharacterAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.search_heroe_layout)

        initAdapter()
        initAdapterListeners()
        initSearchListeners()
        setupScrollListener()
        observeClickType()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun initSearchListeners() {
        binding.search.apply {
            isIconified = false
            setOnQueryTextListener(this@SearchHeroeActivity)
            setOnCloseListener(this@SearchHeroeActivity)
        }
    }

    private fun initAdapter() {
        binding.recycler.apply {
            layoutManager =
                GridLayoutManager(this@SearchHeroeActivity, 2, GridLayoutManager.VERTICAL, false)
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
            adapter = this@SearchHeroeActivity.adapter
        }
    }

    private fun initAdapterListeners() {
        adapter.addLoadStateListener { loadState ->
            val isListEmpty =
                loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0 && binding.search.query.toString()
                    .isNotEmpty()
            showEmptyList(isListEmpty)
            binding.searchMessage.switchVisibility(
                loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0 && binding.search.query.toString()
                    .isEmpty()
            )

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

    private fun setupScrollListener() {
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    binding.recycler.scrollToPosition(0)
                }
        }
    }

    private fun showEmptyList(show: Boolean) {
        recycler.switchVisibility(!show)
        emptyResult.switchVisibility(show)
    }

    private fun updateListFromInput(text: String) {
        if (text.isNotEmpty()) {
            search(text)
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            searchViewModel.searchHeroes(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            updateListFromInput(it.trim())
        }
        return true
    }

    override fun onClose(): Boolean {
        val text = binding.search.query.toString()
        updateListFromInput(text.trim())
        return true
    }

    override fun clickedHeroe(selectedItem: CharacterClickData) {
        searchViewModel.characterSelected(selectedItem)
    }

    private fun observeClickType() {
        val dataObserver = Observer<CharacterClickData> { data ->
            if (data.clickType == ClickCharacterType.OPEN_DETAILS) {
                startDetailActivity(data)
            }
        }

        searchViewModel.clickData.observe(this, dataObserver)
    }

    private fun startDetailActivity(clickData: CharacterClickData) {
        intent = CharacterDetailActivity.getIntent(this, clickData.character.mapToDetail())
        val options = clickData.transitionImage?.let {
            ActivityOptions.makeSceneTransitionAnimation(
                this,
                it,
                clickData.character.id.toString()
            )
        }
        options?.let {
            startActivity(intent, it.toBundle())
        } ?: run {
            startActivity(intent)
        }
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SearchHeroeActivity::class.java)
    }

}