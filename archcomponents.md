# Architecture Components

## Watchlist

Nosso projeto faz uso extensivo de <i>Architecture Components</i>. A nossa motivação inicial em usar um banco de dados `Room` foi simplesmente armazenar a lista de animes que o usuário assiste, pretende assistir ou já assistiu - o que chamamos de <i>Watchlist</i>. A aplicação inteira faz uso da <i>Watchlist</i>, e acessa ela atravéz de uma ``ViewModel`` contendo um ``LiveData`` com toda a <i>Watchlist</i>, e várias funções para modificá-la.

## Recommendation

A API do [Jikan](https://jikan.docs.apiary.io) disponibiliza uma requisição que, dada um anime, retorna sugestões de animes semelhantes. Para elaborarmos a lista de recomendações de um usuário, fazemos a requisição por animes semelhantes no momento que um anime é adicionado à <i>Watchlist</i>, e guardamos a informação relevante da requisição em outra tabela no banco de dados. A lista de recomendações final é montada a partir de uma conglomeração de todas as recomendações de todos os animes presentes na <i>Watchlist</i> de um usuário.