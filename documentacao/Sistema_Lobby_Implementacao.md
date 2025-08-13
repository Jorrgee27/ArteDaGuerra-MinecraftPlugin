# Sistema de Lobby das 7 Eras - Documentação de Implementação

## Visão Geral
Este documento detalha a implementação completa do Sistema de Lobby temático para o plugin "Arte da Guerra", que permite aos jogadores navegar através de 7 eras históricas da humanidade.

## Arquivos Criados/Modificados

### 1. Classe Principal
- **ArteDaGuerraCore.java** - Classe principal do plugin atualizada
  - Integração com o sistema de lobby
  - Registro de comandos e eventos
  - Validação de configuração
  - Comandos administrativos

### 2. Sistema de Lobby
- **LobbyManager.java** - Gerenciador principal do lobby
  - Criação automática do mundo do lobby
  - Estruturas temáticas das 7 eras
  - Sistema de teletransporte com cooldown
  - Controle de acesso às eras
  - Fornecimento de itens de navegação

- **LobbyCommand.java** - Executor de comandos
  - `/lobby` - Teletransporte para o lobby
  - `/era <número>` - Teletransporte para era específica
  - `/eras` - Listagem de eras disponíveis

- **LobbyListener.java** - Gerenciador de eventos
  - Proteção do lobby (blocos, dano, fome)
  - Interações com itens de navegação
  - Detecção de proximidade com eras
  - Proteção de inventário

- **LobbyGUI.java** - Interface gráfica
  - Menu de seleção de eras
  - Menu de informações do projeto
  - Sistema de navegação visual
  - Gerenciamento de cliques

### 3. Configurações
- **plugin.yml** - Atualizado com novos comandos e permissões
- **config.yml** - Configurações completas do sistema

## Estrutura das 7 Eras

### Era 1: Primitiva (3.000.000 - 10.000 a.C.)
- **Material Base**: Dirt
- **Estrutura**: Fogueira (Campfire)
- **Acesso**: Liberado por padrão
- **Mundo**: era_primitiva

### Era 2: Pós-Primitiva (10.000 - 3.000 a.C.)
- **Material Base**: Stone
- **Estrutura**: Altar de pedra com tocha
- **Acesso**: Liberado por padrão
- **Mundo**: era_pos_primitiva

### Era 3: Medieval (476 - 1.453 d.C.)
- **Material Base**: Cobblestone
- **Estrutura**: Torre de pedra
- **Acesso**: Liberado por padrão
- **Mundo**: era_medieval

### Era 4: Industrial (1.760 - 1.840)
- **Material Base**: Iron Block
- **Estrutura**: Chaminé de tijolos
- **Acesso**: Restrito (permissão necessária)
- **Mundo**: era_industrial

### Era 5: Moderna (1.900 - 1.990)
- **Material Base**: White Concrete
- **Estrutura**: Prédio moderno
- **Acesso**: Restrito (permissão necessária)
- **Mundo**: era_moderna

### Era 6: Contemporânea (1.990 - 2.020)
- **Material Base**: Redstone Block
- **Estrutura**: Antena de comunicação
- **Acesso**: Restrito (permissão necessária)
- **Mundo**: era_contemporanea

### Era 7: Futurista (2.020+)
- **Material Base**: Diamond Block
- **Estrutura**: Portal futurista
- **Acesso**: Restrito (permissão necessária)
- **Mundo**: era_futurista

## Comandos e Permissões

### Comandos Principais
- `/artedaguerra` - Comando principal com subcomandos
- `/lobby` - Teletransporte para o lobby
- `/era <número>` - Teletransporte para era específica
- `/eras` - Lista todas as eras disponíveis

### Sistema de Permissões
- `artedaguerra.*` - Acesso completo
- `artedaguerra.admin` - Permissões administrativas
- `artedaguerra.lobby` - Acesso ao comando /lobby
- `artedaguerra.era` - Acesso ao comando /era
- `artedaguerra.eras` - Acesso ao comando /eras
- `artedaguerra.era.1` até `artedaguerra.era.7` - Acesso específico por era

