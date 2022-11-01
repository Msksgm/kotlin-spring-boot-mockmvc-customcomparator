package com.example.kotlinspringbootmockmvcjsonassertregex.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.Customization
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.comparator.CustomComparator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.time.OffsetDateTime

class ArticleTest {
    @SpringBootTest
    @AutoConfigureMockMvc
    class CreateArticle {
        @Autowired
        lateinit var mockMvc: MockMvc

        @Test
        fun `正常系-createdAt と updatedAt で比較できずに落ちるテスト`() {
            /**
             * given:
             */
            val requestBody = """
                {
                    "article": {
                        "title": "dummy-title",
                        "description": "dummy-description",
                        "body": "dummy-body"
                    }
                }
            """.trimIndent()

            /**
             * when:
             */
            val resposne = mockMvc.post("/api/articles") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }.andReturn().response
            val actualStatus = resposne.status
            val actualResponseBody = resposne.contentAsString

            /**
             * then:
             * - ステータスコードが一致する
             * - レスポンスボディが一致する
             */
            val expectedStatus = HttpStatus.CREATED.value()
            val expectedResponseBody = """
                {
                    "article": {
                        "title": "dummy-title",
                        "description": "dummy-description",
                        "body": "dummy-body",
                        "createdAt": "2022-01-01T00:00:00.000000+09:00",
                        "updatedAt": "2022-01-01T00:00:00.000000+09:00"
                    }
                }
            """.trimIndent()
            assertThat(actualStatus).isEqualTo(expectedStatus)
            JSONAssert.assertEquals(
                expectedResponseBody,
                actualResponseBody,
                JSONCompareMode.NON_EXTENSIBLE,
            )
        }

        @Test
        fun `正常系-特定のキーが存在すればテストを通す`() {
            /**
             * given:
             */
            val requestBody = """
                {
                    "article": {
                        "title": "dummy-title",
                        "description": "dummy-description",
                        "body": "dummy-body"
                    }
                }
            """.trimIndent()

            /**
             * when:
             */
            val response = mockMvc.post("/api/articles") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }.andReturn().response
            val actualStatus = response.status
            val actualResponseBody = response.contentAsString

            /**
             * then:
             * - ステータスコードが一致する
             * - レスポンスボディが一致する
             */
            val expectedStatus = HttpStatus.CREATED.value()
            val expectedResponseBody = """
                {
                    "article": {
                        "title": "dummy-title",
                        "description": "dummy-description",
                        "body": "dummy-body",
                        "createdAt": "2022-01-01T00:00:00.000000+09:00",
                        "updatedAt": "2022-01-01T00:00:00.000000+09:00"
                    }
                }
            """.trimIndent()
            assertThat(actualStatus).isEqualTo(expectedStatus)
            JSONAssert.assertEquals(
                expectedResponseBody,
                actualResponseBody,
                CustomComparator(
                    JSONCompareMode.NON_EXTENSIBLE,
                    Customization("article.createdAt") { _, _ -> true },
                    Customization("article.updatedAt") { _, _ -> true },
                )
            )
        }

        @Test
        fun `正常系-特定のキーが正規表現と一致すればテストを通す`() {
            /**
             * given:
             */
            val requestBody = """
                {
                    "article": {
                        "title": "dummy-title",
                        "description": "dummy-description",
                        "body": "dummy-body"
                    }
                }
            """.trimIndent()

            /**
             * when:
             */
            val response = mockMvc.post("/api/articles") {
                contentType = MediaType.APPLICATION_JSON
                content = requestBody
            }.andReturn().response
            val actualStatus = response.status
            val actualResponseBody = response.contentAsString

            /**
             * then:
             * - ステータスコードが一致する
             * - レスポンスボディが一致する
             */
            val expectedStatus = HttpStatus.CREATED.value()
            val expectedResponseBody = """
                {
                    "article": {
                        "title": "dummy-title",
                        "description": "dummy-description",
                        "body": "dummy-body",
                        "createdAt": "2022-01-01T00:00:00.000000+09:00",
                        "updatedAt": "2022-01-01T00:00:00.000000+09:00"
                    }
                }
            """.trimIndent()
            assertThat(actualStatus).isEqualTo(expectedStatus)

            fun expectIso8601UtcAndParsable(o: Any): Boolean {
                assertThat(o.toString())
                    .`as`("YYYY-MM-DDTHH:mm:ss.SSSXXX(ISO8601形式)で、TimeZoneはUTCである")
                    .matches("([0-9]{4})-([0-9]{2})-([0-9]{2}T([0-9]{2}):([0-9]{2}):([0-9]{2}).([0-9]{6})\\+09:00)")
                return runCatching { OffsetDateTime.parse(o.toString()) }.isSuccess
            }

            JSONAssert.assertEquals(
                expectedResponseBody,
                actualResponseBody,
                CustomComparator(
                    JSONCompareMode.NON_EXTENSIBLE,
                    Customization("article.createdAt") { actualCreatedAt, _ ->
                        expectIso8601UtcAndParsable(actualCreatedAt)
                    },
                    Customization("article.updatedAt") { actualUpdatedAt, _ ->
                        expectIso8601UtcAndParsable(actualUpdatedAt)
                    },
                )
            )
        }
    }
}
