package de.eshop.client.dataaccess;

import de.eshop.client.connection.ClientConnectionToServer;
import de.eshop.shared.dataaccess.DAO;
import de.eshop.shared.domain.StaffMember;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class StaffMembersDAOImpl implements DAO<StaffMember> {

    private static final Logger logger = Logger.getLogger(StaffMembersDAOImpl.class.getName());

    private final ClientConnectionToServer clientConnectionToServer;

    public StaffMembersDAOImpl(ClientConnectionToServer clientConnectionToServer) {
        this.clientConnectionToServer = clientConnectionToServer;
    }

    @Override
    public void create(StaffMember staffMember) throws IOException {
        clientConnectionToServer.createStaffMember(staffMember);
    }

    @Override
    public StaffMember read(int id) throws IOException {
        return clientConnectionToServer.readStaffMemberByID(id);
    }

    @Override
    public List<StaffMember> readAll() throws IOException {
        return new ArrayList<>(clientConnectionToServer.readAllStaffMembers());
    }

    @Override
    public void update(StaffMember type) {
        //Not implemented yet
    }

    @Override
    public void delete(int id) {
        clientConnectionToServer.deleteStaffMember(id);
    }
}
