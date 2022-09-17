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
Список функций, которые можно использовать как при работе с корутинами так и с RX. Их можно использовать от *ViewModel*, *CoroutineScope*, а также от RX объектов:
- **makeDBCall** - принимает запрос и возращает результат с оберткой *DBResult* для корутин (liveData) или RX
- **makeDBCallAsync** - делает запрос асинхронным
- **makeDB...** - и прочие функции, которые начинаются с этого, предназначены для extension вызовов от разных объектов


# Регулярки
> Список констант, которые можно использовать для проверки совпадает строка с регуляркой или нет при помощи функции **isMatch**
``` kotlin
const val NAME_REGEX = "^[a-zA-Z\\s]+" - **Это имя?**

const val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$" - **Это телефон?**

const val REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6，5])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$" - **Это телефон?**

const val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}" - **Это телефон?**

const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$" - **Это почта?**

const val REGEX_URL = "[a-zA-z]+://[^\\s]*" - **Это ссылка?**

const val REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$" - **Это имя пользователя?**

const val REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$" - **Это дата?**

const val REGEX_BLANK_LINE = "\\n\\s*\\r" - **Это пустая линия?**

const val REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$" - **Это положительные инты?**

const val REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$" - **Это отрицательные инты?**

const val REGEX_INTEGER = "^-?[1-9]\\d*$" - **Это инты?**

const val REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$" - **Это положительные флоаты?**

const val REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$" - **Это отрицательные флоаты?**

fun CharSequence.isMatch(regex: String) = Regex(regex).matches(this) <- вызов из функции
```

# RX
> Описаны методы для работы с многопоточностью
- **runSafe...** - запускает код в различных потоках
- **unsubscribe** - вызывает метод *dispose* у объекта *Disposable*
- **rxTimer** - принимает следующие параметры: *oldTimer* (optional параметр, который завершит прошлый таймер типа *Disposable*), *time* (длительность), прочие заданы автоматически, при желании можно изменить поток выполнения и отслеживания
- **ifCompletes** - если *Completable* объект будет успешно завершен, можно выполнить дополнительный код
- **applyNetworkSchedulers** - запускает интернет запрос
- **intervalRequest** - запрос будет происходить в неком интервале

# SharedPreferences
> Работа с локальной БД
- **put...** - добавляет в преференсы какие-то данные, принимает два параметра: *key, value*
- **commit** - сохраняет изменения, принимает два параметра: *key, value*
- **clear, commitClear** - полное очищение данных из файла
- **remove, commitRemoval** - удаление данных по ключу
- **get** - получение данных по ключу

# String
> Работа со строками
- **toBoolean** - трансформирует строку в бул
- **convertToCamelCase** - трансформирует строку в CamelCase
- **ellipsize** - добавляет точки после определенной позиции
- **highlightSubstring** - подсвечивает некоторым стилем выделенную часть строки и возращает объект Spannable
- **toEnum** - трансформирует строку в енам
- **capitalizeFirstLetter, capitalizeFirstLetterEachWord** - делает первую букву заглавной или в каждом слове

# Toast
> Показывает тосты
- **shortToast, longToast** - недолгие, долгие всплывающие сообщения

# View
> Работа с TabView, View и прочими штуками, которые относятся к визуальному представлению
- ****
