package com.kou.bookManagement.repository

import com.kou.bookManagement.domain.model.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AuthorRepository: JpaRepository<Author, Int> {
    fun findByNameAndBirthDate(name: String, birthDate: LocalDate): Author
    fun findByName(name: String): Author
}