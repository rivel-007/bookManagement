package com.kou.bookManagement.controller

import com.kou.bookManagement.domain.dto.BookRequest
import com.kou.bookManagement.domain.model.Book
import com.kou.bookManagement.domain.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/book")
class BookController(private val bookService: BookService) {

    @PostMapping
    fun insertBook(@RequestBody request: BookRequest): ResponseEntity<Book> {
        val book = bookService.insertBook(request)
        return ResponseEntity.ok(book)
    }

    @PutMapping("/{id}")
    fun updateBook(@PathVariable id: Int, @RequestBody request: BookRequest): ResponseEntity<Book> {
        val book = bookService.updateBook(request,id)
        return ResponseEntity.ok(book)
    }

    @GetMapping("/{byAuthor}")
    fun selectBook(@PathVariable authorName: String): List<Book>{
        return bookService.selectBookByAuthor(authorName)
    }
}