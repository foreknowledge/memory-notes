package com.foreknowledge.cleanarchitectureex.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.foreknowledge.cleanarchitectureex.R
import com.foreknowledge.cleanarchitectureex.framework.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private val noteListAdapter = NoteListAdapter(mutableListOf())
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteListAdapter
        }

        addNote.setOnClickListener { goToNoteDetails() }

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllNotes()
    }

    private fun observeViewModel() {
        viewModel.noteList.observe(viewLifecycleOwner, Observer { noteList ->
            loadingView.visibility = View.INVISIBLE
            notesListView.visibility = View.VISIBLE
            noteListAdapter.updateNotes(noteList.sortedByDescending { it.updateTime })
        })
    }

    private fun goToNoteDetails(noteId: Long = 0L) {
        val action = ListFragmentDirections.actionGoToNote(noteId)
        Navigation.findNavController(notesListView).navigate(action)
    }

}
