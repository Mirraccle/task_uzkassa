package uz.company.task.errors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.company.task.dto.ResponseDTO;

@Slf4j
@RestControllerAdvice
public class ExceptionTranslator {

    @ExceptionHandler
    public ResponseEntity<ResponseDTO> handleAuthExcepction(CommonException ex) {
        log.error("--> Handled AuthException. Type: {}, Message: {}.", ex.getClass(), ex.getMessage());
        ResponseDTO response = new ResponseDTO();
        response.setStatus("FAILED");
        response.setData(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDTO> handleException(Exception ex) {
        log.error("--> Handled Exception. Type: {}, Message: {}.", ex.getClass(), ex.getMessage());
        ResponseDTO response = new ResponseDTO();
        response.setStatus("FAILED");
        response.setData("Unknown error, please refer developer or administrator");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
