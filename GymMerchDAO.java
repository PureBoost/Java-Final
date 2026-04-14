// Data access class for gym merchandise records.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GymMerchDAO {
    public GymMerch createMerchItem(GymMerch gymMerch) {
        String sql = "INSERT INTO gym_merch (merch_name, merch_type, merch_price, quantity_in_stock) VALUES (?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, gymMerch.getMerchName());
            statement.setString(2, gymMerch.getMerchType());
            statement.setDouble(3, gymMerch.getMerchPrice());
            statement.setInt(4, gymMerch.getQuantityInStock());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    gymMerch.setMerchId(keys.getInt(1));
                }
            }

            return gymMerch;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create merch item", exception);
        }
    }

    public GymMerch findMerchById(int merchId) {
        String sql = "SELECT merch_id, merch_name, merch_type, merch_price, quantity_in_stock FROM gym_merch WHERE merch_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, merchId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    GymMerch merch = new GymMerch();
                    merch.setMerchId(resultSet.getInt("merch_id"));
                    merch.setMerchName(resultSet.getString("merch_name"));
                    merch.setMerchType(resultSet.getString("merch_type"));
                    merch.setMerchPrice(resultSet.getDouble("merch_price"));
                    merch.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                    return merch;
                }
                return null;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find merch item", exception);
        }
    }

    public boolean updateMerchItem(GymMerch gymMerch) {
        String sql = "UPDATE gym_merch SET merch_name = ?, merch_type = ?, merch_price = ?, quantity_in_stock = ? WHERE merch_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, gymMerch.getMerchName());
            statement.setString(2, gymMerch.getMerchType());
            statement.setDouble(3, gymMerch.getMerchPrice());
            statement.setInt(4, gymMerch.getQuantityInStock());
            statement.setInt(5, gymMerch.getMerchId());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update merch item", exception);
        }
    }

    public boolean deleteMerchItem(int merchId) {
        String sql = "DELETE FROM gym_merch WHERE merch_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, merchId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to delete merch item", exception);
        }
    }

    public List<GymMerch> getAllMerch() {
        String sql = "SELECT merch_id, merch_name, merch_type, merch_price, quantity_in_stock FROM gym_merch ORDER BY merch_id";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<GymMerch> merchList = new ArrayList<>();
            while (resultSet.next()) {
                GymMerch merch = new GymMerch();
                merch.setMerchId(resultSet.getInt("merch_id"));
                merch.setMerchName(resultSet.getString("merch_name"));
                merch.setMerchType(resultSet.getString("merch_type"));
                merch.setMerchPrice(resultSet.getDouble("merch_price"));
                merch.setQuantityInStock(resultSet.getInt("quantity_in_stock"));
                merchList.add(merch);
            }
            return merchList;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to load merch items", exception);
        }
    }

    public double getTotalStockValue() {
        String sql = "SELECT COALESCE(SUM(merch_price * quantity_in_stock), 0) FROM gym_merch";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
            return 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to calculate stock value", exception);
        }
    }
}
