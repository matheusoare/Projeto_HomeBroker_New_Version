# 🏦 Simulador de Home Broker

Sistema desktop para gerenciamento de operações em uma corretora de valores, que permite gerenciar investidores, ativos e ordens de compra/venda, desenvolvido em Java com interface gráfica Swing e persistência em arquivos.

## 📋 Funcionalidades

- **Gerenciamento de Investidores**: Inserir, alterar, apagar e visualizar investidores por ID
- **Contas** - Contas vinculadas aos clientes
- **Gerenciamento de Ativos**: Cadastrar ações/fundos com ticker, nome e preço
- **Gerenciamento de Ordens**: Criar ordens de compra e venda com múltiplos itens
- **Carteira de Ativos**: Visualizar ativos que cada investidor possui
- **Históricos** - Registro de operações realizadas
- **Controle de Saldo**: Atualização automática de saldo ao comprar/vender
- **Validações**: Verifica saldo insuficiente, investidores existem, etc.
- **Persistência em arquivos** (serialização Java) para armazenar os dados, com tratamento robusto de exceções e uma arquitetura baseada em DAO (Data Access Object).

## 🛠️ Pré-requisitos

- **Java 8+** instalado no sistema
- **Git** (opcional, para clonar o repositório)

### Verificar se Java está instalado:

```bash
java -version
```

Se não tiver Java instalado, baixe em: https://www.java.com/pt_BR/download/


## 🚀 Como Executar

### Passo a passo

### Opção 1: Compilar e Executar Manualmente
 ## 1. **Clone o repositório**
   ```bash
   git clone https://github.com/matheusoare/Projeto_HomeBroker_New_Version.git
   cd Projeto_HomeBroker_New_Version
   ```

 ## 2. **Compile o código**
 
 # No Linux / Mac / GitBash
   ```bash
   javac -d out $(find . -name "*.java")
   ```

 # No Windows
   ```bash
   javac -encoding UTF-8 -d bin src/broker/*.java src/broker/modelos/*.java src/broker/visao/*.java
   ```

 ## 3. **Execute o programa**
   ```bash
   java -cp out broker.visao.Main
   ```

   ---
### Opção 2: Script Automático (Linux)

 Crie um arquivo chamado `executar.sh` na raiz do projeto:

 ```bash 
 #!/bin/bash

 echo "🔧 Compilando o Home Broker System..."
 javac -encoding UTF-8 -d bin src/broker/*.java src/broker/modelos/*.java src/broker/visao/*.java

 if [ $? -ne 0 ]; then
     echo "❌ Erro na compilação! Verifique os arquivos."
     exit 1
 fi

 echo "✅ Compilação concluída com sucesso!" 
 echo ""
 echo "🚀 Executando o programa..."
 java -cp bin broker.visao.Main
 ```
 Depois execute:

 ```bash
 chmod +x executar.sh
 ./executar.sh
 ```

### Opção 3: Script Automático (Windows)

 Crie um arquivo executar.ps1 na raiz do projeto

 ```bash
 Write-Host "🔧 Compilando o Home Broker System..." -ForegroundColor Yellow
 javac -encoding UTF-8 -d bin src/broker/*.java src/broker/modelos/*.java src/broker/visao/*.java

 if ($LASTEXITCODE -ne 0) {
     Write-Host "❌ Erro na compilação! Verifique os arquivos." -ForegroundColor Red
     exit 1
 }

 Write-Host "✅ Compilação concluída com sucesso!" -ForegroundColor Green
 Write-Host ""
 Write-Host "🚀 Executando o programa..." -ForegroundColor Yellow
 java -cp bin broker.visao.Main
 ```

 Execute com:

 ```bash
 .\executar.ps1
 ```

 ---

## 📁 Estrutura do Projeto

