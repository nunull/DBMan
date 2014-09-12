package de.timmalbers.dbMan.db;

import java.io.IOException;

import de.timmalbers.dbMan.scheme.TableScheme;

public abstract class DB {
	private String path;
	private String username;
	private String password;
	
	public DB(String path, String username, String password) throws IOException {
		this.path = path;
		this.username = username;
		this.password = password;
		
		initDB();
	}
	
	protected abstract void initDB() throws IOException;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public abstract Result get(TableScheme tableScheme) throws IOException;
	
	public abstract void update(TableScheme tableScheme) throws IOException;
}
