package com.hhh.voiceappvk.ui.homeFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hhh.voiceappvk.data.HomeViewModel
import com.hhh.voiceappvk.databinding.FragmentHomeBinding
import com.hhh.voiceappvk.util.UiState
import com.vk.api.sdk.VK
import com.vk.sdk.api.docs.DocsService
import com.vk.sdk.api.docs.dto.DocsDoc
import com.vk.sdk.api.docs.dto.TypeParam
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var startRecordFab: FloatingActionButton? = null
    private var homeProgressBar: ProgressBar? = null
    private var homeAdapter: HomeAdapter? = null

    private val viewModelHome by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = mBinding.recyclerView
        startRecordFab = mBinding.startRecordFab
        homeProgressBar = mBinding.homeProgressBar
        initAdapter()
        homeAdapter?.setClickListener(clickListener = object: ItemClickListener {
            override fun onItemClickListener(doc: DocsDoc) {
                Log.d("checkData", doc.toString())
            }

            override fun onItemLongClickListener(doc: DocsDoc, view: View) {
                Log.d("checkData", doc.toString())
            }
        })

        viewModelHome.getFiles.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    homeProgressBar?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    homeProgressBar?.visibility = View.INVISIBLE
                    homeAdapter?.setDiffer(it.data!!)
                }
                is UiState.Error -> {
                    homeProgressBar?.visibility = View.INVISIBLE
                }
                else -> {
                    homeProgressBar?.visibility = View.INVISIBLE
                }
            }
        }

        viewModelHome.getFiles()
    }

    private fun initAdapter() {
        homeAdapter = HomeAdapter()
        recyclerView?.apply {
            adapter = homeAdapter
            layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}