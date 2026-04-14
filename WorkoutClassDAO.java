// Data access class for workout class records.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WorkoutClassDAO {
    public WorkoutClass createWorkoutClass(WorkoutClass workoutClass) {
        String sql = "INSERT INTO workout_classes (workout_class_type, workout_class_description, trainer_id, class_datetime, duration_minutes, capacity) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, workoutClass.getWorkoutClassType());
            statement.setString(2, workoutClass.getWorkoutClassDescription());
            statement.setInt(3, workoutClass.getTrainerId());
            if (workoutClass.getClassDateTime() == null) {
                statement.setTimestamp(4, null);
            } else {
                statement.setTimestamp(4, Timestamp.valueOf(workoutClass.getClassDateTime()));
            }
            statement.setInt(5, workoutClass.getDurationMinutes());
            statement.setInt(6, workoutClass.getCapacity());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    workoutClass.setWorkoutClassId(keys.getInt(1));
                }
            }

            return workoutClass;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create workout class", exception);
        }
    }

    public WorkoutClass findWorkoutClassById(int workoutClassId) {
        String sql = "SELECT workout_class_id, workout_class_type, workout_class_description, trainer_id, class_datetime, duration_minutes, capacity FROM workout_classes WHERE workout_class_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, workoutClassId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapWorkoutClass(resultSet);
                }
                return null;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find workout class", exception);
        }
    }

    public boolean updateWorkoutClass(WorkoutClass workoutClass) {
        String sql = "UPDATE workout_classes SET workout_class_type = ?, workout_class_description = ?, trainer_id = ?, class_datetime = ?, duration_minutes = ?, capacity = ? WHERE workout_class_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, workoutClass.getWorkoutClassType());
            statement.setString(2, workoutClass.getWorkoutClassDescription());
            statement.setInt(3, workoutClass.getTrainerId());
            if (workoutClass.getClassDateTime() == null) {
                statement.setTimestamp(4, null);
            } else {
                statement.setTimestamp(4, Timestamp.valueOf(workoutClass.getClassDateTime()));
            }
            statement.setInt(5, workoutClass.getDurationMinutes());
            statement.setInt(6, workoutClass.getCapacity());
            statement.setInt(7, workoutClass.getWorkoutClassId());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update workout class", exception);
        }
    }

    public boolean deleteWorkoutClass(int workoutClassId) {
        String sql = "DELETE FROM workout_classes WHERE workout_class_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, workoutClassId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to delete workout class", exception);
        }
    }

    public List<WorkoutClass> getAllWorkoutClasses() {
        String sql = "SELECT workout_class_id, workout_class_type, workout_class_description, trainer_id, class_datetime, duration_minutes, capacity FROM workout_classes ORDER BY class_datetime NULLS LAST, workout_class_id";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<WorkoutClass> classes = new ArrayList<>();
            while (resultSet.next()) {
                classes.add(mapWorkoutClass(resultSet));
            }
            return classes;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to load workout classes", exception);
        }
    }

    public List<WorkoutClass> getClassesByTrainerId(int trainerId) {
        String sql = "SELECT workout_class_id, workout_class_type, workout_class_description, trainer_id, class_datetime, duration_minutes, capacity FROM workout_classes WHERE trainer_id = ? ORDER BY class_datetime NULLS LAST, workout_class_id";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, trainerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<WorkoutClass> classes = new ArrayList<>();
                while (resultSet.next()) {
                    classes.add(mapWorkoutClass(resultSet));
                }
                return classes;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to load trainer classes", exception);
        }
    }

    private WorkoutClass mapWorkoutClass(ResultSet resultSet) throws SQLException {
        WorkoutClass workoutClass = new WorkoutClass();
        workoutClass.setWorkoutClassId(resultSet.getInt("workout_class_id"));
        workoutClass.setWorkoutClassType(resultSet.getString("workout_class_type"));
        workoutClass.setWorkoutClassDescription(resultSet.getString("workout_class_description"));
        workoutClass.setTrainerId(resultSet.getInt("trainer_id"));

        Timestamp classTimestamp = resultSet.getTimestamp("class_datetime");
        if (classTimestamp != null) {
            workoutClass.setClassDateTime(classTimestamp.toLocalDateTime());
        }

        workoutClass.setDurationMinutes(resultSet.getInt("duration_minutes"));
        workoutClass.setCapacity(resultSet.getInt("capacity"));
        return workoutClass;
    }
}
