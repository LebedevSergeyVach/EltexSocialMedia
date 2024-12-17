package com.eltex.androidschool

import com.eltex.androidschool.rx.common.SchedulersProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

object TestSchedulersProvider : SchedulersProvider {
    override val io: Scheduler = Schedulers.trampoline()

    override val computation: Scheduler = Schedulers.trampoline()

    override val mainThread: Scheduler = Schedulers.trampoline()
}
