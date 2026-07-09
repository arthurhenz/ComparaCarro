
✅/🙈 Utilizar androidx-material-icons-extended icons no projeto Outlined default Filled selecionado

✅ Pegue minha tabela fipe.csv e faca o download de cada carro no formato 512x512 e salve no meu res.dradable 

✅ migrar pro AGP 9 usando google skill https://github.com/android/skills/tree/main/build/agp/agp-9-upgrade

✅ Migrar id para alias e string projects para type accessors project

✅ Criar pastinha specs   

✅ Mover namespaces dos modulos para a Project.configureKotlinAndroid

TUDO SOBRE TOKENIZACAO E TEMAS DEVE FICAR AQUI ENTAO PROPORTIONS ETC COLO CAR AQUI Theme.Spacing.Small

Estrategias de IA:
- Usar um bot para revisar cada PR (vou revisar em paralelo)
- Apos mergear, se tiver conflito no proximo PR, usar um bot para resolver os conflitos
- Mergear na main



Integrar funcionalidade de comparar com API sem imagens mas que possa aparecer o comparativo de dois carros lado a lado somente os textos
Listagem inicial de 16 carros. Usar placeholder


1. Integracao da API de FIPE https://deividfortuna.github.io/fipe/v2/ para listagem com imagem https://carimagesapi.com e dados das fipes
2. Desenvolver feature de comparar usando dados previos da api com as imagens
3. Desenvolver um backend so para caching dos carros [FUTURO]

Feature: Grafico de variacao de preço

em vez de passar carro para uma tela, pode ter carArgs, que faz map pro novo objeto e ai sim passa o objeto novo


✅ Implementar feature de pesquisas em geral. Tanto no inicial quanto no comparativo.
✅ Sticky header lazycolumn para fixar um item
✅ Icone de chevron dropdown
✅ Estudar Room (core:database)(nao usar IA pra teoria) (poder cachear imagens etc dados)
✅ Repository (mediator que fala se pega de cache ou database):


Semana que vem:

- Filtro no Favoritos
- Lista mockada dos mais vendidos na pagina inicial utilizando IA para extrair os carros mais 
vendidos Fenabrave enviados anterioprmente
- Adicionar access token refresh token ARGON 2015 e implementar tela de login (Argon2 ou bcrypt 
kotlin da vida)
- Login com email e senha, google, e recuperar a senha. Tudo isso com firebase
✅ Flow e Pagination no Room


Melhorias: 

Chamada de API para Repository se faltar dados (ver o uso de camada de dados (data sources) local e 
remote no Android)

Adicionar persistencia com o Backend e cache

Remover espaco extra dos Cards e deixar o espaco mais eficiente

Pesquisar nomes alternativos pro titulo

Colocar Carousel de patrocinadores abaixo de algum conteudo principal mockado

Colocar Dropdowwn tela inicial com orderBy da API

SelectCard mudar a fonte do branco pro cinza e mudar a cor do Checkbox

Indepotencia

Tentar achar mais populares na API de FIPE

Pra semana que vem, tentar extrair a lista mais populares/mais vendidos e por ano. 

Na initial screen, mostrar Mais Vendidos. Eliminar dropdown posteriormente

Crawler Fenbabrave

Shimmer effect as backbone

Sortby nos favoritos (ano preco ordem alfabetca)

