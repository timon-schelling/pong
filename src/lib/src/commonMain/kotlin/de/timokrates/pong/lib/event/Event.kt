package de.timokrates.pong.lib.event

class Event<T>(
        listenerList: ListenerList<T> = ListenerList(), executor: Executor<T> = Executor()
) : NotFireableEvent<T>(listenerList, executor) {

    fun fire(data: T) = fireProtected(data)
    fun clear() = listenerList.clear()
    fun toSaveEvent() = NotFireableEvent<T>(listenerList, executor)
}
