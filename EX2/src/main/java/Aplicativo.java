import java.util.List;

//import DAO;
//import PessoaDAO;
//import Pessoa;

// cpf, nome, idade, profissao

public class Aplicativo {

    public static void main(String[] args) throws Exception {

        PessoaDAO pessoaDAO = new PessoaDAO();

        System.out.println("\n\n==== Inserir usuário === ");
        Pessoa pessoa = new Pessoa(11, 33, "pablo", "Pentester");
        if(pessoaDAO.insert(pessoa) == true) {
            System.out.println("Inserção com sucesso -> " + pessoa.toString());
        }

        System.out.println("\n\n==== Testando autenticação ===");
        System.out.println("Pessoa (" + pessoa.getNome() + "): " + pessoaDAO.verify(11, "pablo"));

        System.out.println("\n\n==== Mostrar usuários orderado por profissao === ");
        List<Pessoa> pessoas = pessoaDAO.getOrderByProfissao();
        for (Pessoa u: pessoas) {
            System.out.println(u.toString());
        }

        System.out.println("\n\n==== Atualizar senha (cpf (" + pessoa.getCpf() + ") === ");
        pessoa.setIdade(22);
        pessoaDAO.update(pessoa);

        System.out.println("\n\n==== Testando autenticação ===");
        System.out.println("Pessoa (" + pessoa.getNome() + "): " + pessoaDAO.verify(11, DAO.toMD5("pablo")));		

        System.out.println("\n\n==== Invadir usando SQL Injection ===");
        System.out.println("Pessoa (" + pessoa.getNome() + "): " + pessoaDAO.verify(11, "x' OR 'x' LIKE 'x"));

        System.out.println("\n\n==== Mostrar usuários ordenados por cpf === ");
        pessoas = pessoaDAO.getOrderByCpf();
        for (Pessoa u: pessoas) {
            System.out.println(u.toString());
        }

        System.out.println("\n\n==== Excluir usuário (cpf " + pessoa.getCpf() + ") === ");
        pessoaDAO.delete(pessoa.getCpf());

        System.out.println("\n\n==== Mostrar usuários ordenados por nome === ");
        pessoas = pessoaDAO.getOrderByNome();
        for (Pessoa u: pessoas) {
            System.out.println(u.toString());
        }
    }
}
