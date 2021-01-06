package dev.bigwig.uaa.repository;

import dev.bigwig.uaa.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
