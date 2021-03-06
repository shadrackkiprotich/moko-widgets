/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.core.OptionalId
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.WidgetDef
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize

@WidgetDef
class TabsWidget<WS : WidgetSize>(
    private val factory: ViewFactory<TabsWidget<out WidgetSize>>,
    override val size: WS,
    override val id: Id?,
    @Suppress("RemoveRedundantQualifierName")
    val tabs: List<TabsWidget.Tab> // for correct codegen
) : Widget<WS>(), OptionalId<TabsWidget.Id> {

    override fun buildView(viewFactoryContext: ViewFactoryContext): ViewBundle<WS> {
        return factory.build(this, size, viewFactoryContext)
    }

    class Tab(
        val title: LiveData<StringDesc>,
        val body: Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>>
    )

    interface Id : Theme.Id
}