Projeto_HomeBroker_New_Version/
├── src/
│   └── broker/
│       ├── modelos/              # Entidades do sistema
│       │   ├── Entidade.java     # Classe abstrata base (com Serializable)
│       │   ├── Cliente.java
│       │   ├── Conta.java
│       │   ├── Ativo.java
│       │   ├── Ordem.java
│       │   ├── Carteira.java
│       │   └── Historico.java
│       │
│       ├── persistencia/         # Camada de persistência
│       │   ├── EntidadeDAO.java  # DAO genérico com CRUD
│       │   ├── DAOfactory.java   # Fábrica de DAOs (Singleton)
│       │   └── PersistenceException.java # Exceção personalizada
│       │
│       ├── visao/                # Interface gráfica
│       │   ├── Main.java         # Ponto de entrada
│       │   ├── JanelaPrincipal.java # Menu principal
│       │   └── JanelaEntidade.java   # CRUD genérico para cada entidade
│       │
│       └── testes/               # Testes unitários (JUnit)
│           ├── ClienteDAOTest.java
│           ├── ContaDAOTest.java
│           ├── AtivoDAOTest.java
│           ├── OrdemDAOTest.java
│           ├── CarteiraDAOTest.java
│           └── HistoricoDAOTest.java
├── lib/                          # JUnit 5 - bibliotecas para testes unitário
│   ├── junit-jupiter-api_5.14.3.jar  
│   ├── junit-jupiter-engine_5.14.3.jar
│   ├── junit-platform-commons_1.14.3.jar
│   ├── junit-platform-engine_1.14.3.jar
│   ├── junit-platform-launcher_1.14.3.jar
│   ├── org.apiguardian.api_1.1.2.jar
│   └── org.opentest4j_1.3.0.jar
├── bin/                               (Gerado após compilação)
│   └── broker/
│       ├── *.class
│       ├── modelos/
│       │   └── *.class
│       └── visao/
│           └── *.class
├── data/                              (Gerado automaticamente)
│   ├── clientes.dat
│   ├── historicos.dat
│   ├── contas.dat
│   ├── ativos.dat
│   ├── carteiras.dat
│   └── ordens.dat
├── executar.ps1                       (Script Windows)
├── executar.sh                        (Script Linux/Mac)
└── README.md


## 🖥️ Como Usar o Programa

### Tela Principal
Ao executar o programa, uma janela com botões para cada entidade será exibida:

- **Clientes** – Cadastro de investidores
- **Contas** – Contas vinculadas aos clientes
- **Ativos** – Ações e fundos disponíveis
- **Ordens** – Ordens de compra e venda
- **Carteiras** – Ativos que cada cliente possui
- **Históricos** – Registro de operações realizadas

### Operações Básicas (CRUD)
Cada janela de entidade permite as seguintes operações:

| Botão | Funcionalidade |
|-------|----------------|
| **Novo** | Abre um formulário para cadastrar um novo registro |
| **Buscar** | Localiza um registro pelo ID e exibe seus detalhes |
| **Editar** | Altera os dados de um registro existente |
| **Apagar** | Remove um registro pelo ID |
| **Atualizar** | Recarrega a tabela com os dados mais recentes |

### Exemplo: Cadastrar um Cliente

1. Na tela principal, clique em **"Clientes"**
2. Clique em **"Novo"**
3. Preencha os campos:
   - **ID**: `1`
   - **Nome**: `João Silva`
   - **CPF**: `123.456.789-00`
4. Clique em **"OK"** para salvar
5. O registro aparecerá na tabela

### Exemplo: Criar uma Ordem de Compra

1. Na tela principal, clique em **"Ordens"**
2. Clique em **"Novo"**
3. Preencha os campos:
   - **ID**: `1`
   - **ID Conta**: `1`
   - **ID Ativo**: `1`
   - **Tipo**: `COMPRA`
   - **Quantidade**: `100`
   - **Preço Limite**: `25.50`
4. Clique em **"OK"** para salvar

### Exemplo: Visualizar um Cliente

1. Na tela principal, clique em "Clientes"
2. Digite no campo de busca:
   - **ID**: `1`
3. Clique em "Buscar"
4. Uma janela com os dados do cliente será exibida

### Persistência Automática
Os dados são salvos automaticamente em arquivos `.dat` na pasta `data/` sempre que você:
- Cadastra um novo registro
- Edita um registro existente
- Apaga um registro

Observações:
- Se você fechar o programa incorretamente, os últimos dados podem não ser salvos

### ⚠️ Validações

O sistema realiza verificações para evitar erros:

- **ID duplicado** – Não é permitido salvar dois registros com o mesmo ID
- **Campos obrigatórios** – Todos os campos devem ser preenchidos
- **Tipos numéricos** – Campos como ID, quantidade e preço exigem valores numéricos
- **Entidade não encontrada** – Ao buscar, editar ou apagar um ID inexistente, uma mensagem de erro é exibida

Em caso de erro, uma mensagem explicativa será exibida na tela.


## 💾 Persistência de Dados

### Como os dados são salvos

