//
//  NoteListScreen.swift
//  iosApp

import SwiftUI
import shared

struct NoteListScreen: View {
    private var noteDataSource: NoteDataSource
    @StateObject var viewModel = NoteListViewModel(noteDataSource: nil)
    
    @State private var isNoteSelected = false
    @State private var selectedNoteId: Int64? = nil
    
    init(noteDataSource: NoteDataSource) {
        self.noteDataSource = noteDataSource
    }
    
    var body: some View {
        VStack {
            ZStack {
                NavigationLink(destination: NoteDetailScreen(noteDataSource: self.noteDataSource, noteId: selectedNoteId), isActive: $isNoteSelected) {
                    EmptyView()
                }.hidden()
                HideableSearchTextField<NoteDetailScreen>(onSearchToggled: {
                    viewModel.toggleIsSearchActive()
                }, destinationProvider: {
                    NoteDetailScreen(
                        noteDataSource: noteDataSource,
                        noteId: selectedNoteId
                    )
                }, isSearchActive: viewModel.isSearchActive, searchText: $viewModel.searchText)
                .frame(maxWidth: .infinity, minHeight: 40)
                .padding()

                if !viewModel.isSearchActive {
                    Text("All notes")
                        .font(.title2)
                }
            }
            
            List {
                ForEach(viewModel.filteredNotes, id: \.self.id) { note in
                    Button(action: {
                        isNoteSelected = true
                        selectedNoteId = note.id?.int64Value
                    }) {
                        NoteItem(note: note, onDeleteClick: {
                            viewModel.deleteNoteById(id: note.id?.int64Value)
                        })
                    }
                }
            }
            .onAppear {
                viewModel.loadNotes()
            }
            .listStyle(.plain)
            .listRowSeparator(.hidden)
        }
        .onAppear {
            viewModel.setNoteDataSource(noteDataSource: noteDataSource)
        }
        .navigationBarHidden(true)
        .hoverEffect(/*@START_MENU_TOKEN@*/.highlight/*@END_MENU_TOKEN@*/)
    }
    
}

func fundonothing() {
    print("hello")
}

struct NoteListScreen_Previews: PreviewProvider {
    
    static var previews: some View {
        VStack {
        List {
            ForEach(0..<10) { note in
                Button(action: {}
                ) {
                    NoteItem(note: Note(
                        id: 1,
                        title: "hello " + String(note),
                        content: "goodbye",
                        colorHex: Int64(8454748548 * note),
                        created: Kotlinx_datetimeLocalDateTime.init(
                            date: Kotlinx_datetimeLocalDate(
                                year: 2022,
                                monthNumber: 10,
                                dayOfMonth: 9),
                            time: Kotlinx_datetimeLocalTime(
                                hour: 5,
                                minute: 32,
                                second: 10,
                                nanosecond: 0)
                        )
                    ),
                    onDeleteClick: { fundonothing() }
                )
                }
            }
        }
            Spacer()
        }
    }
}
