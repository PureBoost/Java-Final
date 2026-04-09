// Service class for membership business rules.
public class MembershipService {
    private final MembershipDAO membershipDAO;

    public MembershipService(MembershipDAO membershipDAO) {
        this.membershipDAO = membershipDAO;
    }

    public Membership purchaseMembership(Membership membership) {
        throw new UnsupportedOperationException("Implement purchaseMembership in MembershipService");
    }

    public MembershipDAO getMembershipDAO() {
        return membershipDAO;
    }
}
