// Service class for gym merchandise business rules.
public class GymMerchService {
    private final GymMerchDAO gymMerchDAO;

    public GymMerchService(GymMerchDAO gymMerchDAO) {
        this.gymMerchDAO = gymMerchDAO;
    }

    public GymMerch addItem(GymMerch gymMerch) {
        throw new UnsupportedOperationException("Implement addItem in GymMerchService");
    }

    public GymMerchDAO getGymMerchDAO() {
        return gymMerchDAO;
    }
}
