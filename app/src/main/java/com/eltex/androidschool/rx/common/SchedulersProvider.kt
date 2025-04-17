//package com.eltex.androidschool.rx.common
//
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
//import io.reactivex.rxjava3.core.Scheduler
//import io.reactivex.rxjava3.schedulers.Schedulers
//
///**
// * Интерфейс для предоставления планировщиков (Schedulers) из RxJava3.
// *
// * Этот интерфейс определяет методы для получения различных типов планировщиков,
// * которые используются для выполнения асинхронных операций.
// *
// * @see Scheduler Основной класс из RxJava3, представляющий планировщик.
// */
//interface SchedulersProvider {
//
//    /**
//     * Возвращает планировщик для выполнения операций ввода-вывода (I/O).
//     *
//     * Этот планировщик подходит для операций, связанных с сетью, базой данных или файловой системой.
//     *
//     * @return Scheduler Планировщик для операций ввода-вывода.
//     */
//    val io: Scheduler
//        get() = Schedulers.io()
//
//    /**
//     * Возвращает планировщик для выполнения вычислительных операций.
//     *
//     * Этот планировщик подходит для операций, которые не требуют ввода-вывода,
//     * например, обработка данных или выполнение сложных вычислений.
//     *
//     * @return Scheduler Планировщик для вычислительных операций.
//     */
//    val computation: Scheduler
//        get() = Schedulers.computation()
//
//    /**
//     * Возвращает планировщик для выполнения операций в основном потоке (UI-потоке).
//     *
//     * Этот планировщик используется для обновления пользовательского интерфейса.
//     *
//     * @return Scheduler Планировщик для основного потока.
//     */
//    val mainThread: Scheduler
//        get() = AndroidSchedulers.mainThread()
//
//    companion object {
//        /**
//         * Экземпляр по умолчанию, предоставляющий стандартные планировщики.
//         */
//        val DEFAULT = object : SchedulersProvider {}
//    }
//}
