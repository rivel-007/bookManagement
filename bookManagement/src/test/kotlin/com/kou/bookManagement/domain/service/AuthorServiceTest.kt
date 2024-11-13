package com.kou.bookManagement.domain.service

import com.kou.bookManagement.domain.dto.AuthorRequest
import com.kou.bookManagement.domain.model.Author
import com.kou.bookManagement.domain.model.Book
import com.kou.bookManagement.repository.AuthorRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.ConstraintViolation
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDate
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertThrows

@SpringBootTest
class AuthorServiceTest {

    private lateinit var validator: Validator

    @MockBean
    lateinit var authorRepository: AuthorRepository

    @Autowired
    lateinit var authorService: AuthorService

    @BeforeEach
    fun setup() {
        val factory = Validation.buildDefaultValidatorFactory()
        validator = factory.validator
    }

    @Test
    //正常系
    fun insertAuthor_test001() {
        // テスト用の書籍データ
        val bookInfo = Triple("Book 1", 1, true)
        val book = Book(title = bookInfo.first, price = bookInfo.second, published = bookInfo.third)

        val request = AuthorRequest (
            name = "Test Author",
            birthDate = LocalDate.of(2000, 1, 1),
            books = listOf(bookInfo)
        )
        // モックの動作定義
        `when`(authorRepository.save(any(Author::class.java))).thenReturn(
            Author(id = 1, name = request.name, birthDate = request.birthDate, books = listOf(book) )
        )

        // サービスメソッドの呼び出し
        val savedAuthor = authorService.insertAuthor(request)

        // 結果の確認
        assertNotNull(savedAuthor)
        assertEquals(1, savedAuthor.id)
        assertEquals(request.name, savedAuthor.name)
        assertEquals(request.birthDate, savedAuthor.birthDate)
        assertEquals(request.books[0].first, savedAuthor.books[0].title)
        assertEquals(request.books[0].second, savedAuthor.books[0].price)
        assertEquals(request.books[0].third, savedAuthor.books[0].published)
        verify(authorRepository, times(1)).save(any(Author::class.java))
    }

    @Test
    //正常系
    fun updateAuthor_test001() {
        // テスト用の既存著者と書籍データ
        val existAuthor = Author(
            id = 1,
            name = "Old Author",
            birthDate = LocalDate.of(2000, 1, 1),
            books = listOf(Book(title = "Book 01", price = 10000, published = true))
        )

        // 更新リクエスト用の書籍データ（タイトル、価格、出版状況）
        val bookInfo = Triple("Update Book", 1111, true)

        val request = AuthorRequest(
            name = "Update Author",
            birthDate = LocalDate.of(2000, 10, 10),
            books = listOf(bookInfo)
        )

        // モックの動作定義
        `when`(authorRepository.findById(1)).thenReturn(Optional.of(existAuthor))

        val updatedAuthor = Author(
            id = 1,
            name = request.name,
            birthDate = request.birthDate,
            books = listOf(Book(title = bookInfo.first, price = bookInfo.second, published = bookInfo.third))
        )
        `when`(authorRepository.save(any(Author::class.java))).thenReturn(updatedAuthor)

        // サービスメソッドの呼び出し
        val savedAuthor = authorService.updateAuthor(request, 1)

        // 結果の確認
        assertNotNull(savedAuthor)
        assertEquals(request.name, savedAuthor.name)
        assertEquals(request.birthDate, savedAuthor.birthDate)
        assertEquals(request.books[0].first, savedAuthor.books[0].title)
        assertEquals(request.books[0].second, savedAuthor.books[0].price)
        assertEquals(request.books[0].third, savedAuthor.books[0].published)
        verify(authorRepository, times(1)).save(any(Author::class.java))
    }

    @Test
    //該当するIDの著者が存在しない場合
    fun updatedBook_testException001() {
        // テスト用の著者データ
        val bookInfo = Triple("Book 1", 1, true)
        // 存在しない著者のID
        val noExistId = 999

        val request = AuthorRequest(
            name = "Update Author",
            birthDate = LocalDate.of(2000, 10, 10),
            books = listOf(bookInfo)
        )

        // モックの設定
        `when`(authorRepository.findById(noExistId)).thenReturn(Optional.empty())

        // 例外が発生することを確認
        assertThrows(EntityNotFoundException::class.java) {
            authorService.updateAuthor(request, noExistId)
        }
    }

    @Test
    //異常系 生年月日が未来日付
    fun birthDateCheck_testErr001() {
        val futureDate = LocalDate.now().plusDays(1) // 現在の日付より未来の日付
        val author = Author(
            name = "Future Author",
            birthDate = futureDate
        )

        //バリデーションエラーを確認
        val violations: Set<ConstraintViolation<Author>> = validator.validate(author)
        assertEquals(1, violations.size)
    }
}
