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
