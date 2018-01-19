package com.networkedassets.git4c.core

import com.github.kittinunf.result.Result
import com.networkedassets.git4c.boundary.GetTemporaryEditBranchCommand
import com.networkedassets.git4c.boundary.outbound.RequestId
import com.networkedassets.git4c.boundary.outbound.TemporaryBranch
import com.networkedassets.git4c.boundary.outbound.VerificationStatus
import com.networkedassets.git4c.boundary.outbound.exceptions.NotFoundException
import com.networkedassets.git4c.core.business.Computation
import com.networkedassets.git4c.core.bussiness.SourcePlugin
import com.networkedassets.git4c.core.datastore.cache.DocumentsViewCache
import com.networkedassets.git4c.core.datastore.cache.TemporaryEditBranchResultCache
import com.networkedassets.git4c.core.datastore.repositories.*
import com.networkedassets.git4c.core.process.ICheckUserPermissionProcess
import com.networkedassets.git4c.delivery.executor.execution.UseCase
import com.networkedassets.git4c.delivery.executor.monitoring.TransactionInfo
import com.networkedassets.git4c.utils.error
import com.networkedassets.git4c.utils.getLogger
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GetTemporaryEditBranchUseCase(
        val importer: SourcePlugin,
        val cache: DocumentsViewCache,
        val repositoryDatabase: RepositoryDatabase,
        val macroSettingsDatabase: MacroSettingsDatabase,
        val checkUserPermissionProcess: ICheckUserPermissionProcess,
        val macroLocationDatabase: MacroLocationDatabase,
        val temporaryEditBranchesDatabase: TemporaryEditBranchesDatabase,
        val temporaryEditBranchResultCache: TemporaryEditBranchResultCache
) : UseCase<GetTemporaryEditBranchCommand, RequestId> {

    //Executor can be changed in runtime
    val executor = Executors.newScheduledThreadPool(4)

    val log = getLogger()

    override fun execute(request: GetTemporaryEditBranchCommand): Result<RequestId, Exception> {
        val macroId = request.macroId

        val id = UUID.randomUUID().toString()

        temporaryEditBranchResultCache.put(id, Computation(id))

        executor.execute {
            process(id, macroId, request.transactionInfo)
        }

        return Result.of { RequestId(id) }

    }

    fun process(requestId: String, macroId: String, transactionInfo: TransactionInfo) {

        try {

            val macroLocation = macroLocationDatabase.get(macroId)
            val macro = macroSettingsDatabase.get(macroId) ?: throw NotFoundException(transactionInfo, VerificationStatus.REMOVED)
            val repositoryId = macro.repositoryUuid ?: throw NotFoundException(transactionInfo, VerificationStatus.REMOVED)
            val repository = repositoryDatabase.get(repositoryId) ?: throw NotFoundException(transactionInfo, VerificationStatus.REMOVED)

            if (importer.isLocked(repository.repositoryPath)) {
                executor.schedule({ process(requestId, macroId, transactionInfo) }, 1, TimeUnit.SECONDS)
                return
            }

            var temporaryBranch = macroLocation.withNotNull { temporaryEditBranchesDatabase.get(repositoryId, this.pageId) }

            if (temporaryBranch != null &&
                    (!importer.getBranches(repository).contains(temporaryBranch.name)
                            || importer.isBranchMerged(repository, temporaryBranch.name, macro.branch))) {

                temporaryEditBranchesDatabase.remove(temporaryBranch.uuid)
                cache.remove(macroId)
                temporaryBranch = null
            }

            temporaryEditBranchResultCache.put(requestId, Computation(requestId, Computation.ComputationState.FINISHED, TemporaryBranch(temporaryBranch?.name)))

        } catch (e: Exception) {
            log.error(e) { "Execution failed" }
            temporaryEditBranchResultCache.put(requestId, Computation(requestId, Computation.ComputationState.FAILED, error = e))
        }


    }

    infix inline fun <T : Any, R : Any> T?.withNotNull(thenDo: T.() -> R?): R? = if (this == null) null else this.thenDo()

}