import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Funcionario{
    int id;
    String cpf;
    String nome;
    String cargo;
    double salario;

    // Construtor padrão da classe Funcionário
    Funcionario(){
        this.id = 0;
        this.cpf = "";
        this.nome = "";
        this.cargo = "";
        this.salario = 0.0;
    }

    // Construtor com parâmetros da classe Funcionario
    Funcionario(int id, String cpf, String nome, String cargo, double salario){
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.cargo = cargo;
        this.salario = salario;
    }

    // Getters and Setters
    public int getId(){
        return id;
    }

    public String getCpf(){
        return cpf;
    }

    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }

    public String getCargo(){
        return cargo;
    }
    public void setCargo(String cargo){
        this.cargo = cargo;
    }

    public double getSalario(){
        return salario;
    }
    public void setSalario(double salario){
        this.salario = salario;
    }

    // Retorna uma representação textual do Funcionário
    @Override
    public String toString(){
        return String.format("ID: %d | Nome: %s | CPF: %s | Cargo: %s | Salário: R$ %.2f",
                            id, nome, Sistema_Cadatro.exibirCPF(cpf), cargo, salario);
    }
}

public class Sistema_Cadatro{
    // Método para cadastrar um novo Funcionário
    public static void cadastrarFuncionario(Scanner sc){
        System.out.println("\nCadastrar um novo Funcionário:");
        System.out.println("-----------------------------------");

        String nome;
        do{
            System.out.print("Nome: ");
            nome = sc.nextLine().trim();

            if(nome.isEmpty()){
                System.out.println("ERRO: O nome não pode ser vazio!\n");
            }
        } while(nome.isEmpty());

        String cpf;
        do{
            System.out.print("CPF: ");
            cpf = sc.nextLine().trim();

            if(cpf.isEmpty()){
                System.out.println("ERRO: O CPF não pode ser vazio!\n");
            } else if(cpf.length() != 11){
                System.out.println("ERRO: O CPF deve ter exatamente 11 dígitos!\n");
            }
        } while(cpf.isEmpty() || cpf.length() != 11);

        String cargo;
        do{
            System.out.print("Cargo: ");
            cargo = sc.nextLine().trim();

            if(cargo.isEmpty()){
                System.out.println("ERRO: O Cargo não pode ser vazio!\n");
            }
        } while(cargo.isEmpty());

        double salario = 0;
        boolean salarioValido;
        do{
            salarioValido = true;
            System.out.print("Salário: ");

            try{
                salario = Double.parseDouble(sc.nextLine());
                if(salario <= 0){
                    System.out.println("ERRO: O salário deve ser maior que zero!\n");
                    salarioValido = false;
                }
            } catch(Exception e){
                System.out.println("ERRO: Digite um número válido!\n");
                salarioValido = false;
            }
        } while(!salarioValido);

        boolean cpfExistente = false;
        File arquivo = new File("funcionarios.txt");
        if(arquivo.exists() && arquivo.length() > 0){
            try(Scanner leitor = new Scanner(arquivo)){
                while(leitor.hasNextLine()){
                    String linha = leitor.nextLine().trim();
                    if(linha.isEmpty()) continue;
                    String[] dados = linha.split(";");
                    if(dados.length >= 2){
                        String cpfExist = dados[1].trim();
                        if(cpfExist.equals(cpf)){
                            cpfExistente = true;
                            break;
                        }
                    }
                }
            } catch(Exception e){
                System.out.println("Aviso: não foi possível verificar CPFs existentes: " + e.getMessage());
            }
        }

        if(cpfExistente){
            System.out.println("ERRO: CPF já cadastrado para outro Funcionário.");
            pausar(sc);
        } else{
            int novoId = lerUltimoIdDoArquivo() + 1;

            try(FileWriter fw = new FileWriter("funcionarios.txt", true)){
                fw.write(String.format("%d; %s; %s; %s; %.2f%n",
                        novoId, cpf, nome, cargo, salario));
                System.out.println("Funcionário cadastrado com sucesso!");
            } catch(IOException e){
                System.out.println("Erro ao cadastrar: " + e.getMessage());
            }

            pausar(sc);
        }
    }

