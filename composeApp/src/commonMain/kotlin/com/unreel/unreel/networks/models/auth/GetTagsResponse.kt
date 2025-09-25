package com.unreel.unreel.networks.models.auth

import com.unreel.unreel.networks.models.BaseResponse
import kotlinx.serialization.Serializable

typealias Tag = String;
typealias Category = String;

@Serializable
data class GetTagsResponse(
    val tags: List<Tag>
) : BaseResponse

@Serializable
data class GetCategoriesResponse(
    val categories: List<Category>
) : BaseResponse



