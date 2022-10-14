import SwiftUI
import shared

// anything defined in the iOSApp struct will be a singleton

@main
struct iOSApp: App {
    
    public let databaseModule = DatabaseModule()
    
	var body: some Scene {
        WindowGroup {
            NavigationView {
                NoteListScreen(noteDataSource: databaseModule.noteDataSource)
            }.accentColor(.black)
		}
	}
}
