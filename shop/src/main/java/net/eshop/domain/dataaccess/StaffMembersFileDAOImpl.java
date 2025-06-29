package net.eshop.domain.dataaccess;

import net.eshop.domain.StaffMember;
import net.eshop.exceptions.UserNotFoundException;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        int id = staffMember.getNumber();

        if (containsStaffMember(id)) {
            logger.info(MessageFormat.format("StaffMember with number {0} does already exist!", id));
            return;
        }

        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bufferedReader = new BufferedWriter(fileWriter)) {

            bufferedReader.write(id + ".id=" + id + "\n");
            bufferedReader.write(id + ".name=" + staffMember.getName() + "\n");
            bufferedReader.write(id + ".password=" + staffMember.getPassword() + "\n");
        }
    }

    @Override
    public StaffMember read(int id) throws IOException {

        if (!containsStaffMember(id))
            throw new UserNotFoundException(MessageFormat.format("No user with number {0}", id));

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
    public List<StaffMember> readAll() throws IOException {

        List<StaffMember> allStaffMembers = new ArrayList<>();
        Set<Integer> allStaffMemberIds = new HashSet<>();

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
    public void update(StaffMember type) {

    }

    @Override
    public void delete(int id) {

    }

    private boolean containsStaffMember(int id) throws IOException {

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
