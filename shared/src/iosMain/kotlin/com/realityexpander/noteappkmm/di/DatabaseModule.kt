package com.realityexpander.noteappkmm.di

import com.realityexpander.noteappkmm.data.local.DatabaseDriverFactory
import com.realityexpander.noteappkmm.data.note.SqlDelightNoteDataSource
import com.realityexpander.noteappkmm.database.NoteDatabase
import com.realityexpander.noteappkmm.domain.note.NoteDataSource

// Provide a database instance for iOS

class DatabaseModule {

    private val factory by lazy { DatabaseDriverFactory() }

    val noteDataSource: NoteDataSource by lazy {
        SqlDelightNoteDataSource(NoteDatabase(factory.createDriver()))
    }
}