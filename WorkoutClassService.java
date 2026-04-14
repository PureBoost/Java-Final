// Service class for workout class business rules.
import java.time.LocalDateTime;
import java.util.List;

public class WorkoutClassService {
    private final WorkoutClassDAO workoutClassDAO;

    public WorkoutClassService(WorkoutClassDAO workoutClassDAO) {
        this.workoutClassDAO = workoutClassDAO;
    }

    public WorkoutClass createClass(WorkoutClass workoutClass) {
        validateClass(workoutClass);
        return workoutClassDAO.createWorkoutClass(workoutClass);
    }

    public List<WorkoutClass> browseClasses() {
        return workoutClassDAO.getAllWorkoutClasses();
    }

    public List<WorkoutClass> getAssignedClasses(int trainerId) {
        if (trainerId <= 0) {
            throw new IllegalArgumentException("Trainer id must be greater than 0");
        }
        return workoutClassDAO.getClassesByTrainerId(trainerId);
    }

    public WorkoutClass getById(int workoutClassId) {
        if (workoutClassId <= 0) {
            throw new IllegalArgumentException("Class id must be greater than 0");
        }
        return workoutClassDAO.findWorkoutClassById(workoutClassId);
    }

    public boolean updateClass(WorkoutClass workoutClass) {
        if (workoutClass == null || workoutClass.getWorkoutClassId() <= 0) {
            throw new IllegalArgumentException("Valid class and class id are required");
        }
        validateClass(workoutClass);
        return workoutClassDAO.updateWorkoutClass(workoutClass);
    }

    public boolean deleteClass(int workoutClassId, int trainerId) {
        WorkoutClass existing = getById(workoutClassId);
        if (existing == null) {
            throw new IllegalArgumentException("Workout class not found");
        }
        if (existing.getTrainerId() != trainerId) {
            throw new IllegalArgumentException("You can only delete your own classes");
        }
        return workoutClassDAO.deleteWorkoutClass(workoutClassId);
    }

    public LocalDateTime getFixedTimeSlot(String option) {
        if ("1".equals(option)) {
            return LocalDateTime.now().withHour(12).withMinute(0).withSecond(0).withNano(0);
        }
        if ("2".equals(option)) {
            return LocalDateTime.now().withHour(17).withMinute(0).withSecond(0).withNano(0);
        }
        throw new IllegalArgumentException("Invalid time slot option");
    }

    public WorkoutClassDAO getWorkoutClassDAO() {
        return workoutClassDAO;
    }

    private void validateClass(WorkoutClass workoutClass) {
        if (workoutClass == null) {
            throw new IllegalArgumentException("Workout class is required");
        }
        if (workoutClass.getTrainerId() <= 0) {
            throw new IllegalArgumentException("Trainer id must be greater than 0");
        }
        if (workoutClass.getWorkoutClassType() == null || workoutClass.getWorkoutClassType().trim().isEmpty()) {
            throw new IllegalArgumentException("Workout class type is required");
        }
        if (workoutClass.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        if (workoutClass.getCapacity() <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
    }
}
