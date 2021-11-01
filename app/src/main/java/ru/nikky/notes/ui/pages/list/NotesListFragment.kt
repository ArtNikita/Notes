package ru.nikky.notes.ui.pages.list

import android.content.Context
import ru.nikky.notes.ui.pages.list.NotesAdapter
import ru.nikky.notes.domain.NoteEntity
import ru.nikky.notes.domain.NotesRepo
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.nikky.notes.R
import com.google.android.material.snackbar.Snackbar
import ru.nikky.notes.App
import androidx.recyclerview.widget.LinearLayoutManager
import ru.nikky.notes.databinding.FragmentNotesListBinding

class NotesListFragment : Fragment() {
    private val listener: NotesAdapter.OnItemClickListener = object : NotesAdapter.OnItemClickListener {
        override fun onItemClick(noteEntity: NoteEntity) {
            contractActivity!!.noteItemPressed(noteEntity)
        }

        override fun onItemLongClick(noteEntity: NoteEntity, anchorView: View) {
            contractActivity!!.noteItemPressedLong(noteEntity, anchorView)
        }
    }
    private var binding: FragmentNotesListBinding? = null
    private var adapter: NotesAdapter? = null
    private var notesRepo: NotesRepo? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is Contract) { "Launcher activity must implement NotesListFragment.Contract" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNotesListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        setupAddNoteFloatingActionButton()
        initNotesRepo()
        initRecyclerView()
    }

    private fun initToolBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding!!.notesListActivityToolbar)
    }

    private fun setupAddNoteFloatingActionButton() {
        binding!!.addNoteFloatingActionButton.setOnClickListener { v: View? -> addNoteButtonPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.notes_list_activity_toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings_menu) {
            settingsButtonPressed()
            return true
        }
        if (item.itemId == R.id.about_menu) {
            aboutButtonPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun receiveNoteEntity(noteEntity: NoteEntity) {
        if (noteEntity.id == NoteEntity.NEW_NOTE_ENTITY_ID) {
            processNewEditedEntity(noteEntity)
        } else {
            processEditedEntity(noteEntity)
        }
    }

    fun notifyUserThatNoteWasDeleted(noteEntity: NoteEntity) {
        Snackbar.make(binding!!.addNoteFloatingActionButton, getString(R.string.note_was_deleted_snackbar_text), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo_snackbar_action_text) { v: View? -> receiveNoteEntity(noteEntity) }
                .setBackgroundTint(requireContext().getColor(R.color.yellow))
                .setTextColor(requireContext().getColor(R.color.pink))
                .setActionTextColor(requireContext().getColor(R.color.pink))
                .show()
    }

    private fun processNewEditedEntity(noteEntity: NoteEntity) {
        if (noteEntity.isEmpty) return
        notesRepo!!.add(noteEntity.title, noteEntity.detail)
        adapter!!.setData(notesRepo!!.notes)
    }

    private fun processEditedEntity(noteEntity: NoteEntity) {
        if (noteEntity.isEmpty) {
            notesRepo!!.delete(noteEntity.id)
        } else {
            notesRepo!!.update(noteEntity.id, noteEntity.title, noteEntity.detail)
        }
        adapter!!.setData(notesRepo!!.notes)
    }

    private fun addNoteButtonPressed() {
        contractActivity!!.addNoteButtonPressed()
    }

    private fun settingsButtonPressed() {
        contractActivity!!.settingsButtonPressed()
    }

    private fun aboutButtonPressed() {
        contractActivity!!.aboutButtonPressed()
    }

    private fun initNotesRepo() {
        notesRepo = app.notesRepo
    }

    private val app: App
        private get() = requireActivity().application as App

    private fun initRecyclerView() {
        binding!!.notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotesAdapter()
        adapter!!.setOnItemClickListener(listener)
        binding!!.notesRecyclerView.adapter = adapter
        adapter!!.setData(notesRepo!!.notes)
    }

    private val contractActivity: Contract?
        private get() = activity as Contract?

    interface Contract {
        fun addNoteButtonPressed()
        fun noteItemPressed(noteEntity: NoteEntity?)
        fun settingsButtonPressed()
        fun aboutButtonPressed()
        fun noteItemPressedLong(noteEntity: NoteEntity?, anchorView: View?)
    }
}