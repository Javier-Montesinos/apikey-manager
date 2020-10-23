package com.montesinos.securedbyheadertoken.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="apikeys")
public class ApiKey {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="user_name")
	private String username;
	
	@Transient	
	private String uuid;
	
	@Column(name="salt")
	private String salt;
	
	@Column(name="hashed_uuid")
	private String hashedUuid;
	
	@Column(name="api_scope")
	private String apiScope;
	
	@Column(name="active")
	private boolean active;
	
	/**
	 * 
	 */
	public ApiKey() {
		
	}

	/**
	 * @param uuid
	 * @param salt
	 * @param hashedUuid
	 * @param active
	 */
	public ApiKey(String uuid, String salt, String hashedUuid, boolean active) {
		super();
		this.uuid = uuid;
		this.salt = salt;
		this.hashedUuid = hashedUuid;
		this.active = active;
	}

	/**
	 * @param username
	 * @param uuid
	 * @param salt
	 * @param hashedUuid
	 * @param active
	 */
	public ApiKey(String username, String uuid, String salt, String hashedUuid, boolean active) {
		super();
		this.username = username;
		this.uuid = uuid;
		this.salt = salt;
		this.hashedUuid = hashedUuid;
		this.active = active;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHashedUuid() {
		return hashedUuid;
	}

	public void setHashedUuid(String hashedUuid) {
		this.hashedUuid = hashedUuid;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}	

	public String getApiScope() {
		return apiScope;
	}

	public void setApiScope(String apiScope) {
		this.apiScope = apiScope;
	}

	@Override
	public String toString() {
		return "ApiKey [id=" + id + ", username=" + username + ", uuid=" + uuid + ", salt=" + salt + ", hashedUuid="
				+ hashedUuid + ", active=" + active + "]";
	}
	
}
