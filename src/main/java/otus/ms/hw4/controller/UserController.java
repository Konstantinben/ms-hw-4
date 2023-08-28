package otus.ms.hw4.controller;

import otus.ms.hw4.model.dto.UserDto;
import otus.ms.hw4.model.dto.UserUuidDto;
import otus.ms.hw4.model.entity.User;
import otus.ms.hw4.model.mapper.UserMapper;
import otus.ms.hw4.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "UserController", description = "Контроллер для работы с пользователями")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserRepository userRepository;

    private final UserMapper userMapper;


    @PostMapping
    @Operation(summary = "Create user")
    public UserUuidDto create(@RequestBody @Valid UserDto userDto) {
        User user = userMapper.toUser(userDto);
        userRepository.save(user);
        return new UserUuidDto(user.getUuid());
    }

    @PutMapping("/{uuid}")
    @Operation(summary = "Update user")
    public UserDto updateProfile(@PathVariable("uuid") UUID userId, @RequestBody @Valid UserDto profile) {

        User user = userRepository
                .findByUuid(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find user by " + userId));

        userRepository.save(userMapper.toUser(user, profile));
        return userMapper.toUserDto(user);
    }

    @GetMapping ("/{uuid}")
    @Operation(summary = "Get user")
    public UserDto getByUuid(@PathVariable("uuid") UUID userId) {
        return userRepository
                .findByUuid(userId)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new IllegalArgumentException("Cannod find user by " + userId));
    }

    @DeleteMapping ("/{uuid}")
    @Operation(summary = "Delet user")
    public UserDto deleteByUuid(@PathVariable("uuid") UUID userId) {
        /*return userRepository
                .deleteByUuid(userId)
                .map(userMapper::toUserUuidDto)
                .orElseThrow(() -> new IllegalArgumentException("Cannod delete user by " + userId));*/
        User user = userRepository
                .findByUuid(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cannod find user by " + userId));

        userRepository.delete(user);

        return userMapper.toUserDto(user);
    }


    @GetMapping ("/search")
    @Operation(summary = "Поиск анкет")
    public List<UserDto> searchByName(@NotBlank @RequestParam("first_name") String firstName,
                                      @NotBlank @RequestParam("last_name") String lastName) {
        List<UserDto> result = userRepository
                .findLikeFirstAndLastNames(firstName.trim(), lastName.trim())
                .stream()
                .sorted(Comparator.comparing(User::getFirstName))
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());

        log.debug("/user/search found " + result.size() + " records by " + firstName + " and " + lastName);
        return result;
    }
}


