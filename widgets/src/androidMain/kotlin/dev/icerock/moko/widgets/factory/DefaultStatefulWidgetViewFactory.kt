/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.factory

import android.view.View
import android.widget.FrameLayout
import dev.icerock.moko.mvvm.State
import dev.icerock.moko.widgets.StatefulWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.style.applyStyle
import dev.icerock.moko.widgets.style.ext.applyMargin
import dev.icerock.moko.widgets.style.ext.toPlatformSize
import dev.icerock.moko.widgets.style.view.WidgetSize
import dev.icerock.moko.widgets.utils.bindNotNull

actual class DefaultStatefulWidgetViewFactory actual constructor(
    style: Style
) : DefaultStatefulWidgetViewFactoryBase(style) {

    override fun <WS : WidgetSize> build(
        widget: StatefulWidget<out WidgetSize, *, *>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val context = viewFactoryContext.context
        val lifecycleOwner = viewFactoryContext.lifecycleOwner
        val dm = context.resources.displayMetrics

        val root = FrameLayout(context)
        root.applyStyle(style)

        val factoryContext = ViewFactoryContext(context, lifecycleOwner, root)

        val dataView = widget.dataWidget.buildView(factoryContext)
        val loadingView = widget.loadingWidget.buildView(factoryContext)
        val emptyView = widget.emptyWidget.buildView(factoryContext)
        val errorView = widget.errorWidget.buildView(factoryContext)
        val views = listOf(dataView, loadingView, emptyView, errorView)

        views.forEach { viewBundle ->
            val view = viewBundle.view
            view.visibility = View.GONE
            root.addView(view, FrameLayout.LayoutParams(
                viewBundle.size.width.toPlatformSize(dm),
                viewBundle.size.height.toPlatformSize(dm)
            ).apply {
                viewBundle.margins?.let { applyMargin(dm, it) }
            })
        }

        widget.state.bindNotNull(lifecycleOwner) { state ->
            val currentView = when (state) {
                is State.Empty -> emptyView
                is State.Loading -> loadingView
                is State.Data -> dataView
                is State.Error -> errorView
            }

            views.forEach { it.view.visibility = View.GONE }
            currentView.view.visibility = View.VISIBLE
        }

        return ViewBundle(
            view = root,
            size = size,
            margins = style.margins
        )
    }
}
