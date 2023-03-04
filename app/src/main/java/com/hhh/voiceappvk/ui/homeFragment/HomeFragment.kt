package com.hhh.voiceappvk.ui.homeFragment

import android.Manifest
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hhh.voiceappvk.R
import com.hhh.voiceappvk.util.audiorecorder.record.AudioRecorder
import com.hhh.voiceappvk.data.home.HomeViewModel
import com.hhh.voiceappvk.data.room.model.AudioNote
import com.hhh.voiceappvk.data.room.viewmodel.RoomViewModel
import com.hhh.voiceappvk.data.vk.VkModel
import com.hhh.voiceappvk.databinding.FragmentHomeBinding
import com.hhh.voiceappvk.ui.EditNoteFragment
import com.hhh.voiceappvk.util.UiState
import com.vk.api.sdk.VK
import com.vk.sdk.api.docs.DocsService
import com.vk.sdk.api.docs.dto.DocsDoc
import com.vk.sdk.api.docs.dto.TypeParam
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.internal.http.promisesBody
import java.io.File


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    private var startRecordFab: FloatingActionButton? = null
    private var cancelRecordFab: ImageView? = null
    private var tickingRecord: Chronometer? = null
    private var homeAdapter: HomeAdapter? = null

    private val viewModelHome by viewModels<HomeViewModel>()
    private val viewModelRoom by viewModels<RoomViewModel>()

    private val recorder by lazy {
        AudioRecorder(requireActivity().applicationContext)
    }
    private var audioFile: File? = null
    private var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startRecordFab?.focusable = View.NOT_FOCUSABLE
            }
            else {
                startRecordFab?.focusable = View.FOCUSABLE
            }
        }

        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

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

        initAdapter()
        homeAdapter?.setClickListener(clickListener = object: ItemClickListener {
            override fun onItemLongClickListener(note: AudioNote, view: View) {
                val bundle = Bundle()
                bundle.putParcelable("note", note)
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_homeFragment_to_editNoteFragment, bundle)
            }
        })

        startRecordFab = mBinding.startRecordFab
        startRecordFab?.setOnClickListener {
            if (flag) {
                startRecord()
            } else {
                stopRecord()
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_homeFragment_to_editNoteFragment)
            }
        }

        cancelRecordFab = mBinding.cancelRecordFab
        cancelRecordFab?.setOnClickListener {
            stopRecord()
            File(requireActivity().cacheDir, "my_file_audioVK").delete()
        }

        viewModelRoom.allAudioNotes.observe(viewLifecycleOwner) {
            homeAdapter?.setDiffer(it)
        }

//        observeViewModel()
//        viewModelHome.getFiles()
    }

    private fun initAdapter() {
        homeAdapter = HomeAdapter()
        recyclerView?.apply {
            adapter = homeAdapter
            layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        }
    }

//    private fun observeViewModel() {
//        viewModelHome.getFiles.observe(viewLifecycleOwner) {
//            when (it) {
//                is UiState.Loading -> {
//                    homeProgressBar?.visibility = View.VISIBLE
//                }
//                is UiState.Success -> {
//                    homeProgressBar?.visibility = View.INVISIBLE
//                    homeAdapter?.setDiffer(it.data!!)
//                }
//                is UiState.Error -> {
//                    homeProgressBar?.visibility = View.INVISIBLE
//                }
//                else -> {
//                    homeProgressBar?.visibility = View.INVISIBLE
//                }
//            }
//        }
//    }

    private fun startRecord() {
        tickingRecord = mBinding.tickingRecord
        flag = false
        startRecordFab?.setImageResource(R.drawable.ic_done)
        tickingRecord?.base = SystemClock.elapsedRealtime()
        tickingRecord?.start()
        tickingRecord?.visibility = View.VISIBLE
        cancelRecordFab?.visibility = View.VISIBLE
        File(requireActivity().cacheDir, "my_file_audioVK").also {
            recorder.start(it)
            audioFile = it
        }
    }

    private fun stopRecord() {
        flag = true
        tickingRecord?.stop()
        recorder.stop()
        tickingRecord?.visibility = View.GONE
        cancelRecordFab?.visibility = View.GONE
        tickingRecord = null
        startRecordFab?.setImageResource(R.drawable.ic_voice_start)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}