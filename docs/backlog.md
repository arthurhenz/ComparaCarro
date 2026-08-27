
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

✅ Filtro no Favoritos
✅ Lista mockada dos mais vendidos na pagina inicial utilizando IA para extrair os carros mais 
vendidos Fenabrave enviados anterioprmente
✅ Login com email e senha, google, e recuperar a senha. Tudo isso com firebase
✅ Flow e Pagination no Room
✅ Esqueci minha senha, cadastro.
✅ Shimmer effect as backbone


Esta semana:

✅ Mudar icone da tela de login para a oficial Webp do Google
✅ Favoritos botao mudar botar text button ver mais em baixo pro preco aparecer cheio
✅ shimmerEffect funcionando corretamente
✅ Adicionar termos de uso no campo de cadastro
✅ Pegar foto de perfil do Google, se nao tiver, colocar 2 iniciais do nome primeira e ultima
✅ Chevron e verde e vermelho na comparação


✅ Remover Cadastrar com Google do Cadastro
✅ Jogar no Claude Design e pedir pra dar uma melhorada
Melhorar validacao de cadastro com regex

Proxima semana:

✅ Desativar o Test Drive
✅ Colocar quantidade de modelos na Search nao na tela inicial
✅ Trazer de volta a barra de navegacao pra tela de perfil
✅ Tirar botao de Compartilhar
✅ Definir 3 linhas no maximo com ellipsis
✅ Abandonar Graphite
✅ Ajustar tint do shimmer pra ser do theme do projeto
✅ Coloque Back Button em cima na esquerda na tela de Login (popup) e usar a mesma lambda do clickable "Ja possui conta? Entrar", ambos devendo ser popup
✅ Consertar StatusBar
✅ Ajustar DarkTheme e LightTheme
✅ Tirar verde vermelho colocar vantagem em laranja no Compare
✅ Consertar botao Ver Detalhes quando o content é muito height
✅ Colocar imagem nos Favoritos dos carros e colocar placeholder sem ser o cinza


(Telus)
- Connectar Jira e Github MCP
- Slack com Claude

- Como armazenar tokens de forma segura no Android em 2026
- Revisar FavoriteScreen com textbutton em baixo na direita
- Pegar os dados que realmente importam na comparacao
- Fazer traducao do Ciclo de vida da comparacao
- Dar uma olhada em lancar app na loja (india)
- Obfuscação de codigo https://developer.android.com/topic/performance/app-optimization/enable-app-optimization

- Adaptive layout

- Descartar Graphite  usar Github Stack

Melhorias:

Validacao de campo a campo no Cadastro, nao um state unico em baixo

Chamada de API para Repository se faltar dados (ver o uso de camada de dados (data sources) local e 
remote no Android)

Adicionar persistencia com o Backend e cache

Crawler para pegar imagens dessa API para todos os carros (https://api.auto-data.net/documentation)

OTP-like pra validar email com built-in Firebase para cadastro

Crawler Fenabrave