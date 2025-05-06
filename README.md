# Pratt Parser em Java

Este projeto implementa um **Pratt Parser** em Java, uma técnica de parsing eficiente para expressões matemáticas com suporte a precedência e associatividade de operadores. Inspirado pelo artigo [Simple but powerful Pratt parsing](https://matklad.github.io/2020/04/13/simple-but-powerful-pratt-parsing.html), este parser foi desenvolvido do zero utilizando uma arquitetura orientada a objetos com boas práticas de projeto. A saide do parser segue a notação de Lisp-style S-expressions

O repositório contem dois documentos pdf que explicam em detalhes o fluxo do código tanto do lexer quanto do parser.

## Estrutura de Diretórios

```plaintext
prat-parser/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── parser/
│   │   │       └── prat_parser/
│   │   │           ├── App.java
│   │   │           ├── PrattParser.java
│   │   │           ├── lexer/
│   │   │           │   ├── Lexer.java
│   │   │           │   ├── LexerFactory.java
│   │   │           │   ├── LexerState.java
│   │   │           │   └── Rule.java
│   │   │           └── model/
│   │   │               ├── Atom.java
│   │   │               ├── Cons.java
│   │   │               ├── Expression.java
│   │   │               ├── Token.java
│   │   │               └── TokenType.java
│   └── main/
│       └── resources/
│
└── src/
    └── test/
        └── java/
            └── parser/
                └── prat_parser/
                    ├── LexerTest.java
                    └── PrattParserTest.java
```

## 🔍 Visão Geral

O parser é composto por duas etapas principais:

- **Lexical Analysis (Lexer)**: Converte a string de entrada em uma sequência de tokens.
- **Parsing (Parser)**: Constrói uma árvore de sintaxe abstrata (AST) usando a técnica de precedência de operadores (Pratt Parsing).

O núcleo do parser está na classe `PrattParser.java`, que analisa tokens com base em seus níveis de precedência prefixa, infixa e pós-fixa.

## ✅ Princípios SOLID Aplicados

Este projeto seguiu sempre que possível os princípios SOLID:

### 🔹 SRP (Single Responsibility Principle)
Cada classe possui responsabilidade única:
- `Lexer`: tokeniza a entrada.
- `Rule`: define como reconhecer e produzir tokens.
- `PrattParser`: realiza a análise sintática.
- `Expression`, `Atom`, `Cons`: representam a árvore de sintaxe abstrata (AST).

### 🔹 OCP (Open/Closed Principle)
O parser pode ser estendido com novos tipos de expressões ou tokens sem modificar o código existente — basta adicionar novas regras (`Rule`) ou expressões (`Expression`).

### 🔹 LSP (Liskov Substitution Principle)
As subclasses de `Expression` (como `Atom` e `Cons`) podem ser utilizadas de forma intercambiável sem quebrar o comportamento esperado.

### 🔹 ISP (Interface Segregation Principle)
Interfaces como `LexerState` são coesas e específicas, evitando obrigar implementações a dependerem de métodos que não utilizam.

### 🔹 DIP (Dependency Inversion Principle)
As classes dependem de abstrações, como `LexerState` e `Expression`, promovendo baixo acoplamento entre os componentes.

## 🧪 Testes

O projeto conta com **duas classes de teste** (em `src/test/java/parser/prat_parser`) que validam tanto a tokenização quanto a construção da AST:

- `LexerTest.java` – Valida o funcionamento do analisador léxico.
- `PrattParserTest.java` – Verifica parsing de expressões básicas, intermediárias e avançadas. Os testes comparam diretamente a **estrutura da árvore sintática (AST)** utilizando objetos `Atom` e `Cons`, garantindo maior robustez e precisão do que comparações baseadas apenas em strings.

Os testes seguem o padrão **JUnit** e podem ser executados com:

```bash
mvn test
```

### Construção da AST

Vamos utilizar uma expressão simples para ilustrar como o Pratt Parser constrói uma Árvore de Sintaxe Abstrata (AST). Consideremos a expressão:

```
1 + 2 * 3
```

No Pratt Parser, a análise leva em conta a precedência dos operadores. Sabemos que a multiplicação (`*`) tem precedência maior que a adição (`+`). Portanto, a expressão é interpretada como:

```
1 + (2 * 3)
```


A AST correspondente seria:

```
      [+]
     /   \
   [1]   [*]
         / \
       [2] [3]
```

- **`+`**: É o nó raiz da AST, representando a operação de adição.
- **`1`**: É o operando esquerdo da adição.
- **`*`**: É o operando direito da adição, representando a operação de multiplicação.
- **`2` e `3`**: São os operandos da multiplicação.  ([Pratt Expression Parsing Exemplos | ParserObjects](https://whiteknight.github.io/ParserObjects/v3/prattexpr_example)

O Pratt Parser constrói essa árvore respeitando as precedências dos operadores, garantindo que a multiplicação seja avaliada antes da adição.

## 🛠️ Funcionalidades Atuais

O parser atual reconhece e avalia expressões com:

- Literais numéricos e variáveis (`1`, `x`)
- Operadores infix (`+`, `-`, `*`, `/`, `.`, `=`, `? :`)
- Prefixos (`-x`, `+x`)
- Pós-fixos (`!`, `[ ]`)
- Parênteses aninhados

## 📌 Roadmap de Desenvolvimento

- [ ] Suporte a expressões com funções e chamadas encadeadas
- [ ] Validação e tratamento de erros com mensagens mais descritivas
- [ ] Suporte a parsing de expressões booleanas e lógicas

## 📖 Referência Técnica

Este projeto foi fortemente inspirado no trabalho de [matklad](https://matklad.github.io/2020/04/13/simple-but-powerful-pratt-parsing.html), e adapta a mesma ideia para o paradigma orientado a objetos com Java.
