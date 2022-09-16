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
- Extensions функции от **View**
> Эти функции позволяют изменять положение компонента по разным осям, далее будет описано какие методы можно использовать
``` kotlin
1. **rotate** - принимает 6 параметров, которые изначально определены в конструкторе: *initRotation* (начальное положение), *rotation* (угол поворота), *initAlpha* (прозрачность затухания), *alpha* (прозрачность компонента), *startDelay* (задержка начала анимации), *build* (лямбда, можно изменить поведение функции).
2. **rotateIn, rotateOut** - поворот внутрь и наружу. Принимает параметром угол поворота.
3. **colorFade** - принимает 3 параметра, которые изначально определены в конструкторе: *from*, *to* (начальный и конечный цвет затухания), *build* (лямбда, можно изменить поведение функции).
4. **slideUp, slideDown** - перемещают компонент вверх или вниз, функция принимает контекст. 
5. **animate** - принимает контекст и любой файл для анимации (int). Анимирует компонент. 
6. **leftToRight, rightToLeft** - принимают контекст и перемещают компонент слева на право или справо на лево. 
7. **fadeOut** - принимает 4 параметра: *offset* (сдвиг), *duration* (длительность), *onStart, onFinish* (действие по началу анимации или по окончанию).
```

# Биометрия
> Эти функции позволют работать с биометрией на смартфоне и контролировать такие кейсы как: возможна ли биометрия в принципе, запрашивать доступ (отпечаток пальца, снимок лица)
- **biometricAuth**
