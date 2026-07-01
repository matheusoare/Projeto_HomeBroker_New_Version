Simulador de Home Broker
Autores e Versão

    Desenvolvimento: Matheus Soares Ferreira Ramos e Thales Henrique Marques Lázaro

    Documentação e Revisão: Leonardo Naassom Rosa Oliveira

    Versão: 2.0

O trabalho conjunto dos autores resultou nesta aplicação desktop modular. O núcleo de desenvolvimento estabeleceu uma base sólida de engenharia de software, enquanto a revisão e a documentação técnica estruturaram o projeto para que ele funcione como um ecossistema integrado e de fácil compreensão, ligando as regras de negócio de investimentos diretamente à persistência local.
Descrição do Projeto e Funcionalidades

O Simulador de Home Broker é um sistema desktop para gerenciamento de operações em uma corretora de valores. Ele permite administrar investidores, contas, ativos e ordens de compra/venda de forma centralizada. A aplicação utiliza uma interface gráfica Swing e adota o padrão arquitetural DAO (Data Access Object) com Generics e Singleton, o que garante a separação clara de responsabilidades e facilita a manutenção do código.

As funcionalidades principais do sistema englobam:

    Gerenciamento de Investidores e Contas: Inserção, alteração, exclusão e visualização de investidores por ID, associando cada cliente à sua respectiva conta.

    Gerenciamento de Ativos: Cadastro e controle de ações e fundos contendo ticker, nome e preço.

    Gerenciamento de Ordens e Carteira: Criação de ordens de compra e venda com múltiplos itens, atualizando automaticamente o saldo financeiro e refletindo o estado atualizado na carteira de ativos do investidor.

    Histórico e Auditoria: Registro contínuo e automático de todas as operações realizadas para fins de consulta e rastreabilidade.

    Validações e Segurança: Mecanismo robusto que impede IDs duplicados, valida campos obrigatórios, bloqueia ordens se houver saldo insuficiente e trata falhas via exceções personalizadas com a classe PersistenceException.

Tecnologias e Requisitos

Para dar suporte a todas as funcionalidades descritas, o ambiente de desenvolvimento e execução necessita de:

    Java 8+ instalado no sistema (verifique utilizando o comando java -version).

    Java Swing para a renderização da interface gráfica.

    JUnit 5 para a execução dos testes unitários que validam a integridade das operações CRUD na camada de persistência.

Como Executar

Considerando que você já possui o diretório do projeto localmente, a compilação e a inicialização podem ser feitas diretamente através dos scripts automatizados incluídos na raiz ou de forma manual.
Opção 1: Via Scripts de Automação

    No Linux / Mac:
    Bash

    chmod +x executar.sh
    ./executar.sh

    No Windows (PowerShell):
    Bash

    .\executar.ps1

Opção 2: Compilação e Execução Manual

Caso prefira processar os arquivos diretamente no terminal, execute os comandos abaixo a partir do diretório raiz do projeto:

No Windows:

```terminal
New-Item -ItemType Directory -Force -Path bin | Out-Null
javac -encoding UTF-8 -cp "lib/*" -d bin $(Get-ChildItem -Recurse -Filter *.java -Path src | ForEach-Object { $_.FullName })
java -cp "bin;lib/*" broker.visao.Main
```

No Linux:

```terminal
mkdir -p bin
javac -encoding UTF-8 -cp "lib/*" -d bin $(find src -name "*.java")
java -cp "bin:lib/*" broker.visao.Main
```

Para rodar os testes:

```terminal
java -cp "bin;lib/*" broker.testes.TestRunner
```

No Linux:

```terminal
java -cp "bin:lib/*" broker.testes.TestRunner
```

Estrutura e Persistência de Dados

As funcionalidades manipuladas pelo usuário na interface Swing interagem diretamente com a arquitetura interna criada pelos desenvolvedores. Os dados do Home Broker são preservados localmente através da Serialização Java (ObjectOutputStream), salvando o estado do sistema de maneira automatizada sempre que um registro é criado, editado ou removido.

A estrutura de diretórios reflete essa organização:

    data/: Diretório gerado automaticamente para armazenar os arquivos binários de persistência (clientes.dat, contas.dat, ativos.dat, ordens.dat, carteiras.dat, historicos.dat).

    src/broker/modelos/: Contém as entidades abstratas e concretas do sistema que servem de modelo para os dados (Cliente, Conta, Ativo, Ordem, Carteira, Historico).

    src/broker/persistencia/: Concentra a lógica do DAO genérico, da fábrica Singleton e das validações de I/O que evitam a corrupção dos arquivos binários.

    src/broker/visao/: Contém as janelas de interface gráfica (JanelaPrincipal, JanelaEntidade) e a classe Main que inicia o programa.

    src/broker/testes/: Armazena os testes unitários estruturados em JUnit 5 para garantir o comportamento previsível de cada método de salvamento e leitura.
