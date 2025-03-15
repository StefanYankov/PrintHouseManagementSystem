package services;

import data.models.Edition;
import data.models.PrintHouse;
import data.models.Size;
import services.contracts.IEditionService;
import services.contracts.ISerializationService;
import utilities.exceptions.*;
import utilities.globalconstants.ExceptionMessages;
import utilities.globalconstants.ModelsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Manages {@link Edition} entities associated with {@link PrintHouse} instances, including persistence.
 */
public class EditionService implements IEditionService {
    private static final Logger logger = LoggerFactory.getLogger(EditionService.class);
    private final Map<PrintHouse, List<Edition>> editionsByPrintHouse = new HashMap<>();
    private final ISerializationService<Edition> serializationService;

    public EditionService(ISerializationService<Edition> serializationService) {
        if (serializationService == null) {
            logger.error("Serialization service cannot be null");
            throw new IllegalArgumentException("Serialization service cannot be null");
        }
        this.serializationService = serializationService;
        logger.info("EditionService initialized with serialization support");
    }

    /** {@inheritDoc} */
    @Override
    public void addEdition(PrintHouse printHouse, Edition edition) {
        validatePrintHouse(printHouse);
        validateEdition(edition);
        List<Edition> editions = editionsByPrintHouse.computeIfAbsent(printHouse, k -> new ArrayList<>());
        if (editions.contains(edition)) {
            logger.warn("Edition already exists for PrintHouse {}: {}", printHouse, edition);
            throw new InvalidEditionException(ExceptionMessages.EDITION_NOT_IN_PRINT_HOUSE);
        }
        editions.add(edition);
        logger.info("Edition added to PrintHouse {}: {}", printHouse, edition);
    }

    /** {@inheritDoc} */
    @Override
    public List<Edition> getEditions(PrintHouse printHouse) {
        validatePrintHouse(printHouse);
        List<Edition> editions = editionsByPrintHouse.getOrDefault(printHouse, Collections.emptyList());
        logger.debug("Retrieved {} editions for PrintHouse {}", editions.size(), printHouse);
        return new ArrayList<>(editions);
    }

    /** {@inheritDoc} */
    @Override
    public Edition getEdition(PrintHouse printHouse, int index) {
        validatePrintHouse(printHouse);
        List<Edition> editions = getEditions(printHouse);
        if (index < 0 || index >= editions.size()) {
            logger.error("Invalid edition index {} for PrintHouse {}", index, printHouse);
            throw new InvalidEditionException(ExceptionMessages.EDITION_NOT_IN_PRINT_HOUSE);
        }
        return editions.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public void updateEdition(PrintHouse printHouse, Edition edition, String title, Integer numberOfPages, Size size) {
        validatePrintHouse(printHouse);
        validateEdition(edition);
        List<Edition> editions = editionsByPrintHouse.get(printHouse);
        if (editions == null || !editions.contains(edition)) {
            logger.error("Edition not found in PrintHouse {}: {}", printHouse, edition);
            throw new InvalidEditionException(ExceptionMessages.EDITION_NOT_IN_PRINT_HOUSE);
        }
        if (title != null && !title.trim().isEmpty()) {
            if (title.length() < ModelsConstants.MIN_TITLE_LENGTH || title.length() > ModelsConstants.MAX_TITLE_LENGTH) {
                logger.error("Invalid title length: {}", title);
                throw new InvalidTitleException(ExceptionMessages.TITLE_LENGTH_INVALID);
            }
            edition.setTitle(title);
            logger.debug("Updated title to: {}", title);
        }
        if (numberOfPages != null) {
            if (numberOfPages < ModelsConstants.MIN_PAGE_COUNT || numberOfPages > ModelsConstants.MAX_PAGE_COUNT) {
                logger.error("Invalid page count: {}", numberOfPages);
                throw new InvalidNumberOfPagesException(ExceptionMessages.NUMBER_OF_PAGES_INVALID);
            }
            edition.setNumberOfPages(numberOfPages);
            logger.debug("Updated page count to: {}", numberOfPages);
        }
        if (size != null) {
            edition.setSize(size);
            logger.debug("Updated size to: {}", size);
        }
        logger.info("Edition updated in PrintHouse {}: {}", printHouse, edition);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEdition(PrintHouse printHouse, Edition edition) {
        validatePrintHouse(printHouse);
        validateEdition(edition);
        List<Edition> editions = editionsByPrintHouse.get(printHouse);
        if (editions == null || !editions.remove(edition)) {
            logger.warn("Edition not found for removal in PrintHouse {}: {}", printHouse, edition);
        } else {
            logger.info("Edition removed from PrintHouse {}: {}", printHouse, edition);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void saveEditions(PrintHouse printHouse, String filePath) {
        validatePrintHouse(printHouse);
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("File path cannot be null or empty");
            throw new IllegalArgumentException(ExceptionMessages.FILE_PATH_CANNOT_BE_NULL_OR_EMPTY);
        }
        List<Edition> editions = editionsByPrintHouse.getOrDefault(printHouse, Collections.emptyList());
        String fullPath = filePath + "_ph" + printHouse.hashCode() + ".ser"; // Unique per PrintHouse
        serializationService.serialize(editions, fullPath);
        logger.info("Saved {} editions for PrintHouse {} to {}", editions.size(), printHouse, fullPath);
    }

    /** {@inheritDoc} */
    @Override
    public void loadEditions(PrintHouse printHouse, String filePath) {
        validatePrintHouse(printHouse);
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("File path cannot be null or empty");
            throw new IllegalArgumentException(ExceptionMessages.FILE_PATH_CANNOT_BE_NULL_OR_EMPTY);
        }
        String fullPath = filePath + "_ph" + printHouse.hashCode() + ".ser";
        List<Edition> loadedEditions = serializationService.deserialize(fullPath);
        editionsByPrintHouse.put(printHouse, new ArrayList<>(loadedEditions));
        logger.info("Loaded {} editions for PrintHouse {} from {}", loadedEditions.size(), printHouse, fullPath);
    }

    private void validatePrintHouse(PrintHouse printHouse) {
        if (printHouse == null) {
            logger.error(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
            throw new InvalidPrintHouseException(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
        }
    }

    private void validateEdition(Edition edition) {
        if (edition == null) {
            logger.error(ExceptionMessages.EDITION_CANNOT_BE_NULL);
            throw new InvalidEditionException(ExceptionMessages.EDITION_CANNOT_BE_NULL);
        }

        if (edition.getTitle() == null || edition.getTitle().trim().isEmpty()) {
            logger.error(ExceptionMessages.TITLE_CANNOT_BE_NULL);
            throw new InvalidTitleException(ExceptionMessages.TITLE_CANNOT_BE_NULL);
        }

        if (edition.getNumberOfPages() < ModelsConstants.MIN_PAGE_COUNT) {
            logger.error(ExceptionMessages.NUMBER_OF_PAGES_MUST_BE_GREATER_THAN_ZERO);
            throw new InvalidNumberOfPagesException(ExceptionMessages.NUMBER_OF_PAGES_MUST_BE_GREATER_THAN_ZERO);
        }

        if (edition.getSize() == null) {
            logger.error(ExceptionMessages.INVALID_PAGE_SIZE);
            throw new InvalidPageSizeException(ExceptionMessages.INVALID_PAGE_SIZE);
        }
    }
}