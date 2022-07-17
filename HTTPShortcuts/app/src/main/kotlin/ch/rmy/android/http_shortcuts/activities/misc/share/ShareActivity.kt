package ch.rmy.android.http_shortcuts.activities.misc.share

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import ch.rmy.android.framework.extensions.bindViewModel
import ch.rmy.android.framework.extensions.observe
import ch.rmy.android.framework.ui.Entrypoint
import ch.rmy.android.http_shortcuts.activities.BaseActivity

class ShareActivity : BaseActivity(), Entrypoint {

    override val initializeWithTheme: Boolean
        get() = false

    private val viewModel: ShareViewModel by bindViewModel()

    override fun onCreated(savedState: Bundle?) {
        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        val title = intent.getStringExtra(Intent.EXTRA_SUBJECT)
        if (savedState == null) {
            viewModel.initialize(ShareViewModel.InitData(text, title, getFileUris()))
        }
        initViewModelBindings()
    }

    private fun initViewModelBindings() {
        viewModel.viewState.observe(this) { viewState ->
            setDialogState(viewState.dialogState, viewModel)
        }
        viewModel.events.observe(this, ::handleEvent)
    }

    private fun getFileUris(): List<Uri> =
        if (intent.action == Intent.ACTION_SEND) {
            intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)?.let { listOf(it) }
        } else {
            intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
        }
            ?: emptyList()
}
