import usecases.UseCase;
import usecases.UseCaseOne;
import usecases.UseCaseSprint6;

public class Main {

    public static void main(String[] args) {
        UseCase[] useCases = new UseCase[]{new UseCaseOne(), new UseCaseSprint6()};

        for (UseCase usecase : useCases) {
            System.out.println(usecase.getName().toUpperCase());
            usecase.go();
        }

    }
}
