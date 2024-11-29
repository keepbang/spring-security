package com.keepbang.springsecurity.config.docs

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.headers.Header
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.*
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.responses.ApiResponses
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.security.web.FilterChainProxy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer
import java.util.*

@Configuration
class ApiDocsConfig(
    private val applicationContext: ApplicationContext
) {
    @Profile("local")
    @Bean
    fun api(): OpenAPI {
        val info = Info()
            .title("Spring Security API")
            .version("1.0.0")
            .description("Spring Security 기술들을 적용한 프로젝트입니다.")

        return OpenAPI().info(info)
            .components(Components())
    }

    @Bean
    fun userApiDoc(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("All Api")
            .pathsToMatch("/**")
            .addOpenApiCustomizer(springSecurityLoginEndpointCustomizer())
            .addOpenApiCustomizer(springSecurityLogoutEndpotCustomizer())
            .build()
    }

    private fun springSecurityLogoutEndpotCustomizer(): OpenApiCustomizer {
        val filterChainProxy = applicationContext.getBean(
            AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME,
            FilterChainProxy::class.java
        )
        return OpenApiCustomizer { openAPI ->
            for (filterChain in filterChainProxy.filterChains) {
                val optionalLogoutFilter: Optional<LogoutFilter> =
                    filterChain.filters.stream()
                        .filter { it is LogoutFilter }
                        .map { it as LogoutFilter }
                        .findAny()
                if (optionalLogoutFilter.isPresent) {
                    val operation: Operation = Operation()
                    val apiResponses: ApiResponses = ApiResponses()
                    apiResponses.addApiResponse(
                        HttpStatus.OK.value().toString(),
                        ApiResponse().description(HttpStatus.OK.reasonPhrase)
                    )

                    apiResponses.addApiResponse(
                        HttpStatus.UNAUTHORIZED.value().toString(),
                        ApiResponse().description(HttpStatus.UNAUTHORIZED.reasonPhrase)
                    )

                    operation.responses(apiResponses)
                    operation.addTagsItem("spring-security")
                    operation.summary("로그아웃")

                    val pathItem = PathItem().post(operation)
                    openAPI.paths.addPathItem("/logout", pathItem)
                }
            }
        }
    }

    @Bean
    fun springSecurityLoginEndpointCustomizer(): OpenApiCustomizer {
        val filterChainProxy = applicationContext.getBean(
            AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME,
            FilterChainProxy::class.java
        )
        return OpenApiCustomizer { openAPI ->
            for (filterChain in filterChainProxy.filterChains) {
                val optionalFilter: Optional<UsernamePasswordAuthenticationFilter> =
                    filterChain.filters.stream()
                        .filter { it is UsernamePasswordAuthenticationFilter }
                        .map { it as UsernamePasswordAuthenticationFilter }
                        .findAny()
                if (optionalFilter.isPresent) {
                    val usernamePasswordAuthenticationFilter: UsernamePasswordAuthenticationFilter =
                        optionalFilter.get()
                    val operation: Operation = Operation()
                    val schema: Schema<*> = ObjectSchema()
                        .addProperty(
                            usernamePasswordAuthenticationFilter.usernameParameter,
                            StringSchema()._default("id")
                        )
                        .addProperty(
                            usernamePasswordAuthenticationFilter.passwordParameter,
                            StringSchema()._default("password")
                        )
                    val requestBody: RequestBody = RequestBody().content(
                        Content().addMediaType(
                            org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                            MediaType().schema(schema)
                        )
                    )
                    operation.requestBody(requestBody)
                    val apiResponses: ApiResponses = ApiResponses()
                    apiResponses.addApiResponse(
                        HttpStatus.OK.value().toString(),
                        ApiResponse().description(HttpStatus.OK.reasonPhrase)
                            .headers(
                                mapOf(
                                    "access_token" to  Header()
                                        .description("Access token cookie")
                                        .schema(StringSchema().example("access_token={token}")),
                                    "refresh_token" to  Header()
                                        .description("Refresh token cookie")
                                        .schema(StringSchema().example("refresh_token={token}"))
                                )
                            )
                    )

                    apiResponses.addApiResponse(
                        HttpStatus.UNAUTHORIZED.value().toString(),
                        ApiResponse().description(HttpStatus.UNAUTHORIZED.reasonPhrase)
                    )

                    operation.responses(apiResponses)
                    operation.addTagsItem("spring-security")
                    operation.summary("로그인")

                    val pathItem = PathItem().post(operation)
                    openAPI.paths.addPathItem("/login", pathItem)
                }
            }
        }
    }
}