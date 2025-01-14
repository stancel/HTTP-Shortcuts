package ch.rmy.android.http_shortcuts.activities.misc

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import ch.rmy.android.framework.extensions.consume
import ch.rmy.android.framework.extensions.createIntent
import ch.rmy.android.framework.extensions.doOnTextChanged
import ch.rmy.android.framework.extensions.focus
import ch.rmy.android.framework.extensions.getSerializable
import ch.rmy.android.framework.ui.BaseActivityResultContract
import ch.rmy.android.framework.ui.BaseIntentBuilder
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.activities.BaseActivity
import ch.rmy.android.http_shortcuts.databinding.ActivityCurlImportBinding
import ch.rmy.curlcommand.CurlCommand
import ch.rmy.curlcommand.CurlParser

class CurlImportActivity : BaseActivity() {

    private var commandEmpty = true
        set(value) {
            if (field != value) {
                field = value
                invalidateOptionsMenu()
            }
        }

    private lateinit var binding: ActivityCurlImportBinding

    override fun onCreated(savedState: Bundle?) {
        binding = applyBinding(ActivityCurlImportBinding.inflate(layoutInflater))
        setTitle(R.string.title_curl_import)

        binding.curlImportCommand.doOnTextChanged { text ->
            commandEmpty = text.isEmpty()
        }

        binding.curlImportCommand.imeOptions = EditorInfo.IME_ACTION_GO or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING
        } else 0
        binding.curlImportCommand.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO && !commandEmpty) consume {
                startImport()
            } else false
        }
    }

    override fun onResume() {
        super.onResume()
        binding.curlImportCommand.focus()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.curl_import_activity_menu, menu)
        menu.findItem(R.id.action_create_from_curl).isVisible = !commandEmpty
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_create_from_curl -> consume { startImport() }
        else -> super.onOptionsItemSelected(item)
    }

    private fun startImport() {
        val commandString = binding.curlImportCommand.text.toString()
        val command = CurlParser.parse(commandString)

        setResult(
            Activity.RESULT_OK,
            ImportFromCurl.createResult(command),
        )
        finish()
    }

    override val navigateUpIcon = R.drawable.ic_clear

    object ImportFromCurl : BaseActivityResultContract<IntentBuilder, CurlCommand?>(::IntentBuilder) {

        private const val EXTRA_CURL_COMMAND = "curl_command"

        override fun parseResult(resultCode: Int, intent: Intent?): CurlCommand? =
            intent?.getSerializable(EXTRA_CURL_COMMAND)

        fun createResult(command: CurlCommand): Intent =
            createIntent {
                putExtra(EXTRA_CURL_COMMAND, command)
            }
    }

    class IntentBuilder : BaseIntentBuilder(CurlImportActivity::class)
}
