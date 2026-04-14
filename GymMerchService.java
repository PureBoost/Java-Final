// Service class for gym merchandise business rules.
import java.util.List;

public class GymMerchService {
    private final GymMerchDAO gymMerchDAO;

    public GymMerchService(GymMerchDAO gymMerchDAO) {
        this.gymMerchDAO = gymMerchDAO;
    }

    public GymMerch addItem(GymMerch gymMerch) {
        validateMerch(gymMerch);
        return gymMerchDAO.createMerchItem(gymMerch);
    }

    public List<GymMerch> browseMerch() {
        return gymMerchDAO.getAllMerch();
    }

    public GymMerch findById(int merchId) {
        if (merchId <= 0) {
            throw new IllegalArgumentException("Merch ID must be greater than 0");
        }
        return gymMerchDAO.findMerchById(merchId);
    }

    public boolean updateItem(GymMerch gymMerch) {
        if (gymMerch == null || gymMerch.getMerchId() <= 0) {
            throw new IllegalArgumentException("Valid merch item with ID is required");
        }
        validateMerch(gymMerch);
        return gymMerchDAO.updateMerchItem(gymMerch);
    }

    public double getTotalStockValue() {
        return gymMerchDAO.getTotalStockValue();
    }

    public GymMerchDAO getGymMerchDAO() {
        return gymMerchDAO;
    }

    private void validateMerch(GymMerch gymMerch) {
        if (gymMerch == null) {
            throw new IllegalArgumentException("Merch item is required");
        }
        if (isBlank(gymMerch.getMerchName())) {
            throw new IllegalArgumentException("Merch name is required");
        }
        if (isBlank(gymMerch.getMerchType())) {
            throw new IllegalArgumentException("Merch type is required");
        }
        if (gymMerch.getMerchPrice() < 0) {
            throw new IllegalArgumentException("Merch price cannot be negative");
        }
        if (gymMerch.getQuantityInStock() < 0) {
            throw new IllegalArgumentException("Quantity in stock cannot be negative");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
