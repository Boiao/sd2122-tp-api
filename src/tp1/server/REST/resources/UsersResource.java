package tp1.server.REST.resources;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;
import tp1.Discovery;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.clients.REST.RestDirClient;

@Singleton
public class UsersResource implements RestUsers {

	public final Map<String, User> users = new HashMap<>();

	private static Logger Log = Logger.getLogger(UsersResource.class.getName());
	private  RestDirClient dirClient;

	private Discovery discv = new Discovery(null, "users", null);

	public UsersResource() {
		discv.listener();
		dirClient = new RestDirClient(getServiceURI("directory"));

	}

	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);

		// Check if user data is valid
		if(user.getUserId() == null || user.getPassword() == null || user.getFullName() == null ||
				user.getEmail() == null) {
			Log.info("User object invalid.");
			throw new WebApplicationException( Status.BAD_REQUEST );
		}

		// Check if userId already exists
		if(users.containsKey(user.getUserId())) {
			Log.info("User already exists.");
			throw new WebApplicationException( Status.CONFLICT );
		}

		//Add the user to the map of users
		users.put(user.getUserId(), user);
		return user.getUserId();
	}


	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);

		User user = users.get(userId);

		// Check if user exists
		if( user == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}

		// Check if user is valid
		if(userId == null || password == null) {
			Log.info("UserId or password null.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}

		//Check if the password is correct
		if( !user.getPassword().equals( password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}

		return user;
	}

/*
	@Override
	public int getUserbyId(String userId){

		User user = users.get(userId);

		// Check if user exists
		if( user == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}

		return user;
	}
*/

	@Override
	public User updateUser(String userId, String password, User updatedUser) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; updatedUser = " + updatedUser);

		// Check if user is valid
		if(userId == null || password == null) {
			Log.info("UserId or password null.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}

		User user = users.get(userId);

		// Check if user exists
		if( user == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}

		//Check if the password is correct
		if( !user.getPassword().equals(password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}

		if(updatedUser.getUserId() == userId || updatedUser.getUserId() == null) {

			if (updatedUser.getPassword() != null)
				user.setPassword(updatedUser.getPassword());
			if (updatedUser.getEmail() != null)
				user.setEmail(updatedUser.getEmail());
			if (updatedUser.getFullName() != null)
				user.setFullName(updatedUser.getFullName());
		}
		return users.get(userId);
	}


	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);
		//To Test

		// Check if user is valid
		if(userId == null || password == null) {
			Log.info("UserId or password null.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}

		User user = users.get(userId);

		// Check if user exists
		if( user == null ) {
			Log.info("User does not exist.");
			throw new WebApplicationException( Status.NOT_FOUND );
		}

		//Check if the password is correct
		if( !user.getPassword().equals( password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException( Status.FORBIDDEN );
		}
		users.remove(userId);
		//dirClient.deleteUserFiles(userId,password);


		return user;
	}


	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);

		List<User> res = new ArrayList<>();
		String LowerPattern = pattern.toLowerCase();
		for (Map.Entry<String,User> set: users.entrySet()) {
			if(set.getValue().getFullName().toLowerCase().contains(LowerPattern)) {
				res.add(set.getValue());
			}
		}

		return res;
	}

	private URI getServiceURI(String serviceName) {

		URI uri = null;
		discv.listener();
		while (true) {
			if (discv.knownUrisOf(serviceName).length > 0) {
				uri = discv.knownUrisOf(serviceName)[0];
				break;
			}
		}

		return uri;


	}


}
