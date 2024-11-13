package com.kou.bookManagement.controller

import com.kou.bookManagement.domain.dto.AuthorRequest
import com.kou.bookManagement.domain.dto.BookRequest
import com.kou.bookManagement.domain.model.Author
import com.kou.bookManagement.domain.model.Book
import com.kou.bookManagement.domain.service.AuthorService
import com.kou.bookManagement.domain.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/author")
class AuthorController(private val authorService: AuthorService) {

    @PostMapping
    fun insertBook(@RequestBody request: AuthorRequest): ResponseEntity<Author> {
        val book = authorService.insertAuthor(request)
        return ResponseEntity.ok(book)
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Int, @RequestBody request: AuthorRequest): ResponseEntity<Author> {
        val book = authorService.updateAuthor(request,id)
        return ResponseEntity.ok(book)
    }
}