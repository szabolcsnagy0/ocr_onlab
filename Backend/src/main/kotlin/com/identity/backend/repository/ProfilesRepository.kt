package com.identity.backend.repository

import com.identity.backend.data.entities.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfilesRepository: JpaRepository<Profile, Int> {
}