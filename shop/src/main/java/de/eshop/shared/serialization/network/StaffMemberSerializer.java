package de.eshop.shared.serialization.network;

import de.eshop.shared.domain.StaffMember;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class StaffMemberSerializer implements Serializer<StaffMember> {

    @Override
    public void write(PrintStream out, StaffMember staffMember) {
        out.println(staffMember.getNumber());
        out.println(staffMember.getName());
        out.println(staffMember.getPassword());
    }

    @Override
    public StaffMember read(BufferedReader in) throws IOException {
        int number = Integer.parseInt(in.readLine());
        String userName = in.readLine();
        String password = in.readLine();
        return new StaffMember(number, userName, password);
    }
}
