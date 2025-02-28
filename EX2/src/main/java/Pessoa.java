public class Pessoa {
    private int cpf;
    private int idade;
    private String nome;
    private String profissao;

    public Pessoa() {}

    public Pessoa(int cpf, int idade, String nome, String profissao) {
        this.cpf = cpf;
        this.nome = nome;
        this.idade = idade;
        this.profissao = profissao;
    }

    public void setIdade(int idade) { this.idade = idade; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(int cpf) { this.cpf = cpf; }
    public void setProfissao(String profissao) { this.profissao = profissao; }

    public int getIdade() { return this.idade; }
    public String getNome() { return this.nome; }
    public int getCpf() { return this.cpf; }
    public String getProfissao() { return this.profissao; }

    @Override
    public String toString() {
        return "Pessoa [cpf=" + cpf + ", nome=" + nome + ", idade=" + idade + ", profissao=" + profissao + "]";
    }	
}
