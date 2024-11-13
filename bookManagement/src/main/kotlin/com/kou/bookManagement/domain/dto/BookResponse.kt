package com.kou.bookManagement.domain.dto

import java.time.LocalDate

data class BookResponse(
    val id: Long,
    val title: String,
    val price: Int,
    val published: Boolean,
    val authors: List<Pair<String, LocalDate>>
)