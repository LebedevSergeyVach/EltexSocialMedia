package com.eltex.androidschool.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration

import java.util.Locale

class LocaleContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, locale: Locale): ContextWrapper {
            val resources = context.resources
            val configuration = Configuration(resources.configuration)
            configuration.setLocale(locale)
            val wrappedContext = context.createConfigurationContext(configuration)
            return LocaleContextWrapper(wrappedContext)
        }
    }
}
