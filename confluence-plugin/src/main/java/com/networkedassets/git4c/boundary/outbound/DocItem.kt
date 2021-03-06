package com.networkedassets.git4c.boundary.outbound

import com.networkedassets.git4c.data.macro.documents.item.DocumentsItem

class DocItem(documentsItem: DocumentsItem) {
    val uuid: String = documentsItem.index
    val fullName: String = documentsItem.path
    val name: String = documentsItem.name
    val locationPath: List<String> = documentsItem.locationPath + documentsItem.name
    val lastUpdateAuthorName: String? = documentsItem.updateAuthorFullName
    val lastUpdateAuthorEmail: String? = documentsItem.updateAuthorEmail
    val lastUpdateTime: Long? = documentsItem.updateDate.time
    val content: String? = documentsItem.content
    val rawContent = documentsItem.rawContent
    val tableOfContents = documentsItem.tableOfContents
}