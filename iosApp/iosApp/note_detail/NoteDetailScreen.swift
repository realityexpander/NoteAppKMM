//
//  NoteDetailScreen.swift
//  iosApp
//

import shared
import SwiftUI

struct NoteDetailScreen: View {
    private var noteDataSource: NoteDataSource
    private var noteId: Int64?

    @StateObject var viewModel = NoteDetailViewModel(noteDataSource: nil) // start with nil datasource

    @Environment(\.presentationMode) var presentation  // @Environment similar to Android Context, presentationMode has backstack, for popping the backstack below

    init(noteDataSource: NoteDataSource, noteId: Int64? = nil) {
        self.noteDataSource = noteDataSource
        self.noteId = noteId
    }

    var body: some View {
        VStack(alignment: .leading) {
            TextField("Enter a title...", text: $viewModel.noteTitle)
                .font(.title)
            TextField("Enter some content...", text: $viewModel.noteContent)
            Spacer()
        }.toolbar(content: {
            Button(action: {
                viewModel.saveNote {
                    self.presentation.wrappedValue.dismiss()  // similar to popping the backstack
                }
            }) {
                Image(systemName: "checkmark")
            }
        })
        .padding()
        .background(Color(hex: viewModel.noteColor))
        .onAppear {
            viewModel.setNoteDataSourceAndLoadNote(
                noteDataSource: noteDataSource,
                noteId: noteId
            )
        }
    }
}

struct NoteDetailScreen_Previews: PreviewProvider {
    static var previews: some View {
        NoteDetailScreen(noteDataSource: DatabaseModule().noteDataSource, noteId: -2)
    }
}

struct NoteDetailScreen_Previews2: PreviewProvider {
    static var previews: some View {
        NoteDetailScreen(noteDataSource: DatabaseModule().noteDataSource, noteId: nil)
    }
}
