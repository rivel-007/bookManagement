package com.kou.bookManagement.domain.service

import com.kou.bookManagement.domain.dto.AuthorRequest
import com.kou.bookManagement.domain.model.Author
import com.kou.bookManagement.domain.model.Book
import com.kou.bookManagement.repository.AuthorRepository
import com.kou.bookManagement.repository.BookRepository
import jakarta.persistence.EntityNotFoundException

class AuthorService(private val bookRepository: BookRepository, private val authorRepository: AuthorRepository) {
    fun insertAuthor(request: AuthorRequest) : Author {
        //リクエストの情報から書籍を抽出
        val books = selectBook(request.books)
        val author = Author(name = request.name, birthDate = request.birthDate, books = books)
        //登録実行
        return authorRepository.save(author)
    }

    //書籍更新
    fun updateAuthor(request: AuthorRequest, id: Int) : Author{
        //更新対象の書籍を取得
        val targetAuthor = authorRepository.findById(id).orElseThrow {
            throw EntityNotFoundException("該当するIDの著者は見つかりませんでした。: $id")
        }

        //リクエストの情報から書籍を抽出
        val books = selectBook(request.books)
        val author = targetAuthor.copy(name = request.name, birthDate = request.birthDate, books = books)
        //更新実行
        return authorRepository.save(author)
    }

    //タイトル、価格、出版状況から書籍の情報を抽出
    private fun selectBook(bookInfo: List<Triple<String, Int, Boolean>>): List<Book>{
        return bookInfo.map { (title, price, published) ->
            bookRepository.findByTitleAndPriceAndPublished(title, price, published)
        }
    }
}