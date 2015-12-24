package com.yetanotherdevblog.repositories;

import com.yetanotherdevblog.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

}
