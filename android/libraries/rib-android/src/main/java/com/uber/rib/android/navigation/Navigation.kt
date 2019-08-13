package com.uber.rib.android.navigation

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import kotlin.collections.ArrayList

class Navigation(
    private val manager: NavigationManager,
    private val parent: NavigationData
) {
    private val list by lazy {
        ArrayList<NavigationData>()
    }
    private val notifySubject by lazy {
        BehaviorSubject.create<Notify>()
    }

    val size: Int
        @Synchronized
        get() { return list.size }

    val notify: Observable<Notify>
        get() {
            return notifySubject
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
        }

    @Synchronized
    fun last(): NavigationData? {
        return getChildDataAt(size - 1)
    }

    @Synchronized
    fun getChildDataAt(index: Int): NavigationData? {
        return if (index in 0 until size) {
            list[index]
        } else {
            null
        }
    }

    @Synchronized
    fun addChild(name: String): NavigationData {
        val tag = UUID.randomUUID().toString()
        val data = NavigationData(tag = tag, name = name, manager = manager)
        val old = last()

        list.add(data)
        manager.addNavigation(tag, data)
        notifySubject.onNext(Notify(
            dataAdd = data,
            dataRemove = old,
            isPop = false
        ))

        return data
    }

    @Synchronized
    fun pop(count: Int = 1): Boolean {
        if (count <= 0 || size == 0) return false

        val old = last()
        var index = 0

        while (index != count && size != 0) {
            list.removeAt(size - 1)
            index++
        }

        notifySubject.onNext(Notify(
            dataAdd = last(),
            dataRemove = old,
            isPop = true
        ))

        return true
    }

    class Notify(
        val dataAdd: NavigationData?,
        val dataRemove: NavigationData?,
        val isPop: Boolean = false
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Notify

            if (dataAdd != other.dataAdd) return false
            if (dataRemove != other.dataRemove) return false
            if (isPop != other.isPop) return false

            return true
        }

        override fun hashCode(): Int {
            var result = dataAdd?.hashCode() ?: 0
            result = 31 * result + (dataRemove?.hashCode() ?: 0)
            result = 31 * result + isPop.hashCode()
            return result
        }
    }
}