    // Método para exibir as informações de um Funcionário
    public static void exibirFuncionario(String linha){
        if(linha == null || linha.trim().isEmpty()){
            return;
        }

        String[] dados = linha.split(";");

        int id = Integer.parseInt(dados[0].trim());
        String cpf = dados[1].trim();
        String nome = dados[2].trim();
        String cargo = dados[3].trim();
        double salario = Double.parseDouble(dados[4].trim());

        System.out.printf("ID: %d | Nome: %s | CPF: %s | Cargo: %s | Salário: R$ %.2f%n",
                        id, nome, exibirCPF(cpf), cargo, salario);
    }
    
    // Método para editar as informações de um Funcionário
    public static void editarFuncionario(Scanner sc){
        System.out.println("\nEditar Funcionário");
        System.out.println("------------------");

        File arquivo = new File("funcionarios.txt");
        File temp = new File("funcionarios_temp.txt");

        if(!arquivo.exists() || arquivo.length() == 0){
            System.out.println("Nenhum Funcionário cadastrado...");
            pausar(sc);
        } else{
            System.out.print("Digite o ID do funcionário que deseja editar: ");
            int idBusca = sc.nextInt();
            sc.nextLine();

            boolean encontrado = false;

            try(Scanner leitor = new Scanner(arquivo);
                FileWriter fw = new FileWriter(temp)){

                while(leitor.hasNextLine()){
                    String linha = leitor.nextLine();

                    if(linha.trim().isEmpty()){
                        continue;
                    }

                    String[] dados = linha.split(";");

                    int id = Integer.parseInt(dados[0].trim());
                    String cpf = dados[1].trim();
                    String nomeAtual = dados[2].trim();
                    String cargoAtual = dados[3].trim();
                    double salarioAtual = Double.parseDouble(dados[4].trim());

                    if(id == idBusca){
                        encontrado = true;

                        exibirFuncionario(linha);

                        System.out.print("\nDeseja editar este Funcionário? (S/N): ");
                        String confirmacao = sc.nextLine().trim().toUpperCase();

                        if(confirmacao.equals("S")){
                            System.out.println("ID não pode ser alterado.");

                            System.out.print("Novo nome (vazio para manter o mesmo): ");
                            String nome = sc.nextLine().trim();
                            if(nome.isEmpty()){
                                nome = nomeAtual;
                            }

                            System.out.println("CPF não pode ser alterado.");

                            System.out.print("Novo cargo (vazio para manter o mesmo): ");
                            String cargo = sc.nextLine().trim();
                            if(cargo.isEmpty()){
                                cargo = cargoAtual;
                            }

                            double salario = salarioAtual;
                            boolean salarioValido;

                            do{
                                salarioValido = true;
                                System.out.print("Novo salário (atual: " + salarioAtual + "): ");
                                String entrada = sc.nextLine().trim();

                                if(!entrada.isEmpty()){
                                    try{
                                        salario = Double.parseDouble(entrada);
                                        if(salario <= 0){
                                            System.out.println("Salário deve ser maior que zero!");
                                            salarioValido = false;
                                        }
                                    } catch(Exception e){
                                        System.out.println("Digite um número válido!");
                                        salarioValido = false;
                                    }
                                }
                            } while(!salarioValido);

                            fw.write(String.format(
                                "%d; %s; %s; %s; %.2f%n",
                                id, cpf, nome, cargo, salario
                            ));
                        } else{
                            fw.write(linha + System.lineSeparator());
                        }

                    } else{
                        fw.write(linha + System.lineSeparator());
                    }
                }

            } catch(Exception e){
                System.out.println("Erro ao editar Funcionário: " + e.getMessage());
            }

            if(encontrado){
                arquivo.delete();
                temp.renameTo(arquivo);
                System.out.println("\nFuncionário editado com sucesso!");
            } else{
                temp.delete();
                System.out.println("\nFuncionário não encontrado.");
            }

            pausar(sc);
        }
    }

