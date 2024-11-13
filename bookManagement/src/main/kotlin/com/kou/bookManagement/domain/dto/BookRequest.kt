package com.kou.bookManagement.domain.dto

import com.kou.bookManagement.domain.model.Author
import java.time.LocalDate

data class BookRequest (
    val title: String,
    val price: Int,
    //著者名、誕生日を格納
    val authors: List<Pair<String, LocalDate>>,
    val published: Boolean
)
