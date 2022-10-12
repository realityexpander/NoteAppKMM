package com.realityexpander.noteappkmm.domain.note

// SQLDelight also can return a flow, but these dont work on iOS without extra setup

interface NoteDataSource {
    suspend fun insertNote(note: Note)
    suspend fun getNoteById(id: Long): Note?
    suspend fun getAllNotes(): List<Note>
    suspend fun deleteNoteById(id: Long)
}