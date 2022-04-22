package tp1.clients.REST;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

public class RestUsersClient extends RestClient implements Users {

    final WebTarget target;

    public RestUsersClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestUsers.PATH);
    }

    @Override
    public Result<String> createUser(User user) {
        
        return super.reTry(() -> {
            return clt_createUser(user);
        });
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        // TO test
        return super.reTry(() -> {
            return clt_getUser(userId, password);
        });
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        // TO test
        return super.reTry(() -> {
            return clt_updateUser(userId, password, user);
        });
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        // TO test
        return super.reTry(() -> {
            return clt_deleteUser(userId, password);
        });
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        return super.reTry(() ->{
                return clt_searchUsers(pattern);
        });
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public int checkPasssword(String userId,String password){
        return super.reTry(()->{
            return  clt_checkPassword(userId,password);
        });

    }

    private Result<String> clt_createUser(User user) {

        Response r = target.request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(user, MediaType.APPLICATION_JSON));

        if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity())
            return Result.ok(r.readEntity(String.class));
        else {
            System.out.println("Error, HTTP error status: " + r.getStatus());

            return Result.error(errorcheck(Response.Status.fromStatusCode(r.getStatus())));
        }
    }

    private Result<List<User>> clt_searchUsers(String pattern) {
        Response r = target
                .queryParam(QUERY, pattern)
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity())
            return Result.ok(r.readEntity(new GenericType<List<User>>() {
            }));
        else{
            System.out.println("Error, HTTP error status: " + r.getStatus());
        return Result.error(errorcheck(Response.Status.fromStatusCode(r.getStatus())));
    }
    }

    private Result<User> clt_getUser(String userId, String password) {
        //To test
        Response r = target.path(userId)
                .queryParam(PASSWORD, password).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity()) {
            return Result.ok(r.readEntity(User.class));
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            return Result.error(errorcheck(Response.Status.fromStatusCode(r.getStatus())));
        }
    }

    private Result<User> clt_updateUser(String userId, String password, User user) {

        Response r = target.path(userId)
                .queryParam(PASSWORD, password).request()
                .accept(MediaType.APPLICATION_JSON)
                .put(Entity.entity(user, MediaType.APPLICATION_JSON));

        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {
            return Result.ok(r.readEntity(User.class));
        } else{
            System.out.println("Error, HTTP error status: " + r.getStatus());
        return Result.error(errorcheck(Response.Status.fromStatusCode(r.getStatus())));
    }
    }

    private Result<User> clt_deleteUser(String userId, String password) {

        Response r = target.path(userId)
                .queryParam(PASSWORD, password).request()
                .accept(MediaType.APPLICATION_JSON)
                .delete();

        if (r.getStatus() == Response.Status.OK.getStatusCode()) {
            return Result.ok(r.readEntity(User.class));
        } else {
            System.out.println("Error, HTTP error status: " + r.getStatus());
            return Result.error(errorcheck(Response.Status.fromStatusCode(r.getStatus())));
        }
    }

    private int clt_checkPassword(String userId,String password){
        Response r = target.path(userId)
                .queryParam(PASSWORD, password).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();


            return r.getStatus();
    }

    private Result.ErrorCode errorcheck(Status status){
        Result.ErrorCode res;
        switch (status){
            case FORBIDDEN:
                res = Result.ErrorCode.FORBIDDEN;
                break;
            case NOT_FOUND:
                res = Result.ErrorCode.NOT_FOUND;
                break;
            case CONFLICT:
                res = Result.ErrorCode.CONFLICT;
                break;
            default:
                res = Result.ErrorCode.BAD_REQUEST;
        }return res;
    }

}
