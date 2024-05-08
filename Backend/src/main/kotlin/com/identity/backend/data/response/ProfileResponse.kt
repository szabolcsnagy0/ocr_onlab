package com.identity.backend.data.response

import com.identity.backend.data.entities.Profile

data class ProfileResponse (
    val id: Int,
    val name: String?,
    val nationalIds: List<NationalIdResponse>?
)

fun Profile.toProfileResponse() = ProfileResponse(
    id = this.id,
    name = this.name,
    nationalIds = this.nationalIdList.map { it.toNationalIdResponse() }
)