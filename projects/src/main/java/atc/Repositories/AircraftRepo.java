package atc.Repositories;

import atc.Model.Aircraft;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AircraftRepo extends CrudRepository<Aircraft, Integer> {

}
