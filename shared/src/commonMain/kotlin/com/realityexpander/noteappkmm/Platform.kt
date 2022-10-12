package com.realityexpander.noteappkmm

import com.realityexpander.noteappkmm.presentation.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class CommonParcelize()

expect interface CommonParcelable

//---------------------------------

//@OptIn(ExperimentalMultiplatform::class)
//@OptionalExpectation
//@Target(
//    AnnotationTarget.CLASS,
//    AnnotationTarget.PROPERTY
//)
//@Retention(AnnotationRetention.BINARY)
//expect annotation class CommonTypeParceler<T, P : CommonParceler<in T>>()
////expect annotation class CommonTypeParceler<T, P>()

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY
)
expect annotation class CommonTypeParceler<T, P : CommonParceler<in T>>()

expect interface CommonParceler<T>
expect object LocalDateTimeParceler: CommonParceler<LocalDateTime>
