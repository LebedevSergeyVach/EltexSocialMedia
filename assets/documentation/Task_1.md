# Retrofit
<a name="up"></a>

---

## Задача №2. Драг-рейсинг*

Дан код:

```kotlin
fun main() {
    var counter = 0

    val firstWorker = thread {
        repeat(1_000_000) {
            counter++
        }
    }

    val secondWorker = thread {
        repeat(1_000_000) {
            counter++
        }
    }

    firstWorker.join()
    secondWorker.join()

    println(counter) // ?
}
```

Найдите способ сделать так, чтобы в результате всегда получалось 2_000_000

---

## Ответ

Необходимо избежать гонки между данными в потоках (потоки перезаписывают друг друга).
Можно использовать: **`AtomicInteger`**, **`synchronized`** и **`ReentrantLock`**.

### **`AtomicInteger`**

Производительный способ с помощью атомарных операций.
Ни один поток не может увидеть промежуточное состояние и значение данных. Операции выполняются целиком.
Но, как я знаю, можно использовать только в простых операциях.

```kotlin
import kotlin.concurrent.thread
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    repeat(10) {
        val counter = AtomicInteger(0)

        val firstWorker = thread {
            repeat(1_000_000) {
                counter.incrementAndGet()
            }
        }

        val secondWorker = thread {
            repeat(1_000_000) {
                counter.incrementAndGet()
            }
        }

        firstWorker.join()
        secondWorker.join()

        println(counter.get())
    }
}

```

### **`synchronized`**

Популярный и простой способ с помощью синхронизации потоков, но менее производительный, чуть-чуть :)
По сути блокирует часть данных, которые используется в другом месте кода.

```kotlin
import kotlin.concurrent.thread

fun main() {
    repeat(10) {
        var counter = 0

        val lock = Object()

        val firstWorker = thread {
            repeat(1_000_000) {
                synchronized(lock) {
                    counter++
                }
            }
        }

        val secondWorker = thread {
            repeat(1_000_000) {
                synchronized(lock) {
                    counter++
                }
            }
        }

        firstWorker.join()
        secondWorker.join()

        println(counter)
    }
}
```

### **`ReentrantLock`**

Лучший, универсальный и гибко-настраиваемый способ.
Тоже простой и похожий на `synchronized`, но нужно обязательно написать обработку `lock()` и `unlock()`.

```kotlin
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

fun main() {
    repeat(10) {
        var counter = 0

        val lock = ReentrantLock()

        val firstWorker = thread {
            repeat(1_000_000) {
                lock.lock()
                try {
                    counter++
                } finally {
                    lock.unlock()
                }
            }
        }

        val secondWorker = thread {
            repeat(1_000_000) {
                lock.lock()
                try {
                    counter++
                } finally {
                    lock.unlock()
                }
            }
        }

        firstWorker.join()
        secondWorker.join()

        println(counter)
    }
}
```

---

### [TASK_1](Task_1.md) [UP](#up)
