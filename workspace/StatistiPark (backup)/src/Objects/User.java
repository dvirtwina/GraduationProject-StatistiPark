package Objects;

public class User {
	public static final String ROLE_ADMINISTRATOR = "Administrator";
	public static final String ROLE_USER = "User";
	
	private String username;
	private String role;
	
	public User(String username, String role) {
		this.username = username;
		if (role.equals(ROLE_ADMINISTRATOR) || role.equals(ROLE_USER)) {
			this.role = role;
		}
		else {
			this.role = null;
		}
	}
	
	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role;
	}

}
