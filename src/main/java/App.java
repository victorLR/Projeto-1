import DAO.ClienteSetDAO;
import DAO.IClienteDAO;
import domain.Cliente;

import javax.swing.*;

public class App {
    private static IClienteDAO iClienteDAO;

    public static void main(String[] args) {
        iClienteDAO = new ClienteSetDAO();

        String opcao = JOptionPane.showInputDialog(null,
                "Digite 1 para cadastro, 2 para consultar, 3 para exclusão, 4 para alteração ou 5 para sair.",
                "Cadastro", JOptionPane.INFORMATION_MESSAGE);

        while (!isOpcaoValida(opcao)) {
            if (opcao == null || opcao.trim().isEmpty()) {
                sair();
            }
            opcao = JOptionPane.showInputDialog(null,
                    "Opção inválida. Digite 1 para cadastro, 2 para consulta, 3 para exclusão, 4 para alteração ou 5 para sair.",
                    "Cadastro", JOptionPane.INFORMATION_MESSAGE);
        }

        while (isOpcaoValida(opcao)) {
            if (isOpcaoSair(opcao)) {
                sair();
            } else if (isCadastro(opcao)) {
                String dados = JOptionPane.showInputDialog(null,
                        "Digite os dados do cliente separados por vírgula, conforme o exemplo: Nome, Cpf, Telefone, Endereço, Número, Cidade e Estado",
                        "Cadastro", JOptionPane.INFORMATION_MESSAGE);
                cadastrar(dados);
            } else if (isConsulta(opcao)) {
                String dados = JOptionPane.showInputDialog(null, "Digite o CPF do cliente.", "Consultar cliente",
                        JOptionPane.INFORMATION_MESSAGE);
                consultar(dados);
            } else if (isExclusao(opcao)) {
                String dados = JOptionPane.showInputDialog(null, "Digite o CPF do cliente", "Exclusão de cliente",
                        JOptionPane.INFORMATION_MESSAGE);
                excluir(dados);
            } else {
                String dados = JOptionPane.showInputDialog(null,
                        "Digite os dados do cliente separados por vírgula, conforme o exemplo: Nome, CPF, Telefone, Endereço, Número, Cidade e Estado",
                        "Atualização de cliente", JOptionPane.INFORMATION_MESSAGE);
                atualizar(dados);
            }
            opcao = JOptionPane.showInputDialog(null,
                    "Digite 1 para cadastro, 2 para consultar, 3 para exclusão, 4 para alteração ou 5 para sair.",
                    "Cadastro", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private static void atualizar(String dados) {
        String[] dadosSeparados = dados.split(",");
        Cliente cliente = new Cliente(dadosSeparados[0], dadosSeparados[1], dadosSeparados[2], dadosSeparados[3],
                dadosSeparados[4], dadosSeparados[5], dadosSeparados[6]);
        iClienteDAO.alterar(cliente);
        JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso", "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static boolean isExclusao(String opcao) {
        return "3".equals(opcao);
    }

    private static boolean isConsulta(String opcao) {
        return "2".equals(opcao);
    }

    private static boolean isCadastro(String opcao) {
        return "1".equals(opcao);
    }

    private static void sair() {
        StringBuilder clientesCadastrados = new StringBuilder();
        for (Cliente cliente : iClienteDAO.buscarTodos()) {
            clientesCadastrados.append(cliente.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(null, "Clientes cadastrados:\n" + clientesCadastrados,
                "Todos os Clientes", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private static boolean isOpcaoSair(String opcao) {
        return "5".equals(opcao);
    }

    private static boolean isOpcaoValida(String opcao) {
        return "1".equals(opcao) || "2".equals(opcao) || "3".equals(opcao) || "4".equals(opcao) || "5".equals(opcao);
    }


    private static void cadastrar(String dados) {
        String[] dadosSeparados = dados.split(",");

        if (dadosSeparados.length < 7) { // Verifica se foram passados todos os dados necessários
            JOptionPane.showMessageDialog(null, "Dados insuficientes! Certifique-se de informar: Nome, CPF, Telefone, Endereço, Número, Cidade e Estado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Long cpf = Long.parseLong(dadosSeparados[1].trim()); // Converte CPF para Long
            Cliente cliente = new Cliente(dadosSeparados[0],dadosSeparados[1], dadosSeparados[2], dadosSeparados[3], dadosSeparados[4], dadosSeparados[5], dadosSeparados[6]);

            if (iClienteDAO.cadastrar(cliente)) {
                JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar! Cliente já existe.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "CPF inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static void consultar(String cpf) {
        try {
            Long cpfLong = Long.parseLong(cpf.trim());
            Cliente cliente = iClienteDAO.consultar(cpfLong);

            if (cliente != null) {
                JOptionPane.showMessageDialog(null, "Cliente encontrado:\n" + cliente, "Consulta", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Cliente não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "CPF inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static void excluir(String cpf) {
        try {
            Long cpfLong = Long.parseLong(cpf.trim());
            iClienteDAO.excluir(cpfLong);
            JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "CPF inválido! Digite apenas números.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

}
