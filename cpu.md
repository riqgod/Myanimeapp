# CPU & Performance

## Avaliação de desempenho

Segundo o Android Profiler, nossa aplicação não consume muita CPU. Nossa utilização de CPU nunca ultrapassou valores denotados como "medium" no Profiler. Portanto, não fizemos alteração ao nosso código com base nos resultados do Profiler.

A seguir, listamos algumas boas práticas que adotamos durante o desenvolvimento.

## Multithreading e corotinas

Devido ao acesso contínuo a recursos de rede que precisam ser carregados e então mostrados na interface, nosso projeto faz uso ubíquito de `AsyncTask` e várias funcionalidades de corotinas do Kotlin. A seguir, um extrato da classe `DiscoverFragment` mostra o disparo de um único `AsyncTask` que, atravéz de corotinas, faz em paralelo quatro requisições à rede, e povoa quatro `RecyclerViews` com os dados retornados:

```kotlin
val seasonalRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_seasonal)
val seasonalPair = Pair(seasonalRecyclerView, fetchCurrentSeason)

val topUpcomingRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_top_upcoming)
val topUpcomingPair = Pair(topUpcomingRecyclerView, fetchTopUpcoming)

val mostPoplarRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_mp)
val mostPoplarPair = Pair(mostPoplarRecyclerView, fetchMostPoplar)

val topScoreRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_score)
val topScorePair = Pair(topScoreRecyclerView, fetchTopScore)

val pairs = arrayOf(seasonalPair, topUpcomingPair, mostPoplarPair, topScorePair)
AsyncDataSetter().execute(*pairs)
```

A seguir, o código do respectivo `AsyncTask`:

```kotlin
private class AsyncDataSetter: AsyncTask<Pair<RecyclerView, () -> ArrayList<SearchModel>>, Void, Void>() {
    override fun doInBackground(vararg params: Pair<RecyclerView, () -> ArrayList<SearchModel>>?): Void? {
        runBlocking {
            params.forEach { pair ->
                launch {
                    if (pair != null) {
                        val recyclerView = pair.first
                        val data = withContext(Dispatchers.Unconfined) { fetchData(pair.second) }
                        recyclerView.setData(data)
                    }
                }
            }
        }
        return null
    }

    private inline fun <T> fetchData(func: () -> T) = func()

    private suspend fun RecyclerView.setData(data: ArrayList<SearchModel>) {
        if (adapter is DiscoverAdapter) {
            val discoverAdapter = adapter as DiscoverAdapter
            withContext(Dispatchers.Main) {
                discoverAdapter.setSearchData(data)
            }
        }
    }
}
```

Este é apenas um exemplo de uso de corotinas e `AsyncTask` no nosso projeto. Existem vários outros no nosso código, utilizados sempre que precisamos mostrar na interface um recurso buscado atravéz da rede.

## Uso de APIs apropriadas

Utilizamos sempre as classes mais recentes disponíveis em android, e usamos sempre androidx. Procuramos usar RecyclerView sempre que possível.

Em um caso específico, em `DiscoverFragment`, temos uma lista vertical com várias `RecyclerViews`, cada uma exibindo seu conteúdo horizontalmente. Para melhorar a performance, utilizamos o método `RecyclerView.setRecycledViewPool()` para fazer com que as `RecyclerViews` fizessem sua reciclagem em conjunto, melhorando a performance especialmente do scroll vertical dessa tela:

```kotlin
private fun initializeDiscoverRecyclerView(recyclerViewID: Int): RecyclerView {
    val adapt = DiscoverAdapter(activity!!, myaaViewModel)
    val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    val rv = rootView.findViewById<RecyclerView>(recyclerViewID)
    rv.apply {
        layoutManager = manager
        adapter = adapt
        setRecycledViewPool(viewPool)
    }
    return rv
}
```