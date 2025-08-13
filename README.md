# Arte da Guerra - Plugin Minecraft 1.21

## 🎮 Sobre o Projeto

**Arte da Guerra** é um plugin inovador para Minecraft que simula a evolução da humanidade através de 7 eras históricas distintas. Os jogadores podem explorar diferentes períodos da história, desde a era primitiva até o futuro, cada um com suas próprias tecnologias, mecânicas e desafios únicos.

## 🏛️ As 7 Eras Históricas

### 1. Era Primitiva (3.000.000 - 10.000 a.C.)
- **Tecnologias**: Ferramentas de pedra, fogo, caça
- **Estrutura**: Cavernas primitivas
- **Material Temático**: Cobblestone

### 2. Era Pós-Primitiva (10.000 - 3.000 a.C.)
- **Tecnologias**: Agricultura, domesticação, cerâmica
- **Estrutura**: Casas simples de madeira
- **Material Temático**: Oak Wood

### 3. Era Medieval (476 - 1.453 d.C.)
- **Tecnologias**: Metalurgia, arquitetura, feudalismo
- **Estrutura**: Castelos medievais
- **Material Temático**: Stone Bricks

### 4. Era Industrial (1.760 - 1.840)
- **Tecnologias**: Máquinas a vapor, produção em massa
- **Estrutura**: Fábricas industriais
- **Material Temático**: Iron Block

### 5. Era Moderna (1.900 - 1.990)
- **Tecnologias**: Eletricidade, automóveis, aviação
- **Estrutura**: Cidades modernas
- **Material Temático**: White Concrete

### 6. Era Contemporânea (1.990 - 2.020)
- **Tecnologias**: Computadores, internet, biotecnologia
- **Estrutura**: Centros tecnológicos
- **Material Temático**: Redstone Block

### 7. Era Futurista (2.020+)
- **Tecnologias**: IA, nanotecnologia, exploração espacial
- **Estrutura**: Bases espaciais
- **Material Temático**: Quartz Block

## 🚀 Funcionalidades Implementadas

### Sistema de Lobby Temático
- **Mundo dedicado** com representações visuais das 7 eras
- **Estruturas temáticas** para cada período histórico
- **Sistema de navegação** intuitivo entre as eras
- **Proteções completas** do ambiente do lobby

### Comandos Disponíveis
- `/lobby` - Teletransporta para o lobby principal
- `/era <numero>` - Acessa uma era específica (1-7)
- `/eras` - Lista todas as eras disponíveis

### Interface Gráfica
- **Menu interativo** de seleção de eras
- **Informações detalhadas** de cada período
- **Itens de navegação** automáticos (bússola e livro)

### Sistema de Permissões
- Controle granular de acesso às eras
- Permissões específicas por comando
- Sistema de progressão configurável

## 🛠️ Instalação e Configuração

### Requisitos
- **Minecraft**: 1.21
- **Servidor**: Spigot/Paper 1.21
- **Java**: 21 ou superior

### Dependências Opcionais
- **Vault** - Sistema de economia
- **PlaceholderAPI** - Placeholders personalizados
- **WorldEdit** - Edição avançada de mundos
- **WorldGuard** - Proteções adicionais
- **LuckPerms** - Gerenciamento de permissões

### Instalação
1. Baixe o arquivo `ArteDaGuerra-1.0.0-SNAPSHOT.jar`
2. Coloque o arquivo na pasta `plugins/` do seu servidor
3. Reinicie o servidor
4. Configure as permissões conforme necessário

## ⚙️ Configuração

### Permissões Principais
```yaml
artedaguerra.lobby      # Acesso ao comando /lobby
artedaguerra.era        # Acesso ao comando /era
artedaguerra.eras       # Acesso ao comando /eras
artedaguerra.era.1      # Acesso à Era Primitiva
artedaguerra.era.2      # Acesso à Era Pós-Primitiva
# ... e assim por diante para todas as eras
```

### Arquivo de Configuração
O plugin gera automaticamente um arquivo `config.yml` com todas as configurações necessárias.

## 🏗️ Arquitetura do Projeto

### Estrutura de Classes
- **ArteDaGuerraCore** - Classe principal do plugin
- **LobbyManager** - Gerenciamento do sistema de lobby
- **LobbyCommand** - Processamento de comandos
- **LobbyListener** - Eventos e proteções
- **LobbyGUI** - Interface gráfica

### Padrões Utilizados
- **Singleton** para gerenciadores principais
- **Observer** para sistema de eventos
- **Command Pattern** para processamento de comandos
- **Factory** para criação de itens e estruturas

## 🔄 Roadmap de Desenvolvimento

### ✅ Fase 1 - Sistema de Lobby (Concluída)
- [x] Criação do mundo do lobby
- [x] Estruturas temáticas das 7 eras
- [x] Sistema de comandos
- [x] Interface gráfica
- [x] Sistema de proteções

### 🚧 Fase 2 - Mundos das Eras (Em Planejamento)
- [ ] Criação de mundos específicos para cada era
- [ ] Geração procedural de terrenos temáticos
- [ ] Estruturas e construções automáticas
- [ ] NPCs e entidades específicas

### 📋 Fase 3 - Mecânicas de Gameplay (Futuro)
- [ ] Sistema de combate por era
- [ ] Tecnologias e crafting específicos
- [ ] Sistema de economia temporal
- [ ] Eventos dinâmicos
- [ ] Sistema de equipes e territórios

## 🤝 Contribuição

Este projeto segue as **Regras Universais de Desenvolvimento** estabelecidas:

1. **Compilação exclusiva** com Maven
2. **Tolerância zero** a erros de compilação
3. **Excelência em design** de interface
4. **Documentação completa** de todas as implementações
5. **Versionamento** com Git e GitHub

## 📝 Licença

Este projeto é desenvolvido para fins educacionais e de entretenimento.

## 📞 Contato

Para dúvidas, sugestões ou contribuições, entre em contato através dos canais oficiais do projeto.

---

**Versão**: 1.0.0-SNAPSHOT  
**Data de Lançamento**: 13/08/2025  
**Compatibilidade**: Minecraft 1.21  
**Status**: ✅ Sistema de Lobby Implementado e Funcional