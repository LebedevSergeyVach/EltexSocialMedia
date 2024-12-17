package com.eltex.androidschool.rx.posts

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

interface SchedulersProvider {
    val io: Scheduler
        get() = Schedulers.io()

    val computation: Scheduler
        get() = Schedulers.computation()

    val mainThread: Scheduler
        get() = AndroidSchedulers.mainThread()

    companion object {
        val DEFAULT = object : SchedulersProvider {}
    }
}
