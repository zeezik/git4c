package com.networkedassets.git4c.core

import com.github.kittinunf.result.Result
import com.networkedassets.git4c.boundary.VerifyDocumentationMacroByDocumentationsMacroIdQuery
import com.networkedassets.git4c.boundary.outbound.VerificationStatus
import com.networkedassets.git4c.boundary.outbound.exceptions.NotFoundException
import com.networkedassets.git4c.core.bussiness.SourcePlugin
import com.networkedassets.git4c.core.datastore.repositories.ExtractorDataDatabase
import com.networkedassets.git4c.core.datastore.repositories.GlobForMacroDatabase
import com.networkedassets.git4c.core.datastore.repositories.MacroSettingsDatabase
import com.networkedassets.git4c.core.datastore.repositories.RepositoryDatabase
import com.networkedassets.git4c.core.process.action.RefreshMacroAction
import com.networkedassets.git4c.delivery.executor.execution.UseCase

class VerifyDocumentationMacroByDocumentationsMacroIdUseCase(
        val refreshMacroProcess: RefreshMacroAction,
        val macroSettingsRepository: MacroSettingsDatabase,
        val globForMacroDatabase: GlobForMacroDatabase,
        val repositoryDatabase: RepositoryDatabase,
        val extractorDataDatabase: ExtractorDataDatabase,
        val importer: SourcePlugin
) : UseCase<VerifyDocumentationMacroByDocumentationsMacroIdQuery, String> {

    override fun execute(request: VerifyDocumentationMacroByDocumentationsMacroIdQuery): Result<String, Exception> {
        // TODO: Change it when implementing Admin Panel improvements!
        val searchedMacroId = request.macroId
        val macroSettings = macroSettingsRepository.get(searchedMacroId) ?: return@execute Result.error(NotFoundException(request.transactionInfo, VerificationStatus.REMOVED))
        if (macroSettings.repositoryUuid == null) return@execute Result.error(NotFoundException(request.transactionInfo, VerificationStatus.REMOVED))
        val repository = repositoryDatabase.get(macroSettings.repositoryUuid) ?: return@execute Result.error(NotFoundException(request.transactionInfo, VerificationStatus.REMOVED))

        val vi = importer.verify(repository)

        return if (vi.isOk()) {
            Result.of { "" }
        } else {
            Result.error(IllegalArgumentException(""))
        }
    }


}
