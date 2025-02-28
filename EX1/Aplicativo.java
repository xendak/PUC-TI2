import java.util.Scanner;

public class Aplicativo {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.println("Digite o primeiro número: ");
        int num1 = input.nextInt();
        System.out.println("Digite o segundo número: ");
        int num2 = input.nextInt();
        int sum = num1 + num2;

        System.out.println("Soma de " + num1 + " e " + num2 + " é: " + sum);

        input.close();
    }
}
