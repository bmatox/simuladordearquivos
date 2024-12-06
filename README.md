# Simulador de Sistema de Arquivos com Journaling

## Sumário

- [Introdução](#introdução)
- [Metodologia](#metodologia)
- [Parte 1: Introdução ao Sistema de Arquivos com Journaling](#parte-1-introdução-ao-sistema-de-arquivos-com-journaling)
  - [Descrição do Sistema de Arquivos](#descrição-do-sistema-de-arquivos)
  - [Journaling](#journaling)
- [Parte 2: Arquitetura do Simulador](#parte-2-arquitetura-do-simulador)
  - [Estrutura de Dados](#estrutura-de-dados)
  - [Implementação do Journaling](#implementação-do-journaling)
- [Parte 3: Implementação em Java](#parte-3-implementação-em-java)
  - [Classe `FileSystemSimulator`](#classe-filesystemsimulator)
  - [Classes `Arquivo` e `Diretorio`](#classes-arquivo-e-diretorio)
  - [Classe `Journal`](#classe-journal)
- [Resultados Esperados](#resultados-esperados)
- [Como Executar o Simulador](#como-executar-o-simulador)
- [Evidências](#evidências)
  - [Conteúdo do Journal](#conteúdo-do-journal)
  - [Print do arquivo sistemaDeArquivos.dat](#conteúdo-do-arquivo-dat)
  - [Console (Entradas e Saídas)](#console-entradas-e-saídas)

---

## Introdução

Este projeto é um simulador de sistema de arquivos em Java que implementa funcionalidades básicas de manipulação de arquivos e diretórios, com suporte a **Journaling** para garantir a integridade dos dados. O simulador permite a criação de um arquivo que simula o sistema de arquivos e executa operações como copiar, apagar e renomear arquivos e diretórios, bem como listar o conteúdo de um diretório.

---

## Metodologia

O simulador foi desenvolvido na linguagem de programação **Java**. Ele recebe chamadas de métodos com os devidos parâmetros e implementa os métodos correspondentes aos comandos de um sistema operacional.

O programa executa cada funcionalidade e exibe o resultado na tela quando necessário.

---

## Parte 1: Introdução ao Sistema de Arquivos com Journaling

### Descrição do Sistema de Arquivos

Um **sistema de arquivos** é uma estrutura que um sistema operacional utiliza para controlar como os dados são armazenados e recuperados de um meio de armazenamento, como um disco rígido ou SSD. Ele organiza os dados em estruturas hierárquicas de arquivos e diretórios, facilitando o gerenciamento, o acesso e a manipulação das informações.

**Importância do Sistema de Arquivos:**

- **Organização de Dados:** Permite estruturar e organizar dados de forma eficiente.
- **Gerenciamento de Armazenamento:** Otimiza o uso do espaço disponível em dispositivos de armazenamento.
- **Segurança e Permissões:** Controla o acesso aos dados, garantindo privacidade e segurança.
- **Integridade dos Dados:** Previne a corrupção dos dados e assegura a recuperação em caso de falhas.

### Journaling

**Journaling** é uma técnica utilizada em sistemas de arquivos para manter a integridade dos dados, registrando todas as operações antes de aplicá-las efetivamente. Isso é realizado através de um log chamado **journal**.

**Propósito e Funcionamento:**

- **Recuperação após Falhas:** Em caso de interrupções inesperadas (como quedas de energia), o journal permite que o sistema identifique operações incompletas e recupere a consistência dos dados.
- **Tipos de Journaling:**
  - **Write-Ahead Logging (WAL):** Registra todas as operações pendentes antes de executá-las.
  - **Log-Structured File Systems:** Organiza todo o sistema de arquivos como um log contínuo.
  - **Journaling de Metadados:** Apenas metadados são registrados no journal.
  - **Full Journaling:** Tanto dados quanto metadados são registrados, oferecendo maior segurança.

---

## Parte 2: Arquitetura do Simulador

### Estrutura de Dados

Para representar o sistema de arquivos, utilizamos classes Java:

- **Classe `Arquivo`:** Representa um arquivo com atributos como nome, conteúdo, data de criação e modificação.
- **Classe `Diretorio`:** Representa um diretório contendo arquivos e subdiretórios.
- **Classe `FileSystem`:** Gerencia a estrutura hierárquica dos diretórios e arquivos, mantendo uma referência ao diretório raiz.

### Implementação do Journaling

O journaling é implementado através da classe `Journal`, que gerencia o log das operações.

- **Estrutura do Log:**
  - Cada operação é registrada com detalhes como tipo, caminho, timestamp e status (iniciada, concluída).
- **Operações Registradas:**
  - **Criação, exclusão e renomeação** de arquivos e diretórios.
- **Mecanismo:**
  - Antes de executar uma operação, ela é registrada no journal.
  - Após a conclusão, o status é atualizado para "concluída".
  - Em caso de falhas, o journal é utilizado para recuperar operações pendentes.

---

## Parte 3: Implementação em Java

### Classe `FileSystemSimulator`

Implementa o simulador do sistema de arquivos, incluindo métodos para cada operação:

- **Operações Implementadas:**
  - **Criar diretórios**
  - **Criar arquivos**
  - **Copiar arquivos**
  - **Apagar arquivos**
  - **Renomear arquivos**
  - **Renomear diretórios**
  - **Apagar diretórios**
  - **Listar arquivos de um diretório**

### Classes `Arquivo` e `Diretorio`

- **Classe `Arquivo`:**
  - **Atributos:**
    - `nome`: Nome do arquivo.
    - `conteudo`: Conteúdo do arquivo.
    - `dataCriacao`: Data e hora de criação.
    - `dataModificacao`: Data e hora da última modificação.
  - **Métodos:**
    - `renomear(String novoNome)`: Renomeia o arquivo.
    - `apagar()`: Apaga o arquivo.
    - `getConteudo()`: Retorna o conteúdo do arquivo.
  
- **Classe `Diretorio`:**
  - **Atributos:**
    - `nome`: Nome do diretório.
    - `arquivos`: Lista de arquivos contidos.
    - `subdiretorios`: Lista de subdiretórios.
    - `dataCriacao`: Data e hora de criação.
  - **Métodos:**
    - `adicionarArquivo(Arquivo arquivo)`: Adiciona um arquivo ao diretório.
    - `removerArquivo(String nomeArquivo)`: Remove um arquivo do diretório.
    - `adicionarDiretorio(Diretorio diretorio)`: Adiciona um subdiretório.
    - `removerDiretorio(String nomeDiretorio)`: Remove um subdiretório.
    - `renomear(String novoNome)`: Renomeia o diretório.
    - `listarConteudo()`: Lista o conteúdo do diretório.

### Classe `Journal`

Gerencia o log das operações realizadas:

- **Registro de Operações:**
  - Cada operação é registrada com detalhes e status.
- **Recuperação:**
  - Permite identificar e recuperar operações não concluídas em caso de eventos inesperados.

---

## Resultados Esperados

Espera-se que o simulador:

- **Reproduza as Operações Básicas:**
  - Permita realizar operações comuns de sistemas de arquivos de forma similar a um sistema real.
- **Garanta a Integridade dos Dados:**
  - Utilize o journaling para manter a consistência e integridade dos dados, mesmo em casos de falhas ou interrupções inesperadas.
- **Forneça Aprendizado Prático:**
  - Auxilie na compreensão dos conceitos de sistemas de arquivos e a importância do journaling, oferecendo uma ferramenta prática para experimentação.

---

## Como Executar o Simulador

1. **Pré-requisitos:**
   - **Java Development Kit (JDK)** instalado (versão 8 ou superior).
   - Um ambiente de desenvolvimento Java (como **IntelliJ IDEA** ou **Eclipse**), ou acesso ao terminal para compilar e executar programas Java.

2. **Clonar o Repositório:**
   ```bash
   git clone [https://github.com/seu-usuario/seu-repositorio.git](https://github.com/bmatox/simuladordearquivos.git)

## Evidências

### Conteúdo do Journal

![Captura de tela 2024-12-06 003526](https://github.com/user-attachments/assets/e773ddbf-bbe9-4351-882f-8deff769f66d)

### Print do arquivo sistemaDeArquivos.dat

![Captura de tela 2024-12-06 003552](https://github.com/user-attachments/assets/556992d5-e906-4a65-aeb8-106081fa7c29)

### Console (Entradas e Saídas)

![Console (Entradas e Saídas)](caminho/para/o/arquivo/console.png)
