# 🗂️ Sistema de Cadastro (CRUD)

Este projeto consiste em um sistema de cadastro de funcionários (CRUD) desenvolvido em Java, com persistência de dados em arquivos .txt.

A aplicação permite realizar operações de Cadastro, Consulta, Atualização e Remoção de funcionários, além de manter um histórico de aumentos salariais, registrando informações como data, percentual aplicado e novo salário.

O projeto tem como foco o aprendizado prático de programação em Java, explorando conceitos como manipulação de arquivos, organização de dados, regras de negócio e estruturação de sistemas simples baseados em CRUD.

---

## 🎯 Objetivos do Projeto

* Desenvolver um sistema completo utilizando o conceito de CRUD
* Trabalhar com manipulação de arquivos em Java
* Aplicar regras de negócio relacionadas a funcionários e salários
* Registrar histórico de alterações salariais
* Praticar organização e estruturação de código
* Consolidar conhecimentos intermediários da linguagem Java

---

## 📂 Estrutura do Projeto

O projeto está organizado da seguinte forma:

* **Sistema_Cadastro.java**
    * Classe principal do sistema, responsável por:
    * Cadastro de funcionários
    * Consulta de dados
    * Atualização de informações
    * Remoção de registros
    * Aplicação de aumentos salariais
    * Manipulação de arquivos

* **funcionarios.txt**
    Arquivo responsável por armazenar os dados dos funcionários cadastrados, como:
    * ID
    * CPF
    * Nome
    * Cargo
    * Salário

* **historico_aumentos.txt**
    Arquivo responsável por armazenar o histórico de aumentos salariais, contendo:
    * ID do funcionário
    * CPF
    * Nome
    * Data do aumento
    * Salário anterior
    * Percentual aplicado
    * Novo salário

---

## 🛠️ Tecnologias Utilizadas

* **Java**
* Programação Orientada a Objetos (POO)
* Manipulação de arquivos
* Compilador **javac**
* Execução via **JVM**

---

## 🌍 Funcionalidades

* Cadastro de novos funcionários
* Listagem de funcionários cadastrados
* Atualização de dados
* Remoção de funcionários
* Aplicação de aumento salarial
* Registro automático no histórico de aumentos
* Persistência de dados em arquivos .txt

---

## 📌 Observações

* Projeto desenvolvido sem o uso de banco de dados
* Persistência realizada por meio de arquivos locais
* Foco no aprendizado de CRUD e manipulação de arquivos
* Algumas decisões priorizam clareza e organização do código
* Indicado para estudos intermediários em Java

---

## 🚀 Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/FelipeMizher/Sistema-de-Cadastro.git


2. Compile o Projeto:
    ```bash
    javac Sistema_Cadastro.java

3. Execute a Aplicação:
    ```bash
    java Sistema_Cadastro