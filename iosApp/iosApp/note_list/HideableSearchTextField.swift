//
//  HideableSearchTextField.swift
//  iosApp


import SwiftUI

struct HideableSearchTextField<Destination: View>: View {
    
    var onSearchToggled: () -> Void
    var destinationProvider: () -> Destination
    var isSearchActive: Bool
    @Binding var searchText: String  // @Binding makes a two-way binding reflects updates to UI
    
    var body: some View {
        HStack {
            TextField("Search...", text: $searchText)  // $searchText means use 2-way @Binding with `searchText`
                .textFieldStyle(.roundedBorder)
                .opacity(isSearchActive ? 1 : 0)  // change opacity to allow TextView to take up space
            if !isSearchActive {
                Spacer()  // use the remaining space
            }
            Button(action: onSearchToggled) {
                Image(systemName: isSearchActive ? "xmark" : "magnifyingglass")
                    .foregroundColor(.black)
            }
            NavigationLink(destination: destinationProvider()) {
                Image(systemName: "plus")
                    .foregroundColor(.black)
            }
        }
    }
}

struct HideableSearchTextField_Previews: PreviewProvider {
    static var previews: some View {
        HideableSearchTextField(
            onSearchToggled: {},
            destinationProvider: { EmptyView() },
            isSearchActive: true,
            searchText: .constant("YouTube")
        )
    }
}
