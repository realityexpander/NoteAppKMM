package com.realityexpander.noteappkmm

import com.realityexpander.noteappkmm.presentation.*
import kotlinx.datetime.LocalDateTime

// Note: no need to define CommonParcelize here (bc its @OptionalExpectation)
actual interface CommonParcelable  // not used on iOS

// Note: no need to define CommonTypeParceler<T,P : CommonParceler<in T>> here (bc its @OptionalExpectation)
actual interface CommonParceler<T> // not used on iOS
actual object LocalDateTimeParceler : CommonParceler<LocalDateTime> // not used on iOS