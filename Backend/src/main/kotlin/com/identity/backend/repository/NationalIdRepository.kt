package com.identity.backend.repository

import com.identity.backend.data.entities.NationalId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NationalIdRepository : JpaRepository<NationalId, Int> {
}