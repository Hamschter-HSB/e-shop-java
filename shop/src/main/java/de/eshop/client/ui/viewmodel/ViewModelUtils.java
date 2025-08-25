package de.eshop.client.ui.viewmodel;

import de.eshop.shared.domain.exceptions.NoMultipleOfBulkException;

import java.text.MessageFormat;

public final class ViewModelUtils {

    public static boolean currentUserIsStaffMember() {
        return "STAFF_MEMBER".equals(System.getProperty("CURRENT_USER"));
    }

    public static void checkIsBulkArticleMultiple(int bulkSize, int stockSize) throws NoMultipleOfBulkException {
        if (!(stockSize % bulkSize == 0))
            throw new NoMultipleOfBulkException(MessageFormat.format("Error: Article amount must be a multiple of {0}", bulkSize), -1, bulkSize, stockSize);
    }
}
