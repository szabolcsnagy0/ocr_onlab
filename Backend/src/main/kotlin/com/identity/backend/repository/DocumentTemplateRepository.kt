package com.identity.backend.repository

import com.identity.backend.data.entities.DocumentTemplate
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentTemplateRepository: JpaRepository<DocumentTemplate, Int> {
}