    // Método para excluir um Funcionário
    public static String excluirFuncionario(Scanner sc){
        System.out.print("\nDigite o ID do funcionário que deseja excluir: ");
        int idExcluir = sc.nextInt();
        sc.nextLine();

        File arquivo = new File("funcionarios.txt");
        File temp = new File("temp.txt");

        boolean encontrado = false;
        String nomeExcluido = null;

        try(
            Scanner leitor = new Scanner(arquivo);
            FileWriter fw = new FileWriter(temp);
        ){
            while(leitor.hasNextLine()){
                String linha = leitor.nextLine();

                if(linha.trim().isEmpty()){
                    continue;
                }

                String[] dados = linha.split(";");
                int id = Integer.parseInt(dados[0].trim());

                if(id == idExcluir){
                    encontrado = true;

                    String cpf = dados[1].trim();
                    String nome = dados[2].trim();
                    String cargo = dados[3].trim();
                    double salario = Double.parseDouble(dados[4].trim());

                    System.out.println("\nFuncionário encontrado:");
                    System.out.printf(
                        "ID: %d, Nome: %s, CPF: %s, Cargo: %s, Salário: R$ %.2f%n",
                        id, nome, exibirCPF(cpf), cargo, salario
                    );

                    System.out.print("\nDeseja realmente excluir este Funcionário? (S/N): ");
                    String confirmacao = sc.nextLine().trim().toUpperCase();

                    if(confirmacao.equals("S")){
                        nomeExcluido = nome;
                    } else{
                        fw.write(linha + System.lineSeparator());
                    }
                } else{
                    fw.write(linha + System.lineSeparator());
                }
            }

        } catch(Exception e){
            System.out.println("Erro ao excluir funcionário: " + e.getMessage());
        }

        if(!encontrado){
            System.out.println("Funcionário com ID " + idExcluir + " não encontrado.");
            temp.delete();
            return null;
        }

        if(arquivo.delete()){
            temp.renameTo(arquivo);
        }

        return nomeExcluido;
    }
    
    // Método para buscar um Funcionário por nome ou CPF
    public static void buscarFuncionario(Scanner sc){
        limparTela();

        System.out.println("Buscar Funcionário especifico: ");
        System.out.println("-----------------------------------");
        System.out.println("(1) Buscar por CPF");
        System.out.println("(2) Buscar por Nome");
        System.out.println("(3) Voltar ao menu");
        System.out.print("Escolha uma opção: ");

        int opcao = sc.nextInt();
        sc.nextLine();
        boolean fazerBusca = true;

        if(opcao == 3){
            fazerBusca = false;
        }

        if(fazerBusca){
            File arquivo = new File("funcionarios.txt");

            if(!arquivo.exists() || arquivo.length() == 0){
                System.out.println("\nNenhum funcionário cadastrado.");
            } else{
                boolean encontrado = false;

                try(Scanner leitor = new Scanner(arquivo)){
                    if(opcao == 1){
                        System.out.print("\nDigite o CPF (somente números): ");
                        String cpfBusca = sc.nextLine().trim();

                        while(leitor.hasNextLine()){
                            String linha = leitor.nextLine();

                            if(!linha.trim().isEmpty()){
                                String[] dados = linha.split(";");

                                String cpf = dados[1].trim();

                                if(cpf.equals(cpfBusca)){
                                    int id = Integer.parseInt(dados[0].trim());
                                    String nome = dados[2].trim();
                                    String cargo = dados[3].trim();
                                    double salario = Double.parseDouble(dados[4].trim());

                                    System.out.printf(
                                        "\nID: %d\nNome: %s\nCPF: %s\nCargo: %s\nSalário: R$ %.2f%n",
                                        id, nome, exibirCPF(cpf), cargo, salario
                                    );

                                    encontrado = true;
                                }
                            }
                        }

                    } else if(opcao == 2){
                        System.out.print("\nDigite o nome ou parte do nome: ");
                        String nomeBusca = sc.nextLine().trim().toLowerCase();

                        while(leitor.hasNextLine()){
                            String linha = leitor.nextLine();

                            if(!linha.trim().isEmpty()){
                                String[] dados = linha.split(";");

                                String nome = dados[2].trim();

                                if(nome.toLowerCase().contains(nomeBusca)){
                                    int id = Integer.parseInt(dados[0].trim());
                                    String cpf = dados[1].trim();
                                    String cargo = dados[3].trim();
                                    double salario = Double.parseDouble(dados[4].trim());

                                    System.out.printf(
                                        "\nID: %d\nNome: %s\nCPF: %s\nCargo: %s\nSalário: R$ %.2f%n",
                                        id, nome, exibirCPF(cpf), cargo, salario
                                    );

                                    encontrado = true;
                                }
                            }
                        }
                    } else{
                        System.out.println("Opção inválida...");
                    }
                } catch(Exception e){
                    System.out.println("Erro ao buscar Funcionário: " + e.getMessage());
                }

                if(!encontrado && (opcao == 1 || opcao == 2)){
                    System.out.println("\nFuncionário não encontrado.");
                }
            }

            pausar(sc);
        }
    }