Os dados são salvos automaticamente em **arquivos binários** na pasta `data/` utilizando **Serialização Java** (`ObjectOutputStream`).

**Exemplo de arquivo gerado:** `data/clientes.dat`

### Arquivos gerados:

| Arquivo | Entidade |
|---------|----------|
| `clientes.dat` | Cliente |
| `contas.dat` | Conta |
| `ativos.dat` | Ativo |
| `ordens.dat` | Ordem |
| `carteiras.dat` | Carteira |
| `historicos.dat` | Historico |

### Estrutura dos dados

Cada arquivo armazena objetos Java das respectivas classes. Exemplo da classe `Cliente`:

```java
private int id;       // Identificador único
private String nome;  // Nome do investidor
private String cpf;   // CPF do investidor
```

## ⚠️ Tratamento de Exceções

O sistema possui uma camada robusta de tratamento de exceções através da classe `PersistenceException`:

**Operações CRUD** (salvar, atualizar, apagar, carregar)
- Validações:
  1. ID duplicado ao salvar
  2. Entidade não encontrada ao atualizar/apagar
  3. Campos inválidos ou nulos
  4. Erros de I/O ao ler/escrever arquivos

**Exemplo de exceção lançada:**

- Erro na operação Salvar: Entidade com este ID já existe. Valor: Cliente{id=1, nome=João, cpf=123}

## 🧪 Testes

- O projeto inclui testes unitários para cada DAO utilizando JUnit 5.
- Para executar os testes:
```bash

# Se estiver usando Maven
mvn test

# Se estiver usando Gradle
gradle test
```
# Ou execute diretamente na IDE (clique com o botão direito na pasta testes -> Run)

 - Cobertura de testes:
   1. ✅ Salvar com ID novo
   2. ✅ Salvar com ID existente (lança exceção)
   3. ✅ Atualizar com ID existente
   4. ✅ Atualizar com ID inexistente (lança exceção)
   5. ✅ Apagar com ID existente
   6. ✅ Apagar com ID inexistente (lança exceção)
   7. ✅ Carregar com ID existente
   8. ✅ Carregar com ID inexistente (lança exceção)

## 🔧 Troubleshooting (Solução de Problemas)

| Problema | Solução |
|----------|---------|
| `javac: command not found` | Instale o Java JDK e configure o PATH |
| `cannot find symbol` | Compile a partir da raiz do projeto com o caminho correto |
| `Could not find or load main class` | Execute `java -cp bin broker.visao.Main` na raiz |
| Dados não aparecem | Verifique se a pasta `data/` existe e se você salvou os registros |
| Pasta `data/` não criada | Execute o programa uma vez ou crie manualmente: `mkdir data` |
| Script `.sh` não executa no Linux | Dê permissão: `chmod +x executar.sh` | 

## 📚 Documentação do Código

O código está documentado com **Javadoc** para facilitar o entendimento das classes e métodos. Para gerar a documentação:

```bash
javadoc -encoding UTF-8 -d javadoc src/broker/*.java src/broker/modelos/*.java src/broker/persistencia/*.java src/broker/visao/*.java
```
Depois abra o arquivo `javadoc/index.html` no navegador.
---

## 👨‍💻 Autores

Leonardo Naassom Rosa Oliveira - Documentação e Revisão
Matheus Soares Ferreira Ramos - Desenvolvimento
Thales Henrique Marques Lázaro - Desenvolvimento

## 📝 Versão

1.0

---

## 📌 Notas Importantes

- ✅ Interface gráfica com Swing para facilitar o uso
- ✅ Persistência automática em arquivos .dat via serialização
- ✅ CRUD completo para todas as entidades (Clientes, Contas, Ativos, Ordens, Carteiras, Históricos)
- ✅ Tratamento robusto de exceções com PersistenceException
- ✅ Validações de ID duplicado, campos obrigatórios e tipos numéricos
- ✅ Arquitetura baseada em DAO com Generics e Singleton
- ✅ Testes unitários com JUnit 5

---

## 🚀 Próximas Melhorias (sugestões)

- [ ] Adicionar validação de CPF e outros campos específicos
- [ ] Implementar autenticação por senha para usuários
- [ ] Substituir serialização por banco de dados (SQLite/MySQL)
- [ ] Adicionar relatórios em PDF das movimentações
- [ ] Gráficos de desempenho de ativos
- [ ] Funcionalidade de busca por nome (não apenas por ID)
- [ ] Exportar dados para CSV/Excel
- [ ] Versão web da aplicação
