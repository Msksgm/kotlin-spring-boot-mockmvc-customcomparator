package com.example.kotlinspringbootmockmvcjsonassertregex.presentation

import com.example.realworldkotlinspringbootjdbc.openapi.generated.controller.ArticlesApi
import com.example.realworldkotlinspringbootjdbc.openapi.generated.model.Article
import com.example.realworldkotlinspringbootjdbc.openapi.generated.model.NewArticleRequest
import com.example.realworldkotlinspringbootjdbc.openapi.generated.model.SingleArticleResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime

@RestController
class ArticleController : ArticlesApi {
    override fun createArticle(newArticleRequest: NewArticleRequest): ResponseEntity<SingleArticleResponse> {
        return ResponseEntity(
            SingleArticleResponse(
                Article(
                    title = "dummy-title",
                    description = "dummy-description",
                    body = "dummy-body",
                    createdAt = OffsetDateTime.now(),
                    updatedAt = OffsetDateTime.now()
                )
            ),
            HttpStatus.CREATED
        )
    }
}
