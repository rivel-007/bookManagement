package com.kou.bookManagement.domain.service

import com.kou.bookManagement.domain.dto.BookRequest
import com.kou.bookManagement.domain.model.Author
import com.kou.bookManagement.domain.model.Book
import com.kou.bookManagement.repository.AuthorRepository
import com.kou.bookManagement.repository.BookRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class BookService(private val bookRepository: BookRepository, private val authorRepository: AuthorRepository) {
    //書籍登録
    fun insertBook(request: BookRequest) : Book{
        //リクエストの情報から著者を抽出
        val authors = selectAuthor(request.authors)
        val book = Book(title = request.title, price = request.price, authors = authors, published = request.published)
        //登録実行
        return bookRepository.save(book)
    }

    //書籍更新
    fun updateBook(request: BookRequest, id: Int) : Book{
        //更新対象の書籍を取得
        val targetBook = bookRepository.findById(id).orElseThrow {
            throw EntityNotFoundException("該当するIDの書籍は見つかりませんでした。: $id")
        }
        // 書籍を出版済みから未出版にする処理は終了させる
        if (targetBook.published && !request.published) {
            throw IllegalStateException("出版状況を出版済みから未出版に変更することはできません。")
        }
        //リクエストの情報から著者を抽出
        val authors = selectAuthor(request.authors)
        val book = targetBook.copy(title = request.title, price = request.price, authors = authors, published = request.published)
        //更新実行
        return bookRepository.save(book)
    }

    //書籍検索
    fun selectBookByAuthor(name: String): List<Book>{
        //リクエストの情報から著者を抽出
        val authors = authorRepository.findByName(name)
        return authors.books
    }

    //著者名前、生年月日から著者の情報を抽出
    fun selectAuthor(authorsInfo: List<Pair<String, LocalDate>>): List<Author>{
        return authorsInfo.map { (name, birthDate) ->
            authorRepository.findByNameAndBirthDate(name, birthDate)
        }
    }
}