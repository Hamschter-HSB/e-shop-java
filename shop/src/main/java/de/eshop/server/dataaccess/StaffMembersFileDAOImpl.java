package de.eshop.server.dataaccess;

import de.eshop.shared.dataaccess.DAO;
import de.eshop.shared.domain.BulkArticle;
import de.eshop.shared.domain.Customer;
import de.eshop.shared.domain.StaffMember;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaffMembersFileDAOImpl implements DAO<StaffMember> {

    private static final Logger logger = Logger.getLogger(StaffMembersFileDAOImpl.class.getName());

    private final String REG_EX = ".*=";

    public static final String DATA_PATH = "Data";
    public static final String STAFF_MEMBERS = "StaffMembers";

    private final File file = new File(STAFF_MEMBERS);

    public StaffMembersFileDAOImpl() {

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error creating file \"" + STAFF_MEMBERS + "\"", e);
            }
        }
    }

    @Override
    public void create(StaffMember staffMember) throws IOException {

        List<StaffMember> staffMembers = readAll();

        int id = staffMembers.stream()
                .mapToInt(StaffMember::getNumber)
                .max()
                .orElse(0) + 1;

        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write(id + ".id=" + id + "\n");
            bufferedReader.write(id + ".name=" + staffMember.getName() + "\n");
            bufferedReader.write(id + ".password=" + staffMember.getPassword() + "\n");
        }
    }

    @Override
    public synchronized StaffMember read(int id) throws IOException {

        if (!containsStaffMember(id))
            return null;

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateArticles = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(id + "."))
                    duplicateArticles.add(line);
            });

            assert duplicateArticles.size() == 4;

            int number = Integer.parseInt(duplicateArticles.get(0).replaceAll(REG_EX, ""));
            String userName = duplicateArticles.get(1).replaceAll(REG_EX, "");
            String password = duplicateArticles.get(2).replaceAll(REG_EX, "");

            return new StaffMember(number, userName, password);
        }
    }

    @Override
    public synchronized List<StaffMember> readAll() throws IOException {

        List<StaffMember> allStaffMembers = new ArrayList<>();
        Set<Integer> allStaffMemberIds = new LinkedHashSet<>();

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            AtomicInteger counter = new AtomicInteger();

            bufferedReader.lines().forEach(line -> {
                if (counter.getAndIncrement() % 3 == 0) {
                    allStaffMemberIds.add(Integer.parseInt(line.replaceAll(REG_EX, "")));
                }
            });

            allStaffMemberIds.forEach(userID -> {
                try {
                    allStaffMembers.add(read(userID));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return allStaffMembers;
    }

    @Override
    public synchronized void update(StaffMember type) {

    }

    @Override
    public synchronized void delete(int id) {

    }

    private synchronized boolean containsStaffMember(int id) throws IOException {

        try (FileReader fileReader = new FileReader(file)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> duplicateStaffMembers = new ArrayList<>();

            bufferedReader.lines().forEach(line -> {

                if (line.startsWith(id + "."))
                    duplicateStaffMembers.add(line);
            });

            return !duplicateStaffMembers.isEmpty();
        }
    }
}
