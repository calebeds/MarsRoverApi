package com.calebe.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.calebe.dto.HomeDto;

public interface PreferencesRepository extends JpaRepository<HomeDto, Long> {

	HomeDto findByUserId(Long userId);//The name convention makes this method works

}
