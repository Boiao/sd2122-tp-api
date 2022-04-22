package tp1.service;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.Discovery;
import tp1.api.User;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;
import tp1.clients.REST.RestDirClient;
import tp1.server.REST.resources.UsersResource;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class JavaUsers implements Users {

    public final Map<String, User> users = new ConcurrentHashMap<>();

    private static Logger Log = Logger.getLogger(UsersResource.class.getName());

    private Discovery discv = new Discovery(null, "users", null);

    public JavaUsers() {

    }

    @Override
    public Result<String> createUser(User user) {
        Log.info("createUser : " + user);

        // Check if user data is valid
        if (user.getUserId() == null || user.getPassword() == null || user.getFullName() == null ||
                user.getEmail() == null) {
            Log.info("User object invalid.");
            //throw new WebApplicationException(Response.Status.BAD_REQUEST);
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        // Check if userId already exists
        if (users.containsKey(user.getUserId())) {
            Log.info("User already exists.");
            //throw new WebApplicationException(Response.Status.CONFLICT);
            return Result.error(Result.ErrorCode.CONFLICT);
        }

        //Add the user to the map of users
        users.put(user.getUserId(), user);
        return Result.ok(user.getUserId());
    }


    @Override
    public Result<User> getUser(String userId, String password) {
        Log.info("getUser : user = " + userId + "; pwd = " + password);

        User user = users.get(userId);

        // Check if user exists
        if (user == null) {
            Log.info("User does not exist.");
            //throw new WebApplicationException(Response.Status.NOT_FOUND);
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        // Check if user is valid
        if (userId == null || password == null) {
            Log.info("UserId or password null.");
            //throw new WebApplicationException(Response.Status.FORBIDDEN);
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }

        //Check if the password is correct
        if (!user.getPassword().equals(password)) {
            Log.info("Password is incorrect.");
            //throw new WebApplicationException(Response.Status.FORBIDDEN);
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }

        return Result.ok(user);
    }


    @Override
    public Result<User> updateUser(String userId, String password, User updatedUser) {
        Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; updatedUser = " + updatedUser);

        // Check if user is valid
        if (userId == null || password == null) {
            Log.info("UserId or password null.");
            //throw new WebApplicationException(Response.Status.FORBIDDEN);
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }

        User user = users.get(userId);

        // Check if user exists
        if (user == null) {
            Log.info("User does not exist.");
            //throw new WebApplicationException(Response.Status.NOT_FOUND);
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        //Check if the password is correct
        if (!user.getPassword().equals(password)) {
            Log.info("Password is incorrect.");
            //throw new WebApplicationException(Response.Status.FORBIDDEN);
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }

        if (updatedUser.getUserId() == userId || updatedUser.getUserId() == null) {

            if (updatedUser.getPassword() != null)
                user.setPassword(updatedUser.getPassword());
            if (updatedUser.getEmail() != null)
                user.setEmail(updatedUser.getEmail());
            if (updatedUser.getFullName() != null)
                user.setFullName(updatedUser.getFullName());
        }

        return Result.ok(users.get(userId));
    }


    public Result<User> deleteUser(String userId, String password) {
        Log.info("deleteUser : user = " + userId + "; pwd = " + password);
        //To Test

        // Check if user is valid
        if (userId == null || password == null) {
            Log.info("UserId or password null.");
            //throw new WebApplicationException(Response.Status.FORBIDDEN);
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }

        User user = users.get(userId);

        // Check if user exists
        if (user == null) {
            Log.info("User does not exist.");
            //throw new WebApplicationException(Response.Status.NOT_FOUND);
            return Result.error(Result.ErrorCode.NOT_FOUND);
        }

        //Check if the password is correct
        if (!user.getPassword().equals(password)) {
            Log.info("Password is incorrect.");
            //throw new WebApplicationException(Response.Status.FORBIDDEN);
            return Result.error(Result.ErrorCode.FORBIDDEN);
        }
        users.remove(userId);
        RestDirClient dirClient = new RestDirClient(getServiceURI("directory"));
        if(dirClient != null)
            dirClient.deleteUserFiles(userId, password);


        return Result.ok(user);
    }


    @Override
    public Result<List<User>> searchUsers(String pattern) {
        Log.info("searchUsers : pattern = " + pattern);

        List<User> res = new ArrayList<>();
        String LowerPattern = pattern.toLowerCase();
        for (Map.Entry<String, User> set : users.entrySet()) {
            if (set.getValue().getFullName().toLowerCase().contains(LowerPattern)) {
                res.add(set.getValue());
            }
        }

        return Result.ok(res);
    }

    private URI getServiceURI(String serviceName) {

        URI uri = null;
        discv.listener();
        int tries = 0;
        while (tries < 10) {
            if (discv.knownUrisOf(serviceName).length > 0) {
                uri = discv.knownUrisOf(serviceName)[0];
                break;
            }
        }
        return uri;
    }
}
