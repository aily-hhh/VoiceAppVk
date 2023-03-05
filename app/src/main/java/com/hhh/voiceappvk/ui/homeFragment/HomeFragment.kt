package com.hhh.voiceappvk.ui.homeFragment

import android.Manifest
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hhh.voiceappvk.R
import com.hhh.voiceappvk.util.audiorecorder.record.AudioRecorder
import com.hhh.voiceappvk.data.room.model.AudioNote
import com.hhh.voiceappvk.data.room.viewmodel.RoomViewModel
import com.hhh.voiceappvk.databinding.FragmentHomeBinding
import com.vk.api.sdk.VK
import dagger.hilt.android.AndroidEntryPoint
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

    private val viewModelRoom by viewModels<RoomViewModel>()

    private val recorder by lazy {
        AudioRecorder(requireActivity().applicationContext)
    }
    private var audioFile: File? = null
    private var flag = true
    private var duration: String? = null

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

        if (!VK.isLoggedIn()) {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_homeFragment_to_loginFragment)
        }

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
                val bundle = Bundle()
                bundle.putString("duration", duration)
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_homeFragment_to_editNoteFragment, bundle)
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
    }

    private fun initAdapter() {
        homeAdapter = HomeAdapter()
        recyclerView?.apply {
            adapter = homeAdapter
            layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        }
    }

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
        duration = tickingRecord?.text.toString()
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