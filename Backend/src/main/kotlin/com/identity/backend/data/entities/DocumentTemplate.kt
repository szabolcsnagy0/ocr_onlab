package com.identity.backend.data.entities

import jakarta.persistence.*
import org.springframework.data.annotation.Id

@Entity
@Table(name = "document_template")
data class DocumentTemplate(
    @jakarta.persistence.Id @Id
    @SequenceGenerator(
        name = "document_template_id_seq",
        sequenceName = "document_template_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "document_template_id_seq"
    )
    var id: Int = 0,
    val name: String = "",
    val userId: Int = 0,
    val jsonTemplate: String = ""
)