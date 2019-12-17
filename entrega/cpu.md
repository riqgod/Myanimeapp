# CPU & Performance

## Avaliação de desempenho

Segundo o Android Profiler, nossa aplicação não consume muita CPU. Nossa utilização de CPU nunca ultrapassou valores denotados como "medium" no Profiler. Portanto, não fizemos alteração ao nosso código com base nos resultados do Profiler.

A seguir, listamos algumas boas práticas que adotamos durante o desenvolvimento.

## Multithreading e corotinas

Devido ao acesso contínuo a recursos de rede que precisam ser carregados e então mostrados na interface, nosso projeto faz uso ubíquito de `AsyncTask` e várias funcionalidades de corotinas do Kotlin. A seguir, um extrato da classe `DiscoverFragment` mostra o disparo de um único `AsyncTask` que, atravéz de corotinas, faz em paralelo quatro requisições à rede, e povoa quatro `RecyclerViews` com os dados retornados:

```kotlin
val seasonalRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_seasonal)
val seasonalLoadingView = rootView.findViewById<View>(R.id.dc_loading_seasonal)
val seasonalTriple = Triple(seasonalRecyclerView, seasonalLoadingView, fetchCurrentSeason)

val topUpcomingRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_top_upcoming)
val topUpcomingLoadingView = rootView.findViewById<View>(R.id.dc_loading_top_upcoming)
val topUpcomingTriple = Triple(topUpcomingRecyclerView, topUpcomingLoadingView, fetchTopUpcoming)

val mostPoplarRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_mp)
val mostPoplarLoadingView = rootView.findViewById<View>(R.id.dc_loading_mp)
val mostPoplarTriple = Triple(mostPoplarRecyclerView, mostPoplarLoadingView, fetchMostPoplar)

val topScoreRecyclerView = initializeDiscoverRecyclerView(R.id.dc_rv_score)
val topScoreLoadingView = rootView.findViewById<View>(R.id.dc_loading_score)
val topScoreTriple = Triple(topScoreRecyclerView, topScoreLoadingView, fetchTopScore)

val triples = arrayOf(seasonalTriple, topUpcomingTriple, mostPoplarTriple, topScoreTriple)
AsyncDataSetter().execute(*triples)
```

A seguir, o código do respectivo `AsyncTask`:

```kotlin
private class AsyncDataSetter: AsyncTask<Triple<RecyclerView, View, () -> ArrayList<SearchModel>>, Void, Void>() {
    override fun doInBackground(vararg params: Triple<RecyclerView, View, () -> ArrayList<SearchModel>>?): Void? {
        runBlocking {
            params.forEach { triple ->
                launch {
                    if (triple != null) {
                        val recyclerView = triple.first
                        val loadingView = triple.second
                        val data = withContext(Dispatchers.Unconfined) { fetchData(triple.third) }
                        withContext(Dispatchers.Main) {
                            recyclerView.setData(data)
                            loadingView.visibility = View.GONE
                        }
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