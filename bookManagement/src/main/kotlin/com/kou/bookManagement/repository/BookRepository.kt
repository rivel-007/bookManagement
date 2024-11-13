package com.kou.bookManagement.repository

import com.kou.bookManagement.domain.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Int> {
    fun findByTitleAndPriceAndPublished(title: String, price: Int, published: Boolean): Book
}