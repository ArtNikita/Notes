package ru.nikky.notes.ui.pages.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.nikky.notes.databinding.FragmentEditNoteBinding
import ru.nikky.notes.domain.NoteEntity

class EditNoteFragment : Fragment() {
    private var binding: FragmentEditNoteBinding? = null
    private var isNewNote = false
    private var inputNoteEntity: NoteEntity? = null
    val currentNoteId: Int
        get() = if (inputNoteEntity == null) -1 else inputNoteEntity!!.id

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is Contract) { "Launcher activity must implement EditNoteFragment.Contract" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        processInputArguments()
    }

    private fun setupListeners() {
        binding!!.saveNoteButton.setOnClickListener { contractActivity.saveResult(note) }
    }

    private val note: NoteEntity?
        get() {
            val titleText = binding!!.titleEditText.text.toString().trim { it <= ' ' }
            val detailText = binding!!.detailEditText.text.toString().trim { it <= ' ' }
            val outputNoteEntity = if (isNewNote) {
                NoteEntity(titleText, detailText)
            } else {
                inputNoteEntity!!.title = titleText
                inputNoteEntity!!.detail = detailText
                inputNoteEntity
            }
            return outputNoteEntity
        }

    private fun processInputArguments() {
        inputNoteEntity = requireArguments()[KEY_NOTE_ENTITY] as NoteEntity?
        isNewNote = inputNoteEntity == null
        if (!isNewNote) {
            binding!!.titleEditText.setText(inputNoteEntity!!.title)
            binding!!.detailEditText.setText(inputNoteEntity!!.detail)
        }
    }

    interface Contract {
        fun saveResult(noteEntity: NoteEntity?)
    }

    private val contractActivity: Contract
        get() = activity as Contract

    companion object {
        const val KEY_NOTE_ENTITY = "KEY_NOTE_ENTITY"

        @JvmStatic
        fun newInstance(noteEntity: NoteEntity?): EditNoteFragment {
            val editNoteFragment = EditNoteFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_NOTE_ENTITY, noteEntity)
            editNoteFragment.arguments = bundle
            return editNoteFragment
        }
    }
}