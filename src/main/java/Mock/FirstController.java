package Mock;

import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping(value = "app/v1")
public class FirstController {
    String bodyGet, bodyPost;

    @PostConstruct
    public void init() throws IOException {
        Path pathGet = Path.of("src/main/resources/getAnswer.txt");
        bodyGet = Files.readString(pathGet);

        Path pathPost = Path.of("src/main/resources/postAnswer.txt");
        bodyPost = Files.readString(pathPost);
    }

    @GetMapping (value = "/getRequest")
    public ResponseEntity<String> getRequest(@RequestParam int id, @RequestParam String name) throws IOException {

        try {
            if (id > 10 && id < 50) {
                Thread.sleep(1000);
            } else {
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (id <= 10) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("id должен быть больше 10");
        }
        if (name.length() <= 5) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "Имя должно содержать больше 5 символов");
        }

        String data;
        data = bodyGet.replace("{name}", name);

        return ResponseEntity.ok(data);
    }

    @PostMapping (value = "/postRequest")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, Object> body) throws IOException {
        String name = (String) body.get("name");
        String surname = (String) body.get("surname");
        Integer age = (Integer) body.get("age");

        if (name == null || name == "" || surname == null || surname == "" || age == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Поля не могут быть пустыми");
        }

        String data;
        data = bodyPost.replace("{name}", name)
                .replace("{surname}", surname)
                .replace("{age}*2", String.valueOf(age * 2))
                .replace("{age}", String.valueOf(age));

        return ResponseEntity.ok(data);
    }
}

