# Simulador de Sistema de Arquivos com Journaling

## Sumário

- [Introdução](#introdução)
- [Metodologia](#metodologia)
- [Parte 1: Introdução ao sistema de arquivos com Journaling](#parte-1-introdução-ao-sistema-de-arquivos-com-journaling)
  - [Descrição do sistema de arquivos](#descrição-do-sistema-de-arquivos)
  - [Journaling](#journaling)
- [Parte 2: Arquitetura do simulador](#parte-2-arquitetura-do-simulador)
  - [Estrutura de dados](#estrutura-de-dados)
  - [Implementação do Journaling](#implementação-do-journaling)
  - [Arquivo .dat](#arquivo-dat)
- [Parte 3: Implementação em Java](#parte-3-implementação-em-java)
  - [Classe `FileSystemSimulator`](#classe-filesystemsimulator)
  - [Classes `Arquivo` e `Diretorio`](#classes-arquivo-e-diretorio)
  - [Classe `Journal`](#classe-journal)
- [Resultados esperados](#resultados-esperados)
- [Como executar o simulador](#como-executar-o-simulador)
- [Evidências](#evidências)
  - [Conteúdo do Journal](#conteúdo-do-journal)
  - [Print do arquivo sistemaDeArquivos.dat](#conteúdo-do-arquivo-dat)
  - [Prints do console (Entradas e Saídas)](#console-entradas-e-saídas)

---

## Introdução

Este projeto é um simulador de sistema de arquivos em Java que implementa funcionalidades básicas de manipulação de arquivos e diretórios, com suporte a **Journaling** para garantir a integridade dos dados. O simulador permite a criação de um arquivo que simula o sistema de arquivos e executa operações como copiar, apagar e renomear arquivos e diretórios, bem como listar o conteúdo de um diretório.

---

## Metodologia

O simulador foi desenvolvido na linguagem de programação **Java**. Ele recebe chamadas de métodos com os devidos parâmetros e implementa os métodos correspondentes aos comandos de um sistema operacional.

O programa executa cada funcionalidade e exibe o resultado na tela quando necessário.

---

## Parte 1: Introdução ao sistema de arquivos com Journaling

### Descrição do Sistema de Arquivos

Um **sistema de arquivos** é uma estrutura que um sistema operacional utiliza para controlar como os dados são armazenados e recuperados de um meio de armazenamento, como um disco rígido ou SSD. Ele organiza os dados em estruturas hierárquicas de arquivos e diretórios, facilitando o gerenciamento, o acesso e a manipulação das informações.

**Importância do sistema de arquivos:**

- **Organização de dados:** Permite estruturar e organizar dados de forma eficiente.
- **Gerenciamento de armazenamento:** Otimiza o uso do espaço disponível em dispositivos de armazenamento.
- **Segurança e permissões:** Controla o acesso aos dados, garantindo privacidade e segurança.
- **Integridade dos dados:** Previne a corrupção dos dados e assegura a recuperação em caso de falhas.

### Journaling

**Journaling** é uma técnica utilizada em sistemas de arquivos para manter a integridade dos dados, registrando todas as operações antes de aplicá-las efetivamente. Isso é realizado através de um log chamado **journal**.

**Propósito e Funcionamento:**

- **Recuperação após falhas:** Em caso de interrupções inesperadas (como quedas de energia), o journal permite que o sistema identifique operações incompletas e recupere a consistência dos dados.
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

### Arquivo `.dat`

O arquivo `.dat` no contexto deste simulador de sistema de arquivos em Java é um **contêiner binário** que armazena a estrutura do sistema de arquivos, incluindo todos os diretórios e arquivos criados pelo usuário. Este arquivo é essencial para a persistência dos dados, permitindo que o sistema de arquivos mantenha seu estado entre as execuções do programa.

#### Conteúdo do arquivo `.dat`

O arquivo `.dat` contém dois principais tipos de dados:

1. **Estrutura do sistema de arquivos:**
   - A estrutura hierárquica de diretórios e arquivos é serializada e armazenada no arquivo. Isso inclui todas as informações sobre diretórios (como nome, lista de arquivos e subdiretórios) e arquivos (como nome, posição e tamanho no arquivo contêiner, data de criação e modificação).
   
2. **Conteúdos dos arquivos:**
   - Os dados reais dos arquivos (o conteúdo) são armazenados em posições específicas dentro do arquivo `.dat`. Cada arquivo sabe a posição inicial e o tamanho de seu conteúdo dentro do arquivo contêiner, permitindo a leitura e escrita eficientes.

#### Funcionamento

##### 1. Serialização da estrutura do sistema de arquivos

A estrutura do sistema de arquivos é representada por objetos Java (`Diretorio` e `Arquivo`) que são serializados e armazenados no arquivo `.dat`. A serialização converte esses objetos em uma sequência de bytes que podem ser salvos e posteriormente desserializados para recriar os objetos originais.

##### 2. Posições dos conteúdos dos arquivos

Cada arquivo no sistema de arquivos tem atributos que indicam a posição inicial e o tamanho de seu conteúdo dentro do arquivo `.dat`. Isso permite que o simulador leia e escreva diretamente no arquivo contêiner sem necessidade de carregar todo o conteúdo em memória.

##### 3. Processo de escrita

Quando um arquivo é criado ou modificado, seu conteúdo é escrito em uma posição específica dentro do arquivo `.dat`. O simulador garante que a posição correta seja calculada e que não haja sobreposição de dados. Após a escrita, a estrutura do sistema de arquivos é atualizada e salva novamente.

##### 4. Processo de leitura (Não implementado aqui)

#### Importância do arquivo `.dat`

- **Persistência dos dados:** O arquivo `.dat` permite que os dados do sistema de arquivos sejam persistentes entre as execuções do programa. Toda vez que o simulador é iniciado, ele carrega a estrutura do sistema de arquivos a partir deste arquivo.
- **Eficiência:** Ao armazenar tanto a estrutura quanto o conteúdo dos arquivos de forma serializada e referenciada, o arquivo `.dat` permite operações de leitura e escrita eficientes.
- **Integridade dos dados:** Utilizando técnicas de journaling, o simulador garante que as operações sejam registradas e que a estrutura do sistema de arquivos permaneça consistente, mesmo em caso de falhas ou interrupções.
  
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

## Resultados esperados

Espera-se que o simulador:

- **Reproduza as operações básicas:**
  - Permita realizar operações comuns de sistemas de arquivos de forma similar a um sistema real.
- **Garanta a integridade dos dados:**
  - Utilize o journaling para manter a consistência e integridade dos dados, mesmo em casos de falhas ou interrupções inesperadas.
- **Persistência dos dados:**
  -  Utilize um arquivo .dat para armazenar a estrutura do sistema de arquivos e os conteúdos dos arquivos, garantindo que o estado do sistema de arquivos seja preservado entre as execuções do programa.
  -  Carregue a estrutura do sistema de arquivos a partir do arquivo .dat ao iniciar o simulador.
---

## Como executar o simulador

1. **Pré-requisitos:**
   - **Java Development Kit (JDK)** instalado (versão 8 ou superior).
   - Um ambiente de desenvolvimento Java (como **IntelliJ IDEA** ou **Eclipse**), ou acesso ao terminal para compilar e executar programas Java.

2. **Clonar o Repositório:**
   ```bash
   git clone https://github.com/bmatox/simuladordearquivos.git
   ```

3. **Compilar o Projeto:**
   - Navegue até o diretório do projeto:
     ```bash
     cd simuladordearquivos
     ```
   - Compile as classes Java:
     ```bash
     javac simulador/*.java
     ```

4. **Executar o Simulador:**
   - No terminal, execute o programa:
     ```bash
     java simulador.Main
     ```
   - Ou, se estiver usando um IDE, execute a classe `Main` diretamente.

5. **Interagir com o Simulador:**
   - Após executar o simulador, você verá um menu com várias opções no console.
   - Para realizar qualquer operação, é importante começar com `/raiz` ao especificar caminhos de diretórios. Por exemplo, ao criar um diretório ou arquivo, o caminho deve iniciar com `/raiz`.

6. **Exemplo - Criar um Diretório:**
   - No menu, escolha a opção para criar um diretório digitando `1` e pressionando `Enter`.
   - Quando solicitado, insira o caminho do novo diretório, iniciando com `/raiz`:
     ```plaintext
     Caminho do novo diretório: /raiz/meuNovoDiretório
     ```
   - O simulador confirmará a criação do diretório se tudo estiver correto.

---

## Evidências

### Conteúdo do Journal

![Conteúdo do Journal](https://github.com/user-attachments/assets/e773ddbf-bbe9-4351-882f-8deff769f66d)

### Print do arquivo sistemaDeArquivos.dat

![Print do arquivo sistemaDeArquivos.dat](https://github.com/user-attachments/assets/556992d5-e906-4a65-aeb8-106081fa7c29)

### Prints do console (Entradas e Saídas)

![Prints do console (Entradas e Saídas)](https://github.com/user-attachments/assets/ca069e39-4bad-4ccc-8237-09384162f2c3)
![Prints do console (Entradas e Saídas)](https://github.com/user-attachments/assets/654e0279-1e69-4171-9e8e-701dd5a0b3c6)
![Prints do console (Entradas e Saídas)](https://github.com/user-attachments/assets/75bbdf61-0679-4862-8bbe-43144e4bf81d)
![Prints do console (Entradas e Saídas)](https://github.com/user-attachments/assets/d59feffc-5bbe-4fee-9c2d-d10a54c3e63e)




