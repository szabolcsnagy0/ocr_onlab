package com.identity.backend.data.entities

import jakarta.persistence.*
import org.springframework.data.annotation.Id

@Entity
@Table(name = "document_field")
data class DocumentField(
    @jakarta.persistence.Id @Id
    @SequenceGenerator(
        name = "document_field_id_seq",
        sequenceName = "document_field_id_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "document_field_id_seq"
    )
    var id: Int? = null,
    var title: String? = null,
    var value: String? = null,
    var documentId: Int? = null,
)