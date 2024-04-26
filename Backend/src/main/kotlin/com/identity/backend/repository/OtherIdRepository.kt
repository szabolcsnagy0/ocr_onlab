package com.identity.backend.repository

import com.identity.backend.data.entities.OtherId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OtherIdRepository: JpaRepository<OtherId, Int> {
}