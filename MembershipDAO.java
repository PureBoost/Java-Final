// Data access class for membership records.
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MembershipDAO {
    public Membership createMembership(Membership membership) {
        String sql = "INSERT INTO memberships (membership_type, membership_description, membership_cost, member_id, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, membership.getMembershipType());
            statement.setString(2, membership.getMembershipDescription());
            statement.setDouble(3, membership.getMembershipCost());
            statement.setInt(4, membership.getMemberId());
            statement.setDate(5, Date.valueOf(membership.getStartDate()));
            statement.setDate(6, Date.valueOf(membership.getEndDate()));
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    membership.setMembershipId(keys.getInt(1));
                }
            }

            return membership;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to create membership", exception);
        }
    }

    public Membership findMembershipById(int membershipId) {
        String sql = "SELECT membership_id, membership_type, membership_description, membership_cost, member_id, start_date, end_date FROM memberships WHERE membership_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, membershipId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMembership(resultSet);
                }
                return null;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to find membership by id", exception);
        }
    }

    public boolean updateMembership(Membership membership) {
        String sql = "UPDATE memberships SET membership_type = ?, membership_description = ?, membership_cost = ?, member_id = ?, start_date = ?, end_date = ? WHERE membership_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, membership.getMembershipType());
            statement.setString(2, membership.getMembershipDescription());
            statement.setDouble(3, membership.getMembershipCost());
            statement.setInt(4, membership.getMemberId());
            statement.setDate(5, Date.valueOf(membership.getStartDate()));
            statement.setDate(6, Date.valueOf(membership.getEndDate()));
            statement.setInt(7, membership.getMembershipId());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to update membership", exception);
        }
    }

    public boolean deleteMembership(int membershipId) {
        String sql = "DELETE FROM memberships WHERE membership_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, membershipId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to delete membership", exception);
        }
    }

    public List<Membership> getAllMemberships() {
        String sql = "SELECT membership_id, membership_type, membership_description, membership_cost, member_id, start_date, end_date FROM memberships ORDER BY membership_id";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            List<Membership> memberships = new ArrayList<>();
            while (resultSet.next()) {
                memberships.add(mapMembership(resultSet));
            }
            return memberships;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to load memberships", exception);
        }
    }

    public double getTotalRevenueCurrentYear() {
        String sql = "SELECT COALESCE(SUM(membership_cost), 0) FROM memberships WHERE EXTRACT(YEAR FROM start_date) = EXTRACT(YEAR FROM CURRENT_DATE)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }
            return 0;
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to calculate membership revenue", exception);
        }
    }

    public double getTotalExpensesByMemberId(int memberId) {
        String sql = "SELECT COALESCE(SUM(membership_cost), 0) FROM memberships WHERE member_id = ?";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, memberId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(1);
                }
                return 0;
            }
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to calculate membership expenses", exception);
        }
    }

    private Membership mapMembership(ResultSet resultSet) throws SQLException {
        Membership membership = new Membership();
        membership.setMembershipId(resultSet.getInt("membership_id"));
        membership.setMembershipType(resultSet.getString("membership_type"));
        membership.setMembershipDescription(resultSet.getString("membership_description"));
        membership.setMembershipCost(resultSet.getDouble("membership_cost"));
        membership.setMemberId(resultSet.getInt("member_id"));
        membership.setStartDate(resultSet.getDate("start_date").toLocalDate());
        membership.setEndDate(resultSet.getDate("end_date").toLocalDate());
        return membership;
    }
}
