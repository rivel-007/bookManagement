package com.kou.bookManagement.domain.model

import jakarta.persistence.*
import java.time.LocalDate
import jakarta.validation.constraints.Past

@Entity
data class Author (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    //著者名
    val name: String,
    //生年月日
    @field:Past(message = "生年月日は現在の日付より過去である必要があります。")
    val birthDate: LocalDate,
    //執筆本
    @OneToMany(mappedBy = "author", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val books: List<Book> = listOf()
)