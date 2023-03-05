package com.hhh.voiceappvk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.hhh.voiceappvk.data.room.model.AudioNote
import com.hhh.voiceappvk.data.room.viewmodel.RoomViewModel
import com.hhh.voiceappvk.databinding.FragmentEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.UUID


@AndroidEntryPoint
class EditNoteFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val mBinding get() = _binding!!

    private var currentNameNote: TextView? = null
    private var editNoteText: TextInputEditText? = null
    private var deleteNoteButton: Button? = null
    private var saveNoteButton: Button? = null

    private val viewModelRoom by viewModels<RoomViewModel>()
    private val bundleArgs by navArgs<EditNoteFragmentArgs>()
    private var currentNote: AudioNote? = null
    private var duration: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentNote = bundleArgs.note
        duration = bundleArgs.duration
        editNoteText = mBinding.editNoteText

        currentNameNote = mBinding.currentNameNote
        if (currentNote != null) {
            currentNameNote?.text = currentNote?.title
            editNoteText?.setText(currentNote?.title)
        }

        deleteNoteButton = mBinding.deleteNoteButton
        deleteNoteButton?.setOnClickListener {
            if (currentNote == null) {
                val cache = File(requireActivity().cacheDir, "my_file_audioVK")
                cache.delete()
                dismiss()
            } else {
                viewModelRoom.deleteAudioNote(currentNote!!)
                File(currentNote?.filePath.toString()).delete()
                dismiss()
            }
        }

        saveNoteButton = mBinding.saveNoteButton
        saveNoteButton?.setOnClickListener {
            if (currentNote == null) {

                val file = File(requireActivity().filesDir, UUID.randomUUID().toString())
                val cache = File(requireActivity().cacheDir, "my_file_audioVK")
                cache.copyTo(file)
                cache.delete()

                val currentTitle = editNoteText?.text?.trim().toString()
                val dateTime = System.currentTimeMillis() / 1000

                viewModelRoom.insertAudioNote(note = AudioNote(
                    dateTime = dateTime,
                    duration = duration ?: "",
                    filePath = file.path,
                    title = currentTitle
                ))

                dismiss()
            } else {
                currentNote?.title = editNoteText?.text.toString()
                viewModelRoom.updateAudioNote(currentNote!!)
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}