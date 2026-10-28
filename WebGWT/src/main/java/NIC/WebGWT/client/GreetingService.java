package NIC.WebGWT.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import NIC.WebGWT.shared.Driver;


@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
    List<String> getTeamList();

    List<Driver> getDriverList(String team) throws IllegalArgumentException;
}