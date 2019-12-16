# CPU & Performance

## Avaliação de desempenho

Segundo o Android Profiler, notamos que nossa aplicação não consumia muita memória. Porém, percebemos que o uso de memória aumentava lentamente ao longo do uso da aplicação, um indício de possível vazamento de memória.

## Uso de LeakCanary para identificar e consertar vazamento

Em seguida, confirmamos a presença de um vazamento de memória atravéz do LeakCanary. O dump nos mostrou claramente que o campo `AboutFragment.aired` era responsável pelo vazamento:
```
    ...
    ├─ com.riqsphere.myapplication.ui.animeDetail.about.AboutFragment
    │    Leaking: NO (Fragment#mFragmentManager is not null)
    │    Fragment.mTag=android:switcher:2131230796:0
    │    ↓ AboutFragment.aired
    │                    ~~~~~
    ├─ androidx.appcompat.widget.AppCompatTextView
    │    Leaking: YES (View detached and has parent)
    │    mContext instance of com.riqsphere.myapplication.ui.animeDetail.AnimeDetailActivity with mDestroyed = false
    │    View#mParent is set
    │    View#mAttachInfo is null (view detached)
    │    View.mWindowAttachCount = 1
    │    ↓ AppCompatTextView.mParent
    ├─ androidx.constraintlayout.widget.ConstraintLayout
    │    Leaking: YES (AppCompatTextView↑ is leaking and View detached and has parent)
    ...
```

Atestado isso, trocamos o tipo da variável `aired` de `TextView` para `TextView?` fizemos um override dos métodos `onDestroy()`, `onDestroyView()`, e `onDetach()` para eliminar o vazamento. Aplicamos o mesmo tratamento ao campo `synopsis`, visto que ele era utilizado pela classe de forma quase idêntica ao campo `aired`:
```
override fun onDestroy() {
    super.onDestroy()
    release()
}

override fun onDetach() {
    super.onDestroy()
    release()
}

override fun onDestroyView() {
    super.onDestroy()
    release()
}

private fun release() {
    synopsis = null
    aired = null
}
```

Feito isto, o LeakCanary não identificou mais nenhum vazamento de memória no nosso app.