    // Método para listar todos os Funcionários cadastrados
    public static void listarFuncionarios(Scanner sc){
        boolean sair = false;

        while(!sair){
            limparTela();
            System.out.println("\nLista de funcionários cadastrados:");
            System.out.println("-----------------------------------");

            File arquivo = new File("funcionarios.txt");

            if(!arquivo.exists() || arquivo.length() == 0){
                System.out.println("Nenhum funcionário cadastrado.");
            } else{
                try(Scanner leitor = new Scanner(arquivo)){
                    while(leitor.hasNextLine()){
                        String linha = leitor.nextLine().trim();

                        if(!linha.isEmpty()){
                            exibirFuncionario(linha);
                        }
                    }
                } catch(Exception e){
                    System.out.println("Erro ao ler o arquivo: " + e.getMessage());
                }
            }

            System.out.println("\n-----------------------------------");
            System.out.println("(1) Aumentar salário");
            System.out.println("(2) Editar funcionário");
            System.out.println("(3) Excluir funcionário");
            System.out.println("(4) Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            int opcao;
            try{
                opcao = Integer.parseInt(sc.nextLine());
            } catch(Exception e){
                System.out.println("Opção inválida!");
                pausar(sc);
                continue;
            }

            switch(opcao){
                case 1:
                    aumentarSalario(sc);
                    break;
                case 2:
                    editarFuncionario(sc);
                    break;
                case 3:
                    String funcionarioExcluido = excluirFuncionario(sc);
                    if(funcionarioExcluido != null){
                        System.out.println("\nFuncionário excluído: " + funcionarioExcluido);
                    }
                    pausar(sc);
                    break;
                case 4:
                    System.out.println("Retornando ao menu principal...");
                    sair = true;
                    break;
                default:
                    System.out.println("Opção inválida!");
                    pausar(sc);
            }
        }
    }

    // Método para aumentar o salário de um Funcionário por porcentagem 
    public static void aumentarSalario(Scanner sc){
        System.out.println("\nAumentar salário de funcionário");
        System.out.println("--------------------------------");

        System.out.print("Digite o ID do funcionário: ");
        int idBusca = sc.nextInt();

        System.out.print("Porcentagem de aumento: ");
        double porcentagem = sc.nextDouble();
        sc.nextLine();

        File arquivoOriginal = new File("funcionarios.txt");
        File arquivoTemp = new File("funcionarios_temp.txt");

        boolean idEncontrado = false;

        try(Scanner leitor = new Scanner(arquivoOriginal); FileWriter fw = new FileWriter(arquivoTemp);){
            while(leitor.hasNextLine()){
                String linha = leitor.nextLine();
                String[] dados = linha.split(";");

                int id = Integer.parseInt(dados[0].trim());
                String cpf = dados[1].trim();
                String nome = dados[2].trim();
                String cargo = dados[3].trim();
                double salario = Double.parseDouble(dados[4].trim());

                if(id == idBusca){
                    System.out.printf("Salário atual de %s: R$ %.2f%n", nome, salario);
                    double salarioAntigo = salario;
                    salario += salario * (porcentagem / 100);
                    registrarAumento(id, cpf, nome, salarioAntigo, porcentagem, salario);
                    System.out.printf("Novo salário de %s: R$ %.2f%n", nome, salario);
                    idEncontrado = true;
                }

                fw.write(String.format(Locale.US, "%d; %s; %s; %s; %.2f%n",
                        id, cpf, nome, cargo, salario));
            }
        } catch(Exception e){
            System.out.println("Erro ao atualizar salário: " + e.getMessage());
            pausar(sc);
        }

        if(idEncontrado){
            arquivoOriginal.delete();
            arquivoTemp.renameTo(arquivoOriginal);
            System.out.println("Salário atualizado com sucesso!");
        } else{
            arquivoTemp.delete();
            System.out.println("Funcionário com ID " + idBusca + " não encontrado.");
        }

        pausar(sc);
    }

    // Método para registrar o histórico de aumento salarial de um Funcionário
    public static void registrarAumento(int id, String cpf, String nome, double salarioAntigo, 
                                        double porcentagem, double salarioNovo){
        try(FileWriter fw = new FileWriter("historico_aumentos.txt", true)){
            String data = java.time.LocalDate.now().toString();

            fw.write(String.format(
                "%d; %s; %s; %s; %.2f; %.2f%%; %.2f%n",
                id, cpf, nome, data, salarioAntigo, porcentagem, salarioNovo));
        } catch(Exception e){
            System.out.println("Erro ao registrar histórico de aumento: " + e.getMessage());
        }
    }

    // Método responsável por obter o último ID registrado no arquivo
    public static int lerUltimoIdDoArquivo(){
        int ultimoId = 0;

        try{
            File arquivo = new File("funcionarios.txt");

            if(arquivo.exists()){
                try (Scanner leitor = new Scanner(arquivo)){
                    while (leitor.hasNextLine()){
                        String linha = leitor.nextLine().trim();
                        if(!linha.isEmpty()){
                            String[] dados = linha.split(";");
                            ultimoId = Integer.parseInt(dados[0].trim());
                        }
                    }
                }
            }
        } catch(Exception e){
            System.out.println("Erro ao ler o último ID: " + e.getMessage());
        }

        return ultimoId;
    }

    // Método para mascarar e exibir o CPF
    public static String exibirCPF(String cpf){
    return "" + cpf.charAt(0) + cpf.charAt(1) + cpf.charAt(2) + "." +
                cpf.charAt(3) + cpf.charAt(4) + cpf.charAt(5) + "." +
                cpf.charAt(6) + cpf.charAt(7) + cpf.charAt(8) + "-" +
                cpf.charAt(9) + cpf.charAt(10);
    }

    // Método para adicionar uma pausa entre os menus
    public static void pausar(Scanner sc){
        System.out.println("\nPressione ENTER para continuar...");
        sc.nextLine();
    }

    // Método para limpar o terminal, evitando poluição visual na interface
    public static void limparTela(){
        try{
            String os = System.getProperty("os.name").toLowerCase();

            if(os.contains("win")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else{
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch(IOException | InterruptedException e){
            for(int i = 0; i < 50; i++){
                System.out.println();
            }
        }
    }

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int opcao = 0;  

        do{
            limparTela();

            System.out.println("\nSistema de gestão de Funcionários: ");
            System.out.println("-----------------------------------");
            System.out.println("\n(1) Cadastrar novo Funcionário");
            System.out.println("(2) Listar todos os Funcionários");
            System.out.println("(3) Buscar Funcionário por CPF/Nome");
            System.out.println("(4) Sair");
            System.out.print("Escolha uma opcao: ");

            opcao = sc.nextInt();
            sc.nextLine();
            switch(opcao){
                    case 1:
                        cadastrarFuncionario(sc);
                        break;
                    case 2:
                        listarFuncionarios(sc);
                        break;
                    case 3:
                        buscarFuncionario(sc);
                        break;
                    case 4:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
        } while(opcao != 4);
    }
}