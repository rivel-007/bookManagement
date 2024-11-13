package com.kou.bookManagement.domain.dto

import java.time.LocalDate

data class AuthorRequest (
    val name: String,
    val birthDate: LocalDate,
    //本のタイトル、価格、出版状況を格納
    val books: List<Triple<String, Int, Boolean>>
)