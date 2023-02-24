package uz.company.task.errors;

public class CommonException extends Exception{

    private final String message;


    public CommonException(String message) {
        super(message);
        this.message = message;
    }
}
