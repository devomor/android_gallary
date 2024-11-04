package com.photo.photography.frag

import android.content.Context
import android.view.View
import com.photo.photography.callbacks.CallbackActions
import java.lang.Exception

/**
 * Base class for fragments showing any kind of Media in a Grid fashion.
 *
 * Allows selection, multiple select Context Menus, etc.
 */
abstract class FragBaseMediaGrid : FragBase(),
    IistenerFragment,
    CallbackActions {

    lateinit var editModeListener: EditModeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is EditModeListener) editModeListener = context
        else throw RuntimeException("Parent must implement Edit Mode Listener!")
    }

    fun onBackPressed() = when (editMode()) {
        true -> {
            exitContextMenu()
            true
        }
        false -> false
    }

    /**
     * Exit the Context Menu.
     */
    protected fun exitContextMenu() {
        clearSelected()
        updateToolbar()
    }

    /**
     * Update the Toolbar for switching between Edit Mode.
     */
    protected fun updateToolbar() {
        try {
            editModeListener.changedEditMode(
                editMode(),
                getSelectedCount(),
                getTotalCount(),
                getToolbarTitle()
            )

            // Refresh the Toolbar menu
            activity?.invalidateOptionsMenu()
        }catch (e: Exception){

        }
    }

    /**
     * The total selected item count.
     */
    abstract fun getSelectedCount(): Int

    /**
     * The total number of items displayed.
     */
    abstract fun getTotalCount(): Int

    /**
     * A listener to be invoked when user taps on the Toolbar icon.
     */
    abstract fun getToolbarButtonListener(editMode: Boolean): View.OnClickListener?

    /**
     * Text to display on the toolbar.
     */
    abstract fun getToolbarTitle(): String?
}
