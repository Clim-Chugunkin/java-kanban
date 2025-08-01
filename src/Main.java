import usecases.UseCase;
import usecases.UseCaseOne;
import usecases.UseCaseSprint6;
import usecases.UseCaseSprint7;

public class Main {
    public static void main(String[] args) {
        UseCase[] useCases = {new UseCaseOne(), new UseCaseSprint6(),
                new UseCaseSprint7()};
        for (UseCase usecase : useCases) {
            System.out.println(usecase.getName().toUpperCase());
            usecase.go();
        }
    }
}
