package com.kou.bookManagement.domain.model

import jakarta.persistence.*
import jakarta.validation.constraints.Min

@Entity
data class Book (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    //タイトル
    val title: String,
    //価格
    @field:Min(0)
    val price: Int,
    //著者
    val authors: List<Author> = listOf(),
    //出版状況
    val published: Boolean
)