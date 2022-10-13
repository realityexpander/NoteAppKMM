package com.realityexpander.noteappkmm

import com.realityexpander.noteappkmm.presentation.*
import kotlinx.datetime.LocalDateTime
import kotlinx.wasm.jsinterop.Object

// Note: no need to define CommonParcelize here (bc its @OptionalExpectation)
actual interface CommonParcelable  // not used on iOS
actual annotation class CommonTypeParceler<T, P : CommonParceler<in T>>  // not used on iOS
actual interface CommonParceler<T> // not used on iOS
actual object LocalDateTimeParceler : CommonParceler<LocalDateTime> // not used on iOS