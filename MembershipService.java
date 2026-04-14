// Service class for membership business rules.
import java.time.LocalDate;

public class MembershipService {
    private final MembershipDAO membershipDAO;

    public MembershipService(MembershipDAO membershipDAO) {
        this.membershipDAO = membershipDAO;
    }

    public Membership purchaseMembership(Membership membership) {
        if (membership == null) {
            throw new IllegalArgumentException("Membership is required");
        }

        if (isBlank(membership.getMembershipType())) {
            throw new IllegalArgumentException("Membership type is required");
        }

        if (membership.getMemberId() <= 0) {
            throw new IllegalArgumentException("Member id must be greater than 0");
        }

        if (membership.getMembershipCost() < 0) {
            throw new IllegalArgumentException("Membership cost cannot be negative");
        }

        if (membership.getStartDate() == null) {
            membership.setStartDate(LocalDate.now());
        }

        if (membership.getEndDate() == null) {
            membership.setEndDate(membership.getStartDate().plusDays(30));
        }

        if (membership.getEndDate().isBefore(membership.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        membership.setMembershipType(membership.getMembershipType().trim());
        if (membership.getMembershipDescription() != null) {
            membership.setMembershipDescription(membership.getMembershipDescription().trim());
        }

        return membershipDAO.createMembership(membership);
    }

    public MembershipDAO getMembershipDAO() {
        return membershipDAO;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
