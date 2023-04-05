package ru.booksharing.repositories.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.booksharing.models.works.PublishingHouseWork;

@Repository
public interface PublishingHouseWorksRepository extends JpaRepository<PublishingHouseWork, Long> {}