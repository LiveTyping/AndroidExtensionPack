# AndroidExtensionPack
The library provides useful extensions for your projects


``` kotlin
maven { url 'https://jitpack.io' }
```


``` kotlin
implementation 'com.github.LiveTyping:AndroidExtensionPack:1.0'
```
# Анимация
- BetterViewAnimator 
> Предоставляет функциональность для сокрытия всех элементов за исключением компонента, который был передан в качестве аргумента. Пример использования:
``` kotlin
<com.elegion.klu.ui.widget.BetterViewAnimator
        android:id="@+id/betterAnimation"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:paddingHorizontal="16dp"
        app:layout_constraintBottom_toTopOf="@+id/btnSave"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/toolbar"
        app:visible_child="@id/createCourseName">
        
betterAnimation.visibleChildId = second.id <- вызов из кода
```
- Extensions функции от **View**. Позволяют изменять положение компонента по разным осям, далее будет описано какие методы можно использовать

``` kotlin
rotate
```
> принимает 6 параметров, которые изначально определены в конструкторе: *initRotation* (начальное положение), *rotation* (угол поворота), *initAlpha* (прозрачность затухания), *alpha* (прозрачность компонента), *startDelay* (задержка начала анимации), *build* (лямбда, можно изменить поведение функции).

``` kotlin
rotateIn, rotateOut
```
> поворот внутрь и наружу. Принимает параметром угол поворота.

``` kotlin
colorFade
```
> принимает 3 параметра, которые изначально определены в конструкторе: *from*, *to* (начальный и конечный цвет затухания), *build* (лямбда, можно изменить поведение функции).

``` kotlin
slideUp, slideDown
```
> перемещают компонент вверх или вниз, функция принимает контекст. 

``` kotlin
animate
```
> принимает контекст и любой файл для анимации (int). Анимирует компонент. 

``` kotlin
leftToRight, rightToLeft
```
> принимают контекст и перемещают компонент слева на право или справо на лево. 

``` kotlin
fadeOut
```
> принимает 4 параметра: *offset* (сдвиг), *duration* (длительность), *onStart, onFinish* (действие по началу анимации или по окончанию).

# Биометрия
> Эти функции позволют работать с биометрией на смартфоне и контролировать такие кейсы как: возможна ли биометрия в принципе, запрашивать доступ (отпечаток пальца, снимок лица)
- **biometricAuth** - функция возращает объект *BiometricPrompt* (содержит результат биометрии), принимает 4 метода: *promptInfo* (передаем объект **BiometricPrompt.PromptInfo**, содержит информацию о том какой тип биометрии вы запрашиваете), *onAuthFailed* (действие, если запрос упал), *onAuthError, onAuthSuccess* (дейсвтие, если произошла ошибка или запрос выполнялся успешно). Функция может быть вызвана как от *Activity* так и от *Fragment*.
- **canAuthenticate** - функция может быть вызвана как от *Activity* так и от *Fragment*. Содержит три основных параметра, внутри которых следует реализовать логику отслеживания события. Например *hardwareUnavailable* говорит о том, что на смартфоне нет возможности работы с биометрией на хардверном уровне. Эта лямбда будет вызвана только в том случае, если это действительно так. Также в методе содержаться еще две лямбды: *securityUpdateNeeded, noFingerprintsEnrolled* на устройстве не настроена биоментрия и отпечаток пальца не был распознан соответственно.

# Корутины
> Эти функции позволют работать с многопоточностью
- **makeIOCall** - может быть вызвана из *ViewModel, CoroutineScope*. Нужна для запросов в интернет. Содержит 4 параметра, поведение которых можно определить: *onCallExecuted* (вызывается после завершения вызова), *onErrorAction* (вызывается при ошибке), *ioCall* (сам запрос), *onCalled* (возращает результат своей работы в виде модели)
- **onMain** - запускает код в главном потоке
- **onDefault** - запускает код в основном потоке
- **onIO** - запускает код в отдельном потоке, но без дополнительных слушателей
- **ioCoroutineGlobal, mainCoroutineGlobal, defaultCoroutineGlobal, unconfinedCoroutineGlobal** - запускает код глобально для всего приложения в различных потоках
- **withMainContext, withIOContext, withDefaultContext, withUnconfinedContext, withNonCancellableContext** - запуск кода в определенном контексте
- **viewModelIOCoroutine, viewModelMainCoroutine, viewModelDefaultCoroutine, viewModelUnconfinedCoroutine, viewModelNonCancellableCoroutine** - запускается от объекта *ViewModel* для различных потоков
- **main, io, default, unconfined, nonCancellable** - запускается от объекта *CoroutineScope* для различных потоков
- **cancelIfActive** - завершает *Job*, если активна
- **ioCoroutine, mainCoroutine, unconfinedCoroutine, defaultCoroutine, nonCancellableCoroutine** - запускает код в разных потоках как от *Fragment* так и от *AppCompatActivity*
- **doParallel, doParallelWithResult** - делает параллельный запрос с результатом и без

# Работа с БД
Все модели должны быть дополнительно обернуты в дата класс *DBResult*:
``` kotlin
sealed class DBResult<out T> {
    data class Success<T>(val value: T) : DBResult<T>()
    object Querying : DBResult<Nothing>()
    object EmptyDB : DBResult<Nothing>()
    data class DBError(val throwable: Throwable) : DBResult<Nothing>()
}
```
Далее список функций, которые можно использовать как при работе с корутинами так и с RX. Их можно использовать как от *ViewModel* так и от *CoroutineScope*
## Корутины
- **makeDBCallLiveData** - принимает запрос и возращает лайвдату с оберткой *DBResult*
- 
## RX
