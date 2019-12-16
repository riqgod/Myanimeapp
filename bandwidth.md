# Consumo de Rede

## Avaliação de desempenho

Executando o Android Profiler, constatamos que o carregamento de imagens consiste na maioria esmagadora de nosso uso de recursos de rede. O carregamento da imagem 16x9 exibida em `AnimeDetailActivity` se mostra particularmente pesado.

Nossa aplicação faz uso  de recursos de rede com duas finalidades principais: acessar a API [Jikan](https://jikan.docs.apiary.io) para buscar dados relacionados aos animes, para qual utilizamos sua API em java [Jikan4Java](https://github.com/Doomsdayrs/Jikan4java/), e buscar imagens a serem mostradas em ImageViews, para qual usamos [Picasso](https://square.github.io/picasso/).

Apesar do nosso acesso ao Jikan consumir relativamente poucos recursos de rede, ainda precisamos ser econômicos ao acessá-lo, pois a API permite um máximo de apenas 2 requisições por segundo, e 30 requisições por minuto. Utilizamos uma cache para minimizar a quantidade de requisições que fazemos ao Jikan.

## Cache

O singleton `Cache` e a classe `CacheItem` implementam uma cache que utilizamos para todas as requisições feitas ao Jikan. A cache armazena os resultados de até 20 requisições, utiliza uma política de substituição LRU, e utiliza funções de hash simples implementadas por nós para identificar itens armazenados na cache.

O uso da cache não influenciou muito no consumo de rede da nossa aplicação, visto que o carregamento de imagens não passa por esta cache. Porém, a redução no número de requisição ao Jikan e a rapidez maior ao buscar recursos já presentes na cache providenciam uma melhoria significativa na UX.

## Picasso

A biblioteca Picasso, que usamos para carregar imagens da internet no nosso aplicativo, faz uso automático de sua própria cache. Configurando o Picasso para não utilizar sua cache (atravéz de `Picasso.Builder.memoryCache(Cache.None)`), constatamos que, sem a cache do Picasso, o acesso à rede do nosso aplicativo aumenta drasticamente.

## Preferências: mobile data vs Wi-Fi

Tendo constatado que o carregamento de imagens constitui na maior fonte de consumo de rede do nosso aplicativo, providenciamos uma preferência na `SettingsFragment` que permite desabilitar o carregamento de imagens quando o celular não está conectado à Wi-Fi. Como esperado, esta preferência alivia bastante o peso do aplicativo na rede.