package com.kou.bookManagement.domain.service

import com.kou.bookManagement.domain.dto.BookRequest
import com.kou.bookManagement.domain.model.Author
import com.kou.bookManagement.domain.model.Book
import com.kou.bookManagement.repository.AuthorRepository
import com.kou.bookManagement.repository.BookRepository
import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate
import java.util.*

@SpringBootTest
class BookServiceTest {

    @MockBean
    lateinit var bookRepository: BookRepository

    @MockBean
    lateinit var authorRepository: AuthorRepository

    @Autowired
    lateinit var bookService: BookService

    @Test
    //正常系
    fun insertBook_test001() {
        // テスト用の著者データ
        val author = Author(name = "Author Name",
                            birthDate = LocalDate.of(1980, 1, 1))
        val authorInfo = Pair("Author 1", LocalDate.of(2000, 1, 1))

        // テスト用の書籍データ
        val bookRequest = BookRequest(
            title = "New Book",
            price = 1000,
            authors = listOf(authorInfo),
            published = true
        )

        // モックの設定
        `when`(authorRepository.findByNameAndBirthDate(authorInfo.first, authorInfo.second )).thenReturn(author)
        `when`(bookRepository.save(any(Book::class.java))).thenReturn(
            Book(id = 1, title = bookRequest.title, price = bookRequest.price, authors = listOf(author), published = bookRequest.published)
        )

        val insertBook = bookService.insertBook(bookRequest)

        // 結果の確認
        assertNotNull(insertBook)
        assertEquals(bookRequest.title, insertBook.title)
        assertEquals(bookRequest.price, insertBook.price)
        assertTrue(insertBook.published)
        verify(bookRepository, times(1)).save(any(Book::class.java))
    }

    @Test
    //正常系
    fun updatedBook_test001() {
        // テスト用の著者データ
        val author = Author(name = "Author Name", birthDate = LocalDate.of(1980, 1, 1))
        val authorInfo = Pair("Author 1", LocalDate.of(2000, 1, 1))
        // テスト用の書籍データ
        val existBook = Book(id = 1, title = "Old Book", price = 1000, authors = listOf(author), published = true)
        val id = 1

        val bookRequest = BookRequest(
            title = "Updated Book",
            price = 1500,
            authors = listOf(Pair(authorInfo.first, authorInfo.second)),
            published = true
        )

        // モックの設定
        `when`(bookRepository.findById(1)).thenReturn(Optional.of(existBook))
        `when`(bookRepository.save(any(Book::class.java))).thenReturn(existBook.copy(
            title = bookRequest.title,
            price = bookRequest.price,
            published = bookRequest.published
        ))

        val updatedBook = bookService.updateBook(bookRequest,id)

        // 結果の確認
        assertEquals(bookRequest.title, updatedBook.title)
        assertEquals(bookRequest.price, updatedBook.price)
        assertEquals(bookRequest.published, updatedBook.published)
        verify(bookRepository, times(1)).save(any(Book::class.java))
    }

    @Test
    //異常系　該当するIDの書籍が存在しない場合
    fun updatedBook_testException001() {
        // テスト用の著者データ
        val authorInfo = Pair("Author 1", LocalDate.of(2000, 1, 1))
        // 存在しない書籍のID
        val noExistId = 999

        val bookRequest = BookRequest(
            title = "Updated Book",
            price = 1500,
            authors = listOf(Pair(authorInfo.first, authorInfo.second)),
            published = true
        )

        // モックの設定
        `when`(bookRepository.findById(noExistId)).thenReturn(Optional.empty())

        // 例外が発生することを確認
        assertThrows(EntityNotFoundException::class.java) {
            bookService.updateBook(bookRequest, noExistId)
        }
    }

    @Test
    //異常系　出版状況を出版済みから未出版に変える場合
    fun updatedBook_testException002() {
        // テスト用の著者データ
        val author = Author(name = "Author Name", birthDate = LocalDate.of(1980, 1, 1))
        val authorInfo = Pair("Author 1", LocalDate.of(2000, 1, 1))
        // テスト用の書籍データ
        val existBook = Book(id = 1, title = "Old Book", price = 1000, authors = listOf(author), published = true)

        //更新する書籍　出版状況をfalseに設定
        val bookRequest = BookRequest(
            title = "Updated Book",
            price = 1500,
            authors = listOf(Pair(authorInfo.first, authorInfo.second)),
            published = false
        )

        // モックの設定
        `when`(bookRepository.findById(1)).thenReturn(Optional.of(existBook))

        // 例外が発生することを確認
        assertThrows(IllegalStateException::class.java) {
            bookService.updateBook(bookRequest, 1)
        }
    }
}