## Itens de Navegação

### Compass (Bússola)
- **Nome**: Menu das Eras
- **Função**: Abre interface gráfica de seleção
- **Slot**: 4 (centro do hotbar)

### Book (Livro)
- **Nome**: Informações do Projeto
- **Função**: Exibe detalhes sobre o plugin
- **Slot**: 8 (último slot do hotbar)

## Funcionalidades Implementadas

### 1. Sistema de Proteção
- Prevenção de quebra/colocação de blocos
- Proteção contra dano e fome
- Bloqueio de drop de itens
- Proteção de inventário

### 2. Sistema de Teletransporte
- Cooldown configurável (padrão: 3 segundos)
- Verificação de permissões
- Mensagens informativas
- Controle de acesso por era

### 3. Interface Gráfica
- Menu visual de seleção de eras
- Indicadores de acesso (verde/vermelho)
- Informações detalhadas de cada era
- Sistema de navegação intuitivo

### 4. Criação Automática de Estruturas
- Plataforma central com beacon
- Áreas temáticas para cada era
- Estruturas representativas do período
- Layout circular organizado

### 5. Sistema de Proximidade
- Detecção automática de aproximação
- Exibição de informações na action bar
- Indicação de status de acesso
- Feedback visual em tempo real

## Status de Compilação

### ✅ Compilação Bem-sucedida
- **Maven**: `mvn clean compile` - SUCCESS
- **Packaging**: `mvn package` - SUCCESS
- **Arquivo Gerado**: `ArteDaGuerra-1.0.0-SNAPSHOT-shaded.jar`
- **Tamanho**: Aproximadamente 50KB

### Correções Realizadas
- Substituição de `Material.CONCRETE` por `Material.WHITE_CONCRETE`
- Substituição de `Material.COMPUTER` por `Material.REDSTONE_BLOCK`
- Compatibilidade com Minecraft 1.21

## Próximos Passos

### 1. Implementação das Eras Individuais
- Criação dos plugins específicos de cada era
- Desenvolvimento de mecânicas únicas
- Sistema de progressão entre eras

### 2. Sistema de Economia
- Integração com Vault
- Moedas específicas por era
- Sistema de comércio temporal

### 3. Sistema de Combate
- Armas e armaduras temáticas
- Mecânicas de combate por era
- Balanceamento progressivo

### 4. Eventos Temporais
- Eventos históricos automáticos
- Invasões e guerras
- Progressão da civilização

### 5. Sistema de Construção
- Blocos e materiais específicos
- Tecnologias desbloqueáveis
- Arquitetura temática

## Configurações Recomendadas

### Servidor
- **Versão**: Spigot/Paper 1.21+
- **RAM**: Mínimo 2GB
- **Plugins Recomendados**: WorldEdit, WorldGuard, Vault

### Mundos
- **Lobby**: Mundo flat para melhor performance
- **Eras**: Mundos customizados por período
- **Backup**: Sistema automático recomendado

## Conclusão

O Sistema de Lobby das 7 Eras foi implementado com sucesso, fornecendo uma base sólida para o desenvolvimento futuro do plugin "Arte da Guerra". O sistema oferece:

- ✅ Navegação intuitiva entre eras
- ✅ Interface gráfica completa
- ✅ Sistema de proteção robusto
- ✅ Controle de acesso granular
- ✅ Estruturas temáticas automáticas
- ✅ Compatibilidade com Minecraft 1.21

O projeto está pronto para a próxima fase de desenvolvimento, que incluirá a implementação das mecânicas específicas de cada era histórica.

---

**Versão do Documento**: 1.0.0  
**Data de Criação**: Dezembro 2024  
**Autor**: Arte da Guerra Team  
**Status**: Implementação Completa