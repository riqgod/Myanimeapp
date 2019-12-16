### Bateria

## Avaliação de desempenho

Executando o Profiler, notamos que a causa por trás da grande maioria do consumo de energia de nossa aplicação era o uso da rede. Portanto, nossas estratégias para reduzir o consumo de rede são também nossas principais estratégias para aliviar o consumo da bateria.

## Cache

Nosso aplicativo faz uso duas caches: o objeto `Cache` implementado por nós para requisições realizadas ao [Jikan](https://jikan.docs.apiary.io), e a cache automática presente na biblioteca [Picasso](https://square.github.io/picasso/). Apesar de nossa motivação primária em utilizar estas caches ser economizar no uso de recurso de redes, o uso destas caches também é benéfico quanto ao consumo de energia da aplicação.

## Logging

Durante o desenvolvimento, fizemos uso extensivo de logging de vários tipos para nos manter informados sobre o estado da aplicação e entender quais erros ele lançava. Nos momentos finais antes da entrega deste relatório, comentamos todos os logs.

A API [Jikan4Java](https://github.com/Doomsdayrs/Jikan4java/), que utilizamos extensivamente no nosso projeto, imprime atravéz de `System.out.println` uma mensagem para toda requisição realizada, e não providencia nenhuma forma de desabilitar este logging. Utilizamos [esta](https://stackoverflow.com/questions/9882487/disable-system-out-for-speed-in-java) solução do StackOverflow para desativar o uso de `System.out.println`, e assim eliminamos todo o logging da nossa aplicação.

## Preferências: mobile data vs Wi-Fi

Visto que o carregamento de imagens é responsável pela grande maioria do consume de rede do nosso aplicativo, incluímos na tela de `SettingsFragment` que o desabilita quando o celular não tem conexão Wi-Fi. Apesar de nossa motivação primária em disponibilizar este preferência ser economizar no uso de recurso de redes, o uso desta preferência também é benéfico quanto ao consumo de energia da aplicação.
