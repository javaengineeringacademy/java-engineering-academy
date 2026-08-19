package academy.javaengineering.patterns.enterprise.dto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper converting between User domain entity and UserDTO.
 * Centralizes the mapping logic so it is not scattered across the codebase.
 */
public final class UserMapper {

    private UserMapper() {}

    /**
     * Convert a User entity to a UserDTO, excluding sensitive fields.
     */
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName(),
                user.isActive()
        );
    }

    /**
     * Convert a UserDTO back to a User entity.
     * Password is left blank — must be set separately if needed.
     */
    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setActive(dto.isActive());
        return user;
    }

    /**
     * Convert a list of Users to a list of UserDTOs.
     */
    public static List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}
