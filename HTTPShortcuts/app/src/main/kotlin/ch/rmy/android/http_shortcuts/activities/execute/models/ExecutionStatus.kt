package ch.rmy.android.http_shortcuts.activities.execute.models

import ch.rmy.android.http_shortcuts.data.domains.variables.VariableId
import ch.rmy.android.http_shortcuts.http.ShortcutResponse
import java.io.IOException

sealed interface ExecutionStatus {

    object Preparing : ExecutionStatus

    data class InProgress(
        override val variableValues: Map<VariableId, String>,
    ) : ExecutionStatus, WithVariables

    data class WrappingUp(
        override val variableValues: Map<VariableId, String>,
    ) : ExecutionStatus, WithVariables

    data class CompletedSuccessfully(
        override val response: ShortcutResponse?,
        override val variableValues: Map<VariableId, String>,
    ) : ExecutionStatus, WithResponse, WithVariables

    data class CompletedWithError(
        val error: IOException?,
        override val response: ShortcutResponse?,
        override val variableValues: Map<VariableId, String>,
    ) : ExecutionStatus, WithResponse, WithVariables

    interface WithResponse {
        val response: ShortcutResponse?
    }

    interface WithVariables {
        val variableValues: Map<VariableId, String>
    }
}
