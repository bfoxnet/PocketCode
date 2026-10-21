package com.bfoxnet.pocketcode.search.code

import com.bfoxnet.pocketcode.search.CodeItem
import kotlinx.coroutines.flow.Flow

/** Strategy for searching code content. */
interface CodeSearchStrategy {
    fun search(query: String): Flow<CodeItem>
}
