//
//  Colors.swift
//  iosApp

import Foundation
import SwiftUI

// Similar to extension function in Kotlin, this will create a new Color based on the RGBA values passed in (from the Kotlin side as Hex value)
extension Color {
    init(hex: Int64, alpha: Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }
}

