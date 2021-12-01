package es.codeurjc.userservice.query.queries;

public class FindCustomerByIdQuery {
	private String id;

    public FindCustomerByIdQuery(String id) {
        this.id = id;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
