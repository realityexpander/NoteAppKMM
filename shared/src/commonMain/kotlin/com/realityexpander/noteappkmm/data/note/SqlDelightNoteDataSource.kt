package com.realityexpander.noteappkmm.data.note

import com.realityexpander.noteappkmm.database.NoteDatabase
import com.realityexpander.noteappkmm.domain.note.Note
import com.realityexpander.noteappkmm.domain.note.NoteDataSource
import com.realityexpander.noteappkmm.domain.time.DateTimeUtil

// Implements the NoteDataSource interface for SQLDelight database passed in (db)

class SqlDelightNoteDataSource(db: NoteDatabase): NoteDataSource {

    private val queries = db.noteQueries

    override suspend fun insertNote(note: Note) {
        queries.insertNote(
            id = note.id,
            title = note.title,
            content = note.content,
            colorHex = note.colorHex,
            created = DateTimeUtil.toEpochMillis(note.created)
        )
    }

    override suspend fun getNoteById(id: Long): Note? {
        return queries
            // sets the query
            .getNoteById(id)
            // executes the query - returns NoteEntity a first row of query, or null if no rows
            .executeAsOneOrNull()
            ?.toNote()
            .also{}
    }

    override suspend fun getAllNotes(): List<Note> {
        return queries
            .getAllNotes()
            .executeAsList()
            .map {
                it.toNote()
            }
            .also{}
    }

    override suspend fun deleteNoteById(id: Long) {
        queries.deleteNoteById(id)
    }
}