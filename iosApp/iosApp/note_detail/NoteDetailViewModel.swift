//
//  NoteDetailViewModel.swift
//  iosApp
//
//  Created by Philipp Lackner on 26.09.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

extension NoteDetailScreen {
    @MainActor class NoteDetailViewModel: ObservableObject {
        private var noteDataSource: NoteDataSource?

        private var noteId: Int64?
        @Published var noteTitle = ""
        @Published var noteContent = ""
        @Published private(set) var noteColor = Note.Companion().getRandomNoteColor()

        init(noteDataSource: NoteDataSource? = nil) {
            self.noteDataSource = noteDataSource
        }

        func loadNoteIfExists(id: Int64?) {
            if id != nil {
                noteId = id
                noteDataSource?.getNoteById(id: id!, completionHandler: { note, _ in
                    self.noteTitle = note?.title ?? ""
                    self.noteContent = note?.content ?? ""
                    self.noteColor = note?.colorHex ?? Note.Companion().getRandomNoteColor()
                })
            }
        }

        func saveNote(onSaved: @escaping () -> Void) {
            noteDataSource?.insertNote(
                note: Note(
                    id: noteId == nil ? nil : KotlinLong(value: noteId!),
                    title: noteTitle,
                    content: noteContent,
                    colorHex: noteColor,
                    created: DateTimeUtil().now()),
                completionHandler: { _ in
                    onSaved()
                })
        }

        func setParamsAndLoadNote(noteDataSource: NoteDataSource, noteId: Int64?) {
            self.noteDataSource = noteDataSource
            loadNoteIfExists(id: noteId)
        }
    }
}
