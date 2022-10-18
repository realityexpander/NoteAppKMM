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

    // To use @State in the View (not as elegant), see this: https://stackoverflow.com/a/60058909/2857200
//    @State private var bgColor =
//        Color(.sRGB, red: 0.98, green: 0.9, blue: 0.2)

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
            HStack {
                Spacer()
                ColorPicker(
                    "Color",
                    //selection: $bgColor
                    selection: $viewModel.bgColor
                )
                .frame(maxWidth: 30, minHeight: 40)
                .padding()
                //.onChange(of: bgColor) { newColor in  // using @State in the View (not elegant)
                .onChange(of: viewModel.bgColor) { newColor in
                    let red = UInt((newColor.cgColor?.components?[0] ?? 0) * 255)
                    let green = UInt((newColor.cgColor?.components?[1] ?? 0) * 255)
                    let blue = UInt((newColor.cgColor?.components?[2] ?? 0) * 255)
                    viewModel.noteColor = Int64((red << 16) | (green << 8) | blue)
                }
                
                Button(action: {
                    viewModel.saveNote {
                        self.presentation.wrappedValue.dismiss()  // similar to popping the backstack
                    }
                }) {
                    Image(systemName: "checkmark")
                }
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
        return NavigationView {
            NoteDetailScreen(noteDataSource: DatabaseModule().noteDataSource, noteId: nil)
        }
    }
}
