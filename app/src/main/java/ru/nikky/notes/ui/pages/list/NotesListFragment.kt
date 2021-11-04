package ru.nikky.notes.ui.pages.list

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import ru.nikky.notes.App
import ru.nikky.notes.R
import ru.nikky.notes.databinding.FragmentNotesListBinding
import ru.nikky.notes.domain.NoteEntity
import ru.nikky.notes.domain.NotesRepo

class NotesListFragment : Fragment(R.layout.fragment_notes_list) {
    private val listener: NotesAdapter.OnItemClickListener = object : NotesAdapter.OnItemClickListener {
        override fun onItemClick(noteEntity: NoteEntity) {
            contractActivity.noteItemPressed(noteEntity)
        }

        override fun onItemLongClick(noteEntity: NoteEntity, anchorView: View) {
            contractActivity.noteItemPressedLong(noteEntity, anchorView)
        }
    }
    private val binding: FragmentNotesListBinding by viewBinding(FragmentNotesListBinding::bind)
    private lateinit var adapter: NotesAdapter
    private lateinit var notesRepo: NotesRepo
    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is Contract) { "Launcher activity must implement NotesListFragment.Contract" }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initToolBar()
        setupAddNoteFloatingActionButton()
        initNotesRepo()
        initRecyclerView()
    }

    private fun initToolBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.notesListActivityToolbar)
    }

    private fun setupAddNoteFloatingActionButton() {
        binding.addNoteFloatingActionButton.setOnClickListener { addNoteButtonPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.notes_list_activity_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_menu -> {
                settingsButtonPressed()
                true
            }
            R.id.about_menu -> {
                aboutButtonPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun receiveNoteEntity(noteEntity: NoteEntity) {
        if (noteEntity.id == NoteEntity.NEW_NOTE_ENTITY_ID) {
            processNewEditedEntity(noteEntity)
        } else {
            processEditedEntity(noteEntity)
        }
    }

    fun notifyUserThatNoteWasDeleted(noteEntity: NoteEntity) {
        Snackbar.make(binding.addNoteFloatingActionButton, getString(R.string.note_was_deleted_snackbar_text), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_snackbar_action_text) { receiveNoteEntity(noteEntity) }
                .setBackgroundTint(requireContext().getColor(R.color.yellow))
                .setTextColor(requireContext().getColor(R.color.pink))
                .setActionTextColor(requireContext().getColor(R.color.pink))
                .show()
    }

    private fun processNewEditedEntity(noteEntity: NoteEntity) {
        if (noteEntity.isEmpty) return
        notesRepo.add(noteEntity.title, noteEntity.detail)
        adapter.setData(notesRepo.notes)
    }

    private fun processEditedEntity(noteEntity: NoteEntity) {
        if (noteEntity.isEmpty) {
            notesRepo.delete(noteEntity.id)
        } else {
            notesRepo.update(noteEntity.id, noteEntity.title, noteEntity.detail)
        }
        adapter.setData(notesRepo.notes)
    }

    private fun addNoteButtonPressed() {
        contractActivity.addNoteButtonPressed()
    }

    private fun settingsButtonPressed() {
        contractActivity.settingsButtonPressed()
    }

    private fun aboutButtonPressed() {
        contractActivity.aboutButtonPressed()
    }

    private fun initNotesRepo() {
        notesRepo = app.notesRepo
    }

    private val app: App
        get() = requireActivity().application as App

    private fun initRecyclerView() {
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotesAdapter()
        adapter.setOnItemClickListener(listener)
        binding.notesRecyclerView.adapter = adapter
        adapter.setData(notesRepo.notes)
    }

    private val contractActivity: Contract
        get() = activity as Contract

    interface Contract {
        fun addNoteButtonPressed()
        fun noteItemPressed(noteEntity: NoteEntity)
        fun settingsButtonPressed()
        fun aboutButtonPressed()
        fun noteItemPressedLong(noteEntity: NoteEntity, anchorView: View)
    }
}