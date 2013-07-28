package com.datatheke.restdriver.response;

public class AuthenticateToken {
	private String id;
	private String token;
	private Long expires_at;

	public AuthenticateToken() {
	}

	public AuthenticateToken(String id, String token, Long expires_at) {
		this.id = id;
		this.token = token;
		this.expires_at = expires_at;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpires_at() {
		return expires_at;
	}

	public void setExpires_at(Long expires_at) {
		this.expires_at = expires_at;
	}

	@Override
	public String toString() {
		return "AuthenticateToken [id=" + id + ", token=" + token + ", expires_at=" + expires_at + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expires_at == null) ? 0 : expires_at.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthenticateToken other = (AuthenticateToken) obj;
		if (expires_at == null) {
			if (other.expires_at != null)
				return false;
		} else if (!expires_at.equals(other.expires_at))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
}
