package com.identity.backend.repository

import com.identity.backend.data.entities.Document
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentRepository : JpaRepository<Document, Int> {
}